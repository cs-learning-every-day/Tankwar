package io.github.xmchxup;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Optional;
import java.util.Random;

import static io.github.xmchxup.Direction.*;

/**
 * @author xmchx (sunhuayangak47@gmail.com)
 */
public class Tank {
	private int x;
	private int y;
	private boolean stopped;
	private Direction direction;
	private boolean up, down, left, right;
	private final boolean enemy;
	private final int speed;


	Tank(int x, int y, boolean enemy, int speed, Direction direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.enemy = enemy;
		this.speed = speed;
	}

	Tank(int x, int y, Direction direction) {
		this(x, y, false, GameConfig.PLAYER_SPEED, direction);
	}

	void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_W:
				up = true;
				break;
			case KeyEvent.VK_S:
				down = true;
				break;
			case KeyEvent.VK_A:
				left = true;
				break;
			case KeyEvent.VK_D:
				right = true;
				break;
			case KeyEvent.VK_J:
				fire();
				break;
			case KeyEvent.VK_K:
				superFire();
				break;
		}
		this.determineDirection();
	}


	void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_W:
				up = false;
				break;
			case KeyEvent.VK_S:
				down = false;
				break;
			case KeyEvent.VK_A:
				left = false;
				break;
			case KeyEvent.VK_D:
				right = false;
				break;
		}
		this.determineDirection();
	}

	void draw(Graphics g) {
		int oldX = x, oldY = y;
		this.determineDirection();
		this.move();

		Image image = this.getImage().orElseThrow(
				() -> new RuntimeException(direction + " cannot get valid image!"));

		if (x < 0) {
			x = 0;
		} else if (x > GameConfig.WINDOW_WIDTH - image.getWidth(null)) {
			x = GameConfig.WINDOW_WIDTH - image.getWidth(null);
		}


		if (y < 0) {
			y = 0;
		} else if (y > GameConfig.WINDOW_HEIGHT - image.getHeight(null)) {
			y = GameConfig.WINDOW_HEIGHT - image.getHeight(null);
		}

		Rectangle rec = this.getRectangle();
		GameClient client = GameClient.getInstance();
		for (Wall wall : client.getWalls()) {
			if (rec.intersects(wall.getRectangle())) {
				x = oldX;
				y = oldY;
				break;
			}
		}

		for (Tank tank : client.getEnemyTanks()) {
			if (rec.intersects(tank.getRectangle())) {
				x = oldX;
				y = oldY;
				break;
			}
		}

		g.drawImage(image, this.x, this.y, null);
	}

	private void fire() {
		Image image = getImage().orElseThrow();
		Missile missile = new Missile(x + image.getWidth(null) / 2 - 6,
				y + image.getHeight(null) / 2 - 6, enemy, direction);
		GameClient.getInstance().add(missile);

		playAudio("shoot.wav");
	}

	private void superFire() {
		Image image;
		for (Direction direction : Direction.values()) {
			image = getImage().orElseThrow();

			Missile missile = new Missile(x + image.getWidth(null) / 2 - 6,
					y + image.getHeight(null) / 2 - 6, enemy, direction);
			GameClient.getInstance().add(missile);
		}

		String audioFile = new Random().nextBoolean() ? "supershoot.aiff" : "supershoot.wav";
		playAudio(audioFile);
	}

	private void playAudio(String filename) {
		Media sound = new Media(
				new File("assets/audios/" + filename).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
	}

	private Rectangle getRectangle() {
		Image image = getImage().orElseThrow();
		return new Rectangle(x, y, image.getWidth(null), image.getHeight(null));
	}

	private void determineDirection() {
		if (!up && !down && !left && !right) {
			this.stopped = true;
			return;
		}

		if (up && !down && !left && !right) {
			this.direction = UP;
		} else if (!up && down && !left && !right) {
			this.direction = DOWN;
		} else if (!up && !down && left && !right) {
			this.direction = LEFT;
		} else if (!up && !down && !left && right) {
			this.direction = RIGHT;
		} else if (up && !down && left && !right) {
			this.direction = LEFT_UP;
		} else if (up && !down && !left && right) {
			this.direction = RIGHT_UP;
		} else if (!up && down && left && !right) {
			this.direction = LEFT_DOWN;
		} else if (!up && down && !left && right) {
			this.direction = RIGHT_DOWN;
		}
		this.stopped = false;
	}

	private void move() {
		if (this.stopped) return;

		switch (direction) {
			case UP:
				y -= speed;
				break;
			case DOWN:
				y += speed;
				break;
			case LEFT:
				x -= speed;
				break;
			case RIGHT:
				x += speed;
				break;
			case LEFT_UP:
				x -= speed;
				y -= speed;
				break;
			case RIGHT_UP:
				x += speed;
				y -= speed;
				break;
			case LEFT_DOWN:
				x -= speed;
				y += speed;
				break;
			case RIGHT_DOWN:
				x += speed;
				y += speed;
				break;
		}
	}

	public Optional<Image> getImage() {
		String prefix = enemy ? "e" : "";
		return direction.getImage(prefix + "tank");
	}
}
