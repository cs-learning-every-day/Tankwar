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
	private static final String IMG_PREFIX = "assets/images/";
	private static final int SPEED = 5;

	private int x;
	private int y;
	private boolean stopped;
	private Direction direction;
	private boolean up, down, left, right;
	private final boolean enemy;


	public Tank(int x, int y, boolean enemy, Direction direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.enemy = enemy;
	}

	public Tank(int x, int y, Direction direction) {
		this(x, y, false, direction);
	}

	public void keyPressed(KeyEvent e) {
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

	public void keyReleased(KeyEvent e) {
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

	public void draw(Graphics g) {
		this.determineDirection();
		this.move();
		g.drawImage(this.getImage().orElseThrow(() ->
						new RuntimeException(direction + " cannot get valid image!")),
				this.x, this.y,
				null);
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

	public void move() {
		if (this.stopped) return;

		switch (direction) {
			case UP:
				y -= SPEED;
				break;
			case DOWN:
				y += SPEED;
				break;
			case LEFT:
				x -= SPEED;
				break;
			case RIGHT:
				x += SPEED;
				break;
			case UPLEFT:
				x -= SPEED;
				y -= SPEED;
				break;
			case UPRIGHT:
				x += SPEED;
				y -= SPEED;
				break;
			case DOWNLEFT:
				x -= SPEED;
				y += SPEED;
				break;
			case DOWNRIGHT:
				x += SPEED;
				y += SPEED;
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

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
}
