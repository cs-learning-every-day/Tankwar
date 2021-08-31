package io.github.xmchxup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Optional;

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
			case KeyEvent.VK_UP:
				up = true;
				break;
			case KeyEvent.VK_DOWN:
				down = true;
				break;
			case KeyEvent.VK_LEFT:
				left = true;
				break;
			case KeyEvent.VK_RIGHT:
				right = true;
				break;
		}
		this.determineDirection();
	}

	void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				up = false;
				break;
			case KeyEvent.VK_DOWN:
				down = false;
				break;
			case KeyEvent.VK_LEFT:
				left = false;
				break;
			case KeyEvent.VK_RIGHT:
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
			this.direction = UPLEFT;
		} else if (up && !down && !left && right) {
			this.direction = UPRIGHT;
		} else if (!up && down && left && !right) {
			this.direction = DOWNLEFT;
		} else if (!up && down && !left && right) {
			this.direction = DOWNRIGHT;
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
			case UPLEFT:
				x -= speed;
				y -= speed;
				break;
			case UPRIGHT:
				x += speed;
				y -= speed;
				break;
			case DOWNLEFT:
				x -= speed;
				y += speed;
				break;
			case DOWNRIGHT:
				x += speed;
				y += speed;
				break;
		}
	}

	public Optional<Image> getImage() {
		String prefix = enemy ? "e" : "";
		switch (direction) {
			case UP:
				return Optional.of(Tools.getImage(prefix + "tankU.gif"));
			case DOWN:
				return Optional.of(Tools.getImage(prefix + "tankD.gif"));
			case LEFT:
				return Optional.of(Tools.getImage(prefix + "tankL.gif"));
			case RIGHT:
				return Optional.of(Tools.getImage(prefix + "tankR.gif"));
			case UPLEFT:
				return Optional.of(Tools.getImage(prefix + "tankLU.gif"));
			case UPRIGHT:
				return Optional.of(Tools.getImage(prefix + "tankRU.gif"));
			case DOWNLEFT:
				return Optional.of(Tools.getImage(prefix + "tankLD.gif"));
			case DOWNRIGHT:
				return Optional.of(Tools.getImage(prefix + "tankRD.gif"));
		}
		return Optional.empty();
	}
}
