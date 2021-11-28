package io.github.xmchxup;

import java.awt.*;
import java.io.DataInput;

/**
 * @author xmchx (sunhuayangak47@gmail.com)
 */
public class Missile {
	private static final int SPEED = 12;

	private int x;
	private int y;
	private final boolean enemy;
	private final Direction direction;

	Missile(int x, int y, boolean enemy, Direction direction) {
		this.x = x;
		this.y = y;
		this.enemy = enemy;
		this.direction = direction;
	}

	Image getImage() {
		switch (direction) {
			case UP:
				return Tools.getImage("missileU.gif");
			case UPLEFT:
				return Tools.getImage("missileLU.gif");
			case UPRIGHT:
				return Tools.getImage("missileRU.gif");
			case DOWN:
				return Tools.getImage("missileD.gif");
			case DOWNLEFT:
				return Tools.getImage("missileLD.gif");
			case DOWNRIGHT:
				return Tools.getImage("missileRD.gif");
			case LEFT:
				return Tools.getImage("missileL.gif");
			case RIGHT:
				return Tools.getImage("missileR.gif");
		}
		return null;
	}

	void move() {
		switch (direction) {
			case UP:
				y -= SPEED;
				break;
			case UPLEFT:
				y -= SPEED;
				x -= SPEED;
				break;
			case UPRIGHT:
				y -= SPEED;
				x += SPEED;
				break;
			case DOWN:
				y += SPEED;
				break;
			case DOWNLEFT:
				x -= SPEED;
				y += SPEED;
				break;
			case DOWNRIGHT:
				x += SPEED;
				y += SPEED;
				break;
			case LEFT:
				x -= SPEED;
				break;
			case RIGHT:
				x += SPEED;
				break;
		}
	}

	void draw(Graphics g) {
		move();
		if (x < 0 || y < 0 ||
				x > GameConfig.WINDOW_WIDTH ||
				y > GameConfig.WINDOW_HEIGHT) {
			return;
		}
		g.drawImage(getImage(), x, y, null);
	}
}
