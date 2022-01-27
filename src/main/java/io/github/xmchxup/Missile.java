package io.github.xmchxup;

import java.awt.*;
import java.io.DataInput;
import java.util.Optional;

/**
 * @author xmchx (sunhuayangak47@gmail.com)
 */
public class Missile {
	private static final int SPEED = 12;

	private int x;
	private int y;
	private final boolean enemy;
	private final Direction direction;

	private boolean live = true;

	Missile(int x, int y, boolean enemy, Direction direction) {
		this.x = x;
		this.y = y;
		this.enemy = enemy;
		this.direction = direction;
	}

	Optional<Image> getImage() {
		return direction.getImage("missile");
	}

	void move() {
		x += direction.xFactor * SPEED;
		y += direction.yFactor * SPEED;
	}

	void draw(Graphics g) {
		move();
		if (x < 0 || y < 0 ||
				x > GameConfig.WINDOW_WIDTH ||
				y > GameConfig.WINDOW_HEIGHT) {
			this.live = false;
			return;
		}

		Rectangle rectangle = this.getRectangle();
		for (Wall wall : GameClient.getInstance().getWalls()) {
			if (rectangle.intersects(wall.getRectangle())) {
				this.live = false;
				return;
			}
		}

		if (enemy) {
			Tank playerTank = GameClient.getInstance().getPlayerTank();
			if (rectangle.intersects(playerTank.getRectangleForHitDetection())) {
				addExplosion();
				playerTank.setHp(playerTank.getHp() - 20);
				if (playerTank.getHp() <= 0) {
					playerTank.setLive(false);
				}
				this.setLive(false);
			}
		} else {
			for (Tank enemyTank : GameClient.getInstance().getEnemyTanks()) {
				if (rectangle.intersects(enemyTank.getRectangleForHitDetection())) {
					addExplosion();
					enemyTank.setLive(false);
					this.setLive(false);
					break;
				}
			}
		}

		g.drawImage(getImage().orElseThrow(), x, y, null);
	}

	boolean isLive() {
		return live;
	}

	private void setLive(boolean live) {
		this.live = live;
	}

	private void addExplosion() {
		GameClient.getInstance().addExplosion(new Explosion(x, y));
		Tools.playAudio("explode.wav");
	}

	private Rectangle getRectangle() {
		Image image = getImage().orElseThrow();
		return new Rectangle(x, y, image.getWidth(null), image.getHeight(null));
	}
}
