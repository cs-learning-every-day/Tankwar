package io.github.xmchxup;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Optional;
import java.util.Random;

import io.github.xmchxup.Save.*;

import static io.github.xmchxup.Direction.*;


/**
 * @author xmchx (sunhuayangak47@gmail.com)
 */
public class Tank {
    private static final int MAX_HP = 100;
    private int x;
    private int y;
    private boolean stopped;
    private Direction direction;
    private final boolean enemy;
    private final int speed;
    private boolean live = true;
    private int hp = MAX_HP;
    private int code;


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

    Tank(Position position, boolean enemy) {
        this(position.getX(), position.getY(), enemy, position.getDirection());
    }

    Tank(int x, int y, boolean enemy, Direction direction) {
        this(x, y, enemy, GameConfig.PLAYER_SPEED, direction);
    }

    void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                code |= Direction.UP.code;
                break;
            case KeyEvent.VK_S:
                code |= Direction.DOWN.code;
                break;
            case KeyEvent.VK_A:
                code |= Direction.LEFT.code;
                break;
            case KeyEvent.VK_D:
                code |= Direction.RIGHT.code;
                break;
            case KeyEvent.VK_J:
                fire();
                break;
            case KeyEvent.VK_K:
                superFire();
                break;
            case KeyEvent.VK_U:
                GameClient.getInstance().restart();
                break;
        }
        this.determineDirection();
    }

    void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                code ^= UP.code;
                break;
            case KeyEvent.VK_S:
                code ^= DOWN.code;
                break;
            case KeyEvent.VK_A:
                code ^= LEFT.code;
                break;
            case KeyEvent.VK_D:
                code ^= RIGHT.code;
                break;
        }
        this.determineDirection();
    }

    void draw(Graphics g) {
        int oldX = x, oldY = y;
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
            if (tank != this && rec.intersects(tank.getRectangle())) {
                x = oldX;
                y = oldY;
                break;
            }
        }

        if (this.enemy && rec.intersects(
                GameClient.getInstance().getPlayerTank().getRectangle())) {
            x = oldX;
            y = oldY;
        }

        if (!enemy) {
            Blood blood = GameClient.getInstance().getBlood();
            if (blood.isLive() && rec.intersects(blood.getRectangle())) {
                this.hp = MAX_HP;
                Tools.playAudio("revive.wav");
                blood.setLive(false);
            }

            g.setColor(Color.WHITE);
            g.fillRect(x, y - 10, image.getWidth(null), 10);

            g.setColor(Color.RED);
            int width = hp * image.getWidth(null) / MAX_HP;
            g.fillRect(x, y - 10, width, 10);

            Image petImage = Tools.getImage("pet-camel.gif");
            g.drawImage(petImage, this.x - petImage.getWidth(null) - DISTANCE_TO_PET, this.y, null);
        }

        g.drawImage(image, this.x, this.y, null);
    }

    private void fire() {
        Image image = getImage().orElseThrow();
        Missile missile = new Missile(x + image.getWidth(null) / 2 - 6,
                y + image.getHeight(null) / 2 - 6, enemy, direction);
        GameClient.getInstance().add(missile);

        Tools.playAudio("shoot.wav");
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
        Tools.playAudio(audioFile);
    }

    private static final int DISTANCE_TO_PET = 4;

    Rectangle getRectangle() {
        Image image = getImage().orElseThrow();
        if (enemy) {
            return new Rectangle(x, y, image.getWidth(null), image.getHeight(null));
        } else {
            Image petImage = Tools.getImage("pet-camel.gif");
            int delta = petImage.getWidth(null) + DISTANCE_TO_PET;
            return new Rectangle(x - delta, y,
                    image.getWidth(null) + delta, image.getHeight(null));
        }
    }

    Rectangle getRectangleForHitDetection() {
        Image image = getImage().orElseThrow();
        return new Rectangle(x, y, image.getWidth(null), image.getHeight(null));
    }

    private void determineDirection() {
        Direction newDirection = Direction.get(code);
        if (newDirection == null) {
            this.stopped = true;
            return;
        }

        this.direction = newDirection;
        this.stopped = false;
    }

    private void move() {
        if (this.stopped) return;
        x += direction.xFactor * speed;
        y += direction.yFactor * speed;
    }

    Optional<Image> getImage() {
        String prefix = enemy ? "e" : "";
        return direction.getImage(prefix + "tank");
    }

    boolean isLive() {
        return live;
    }

    void setLive(boolean live) {
        this.live = live;
    }

    int getHp() {
        return hp;
    }

    void setHp(int hp) {
        this.hp = hp;
    }

    boolean isEnemy() {
        return enemy;
    }

    boolean isDying() {
        return this.hp <= MAX_HP * 0.2;
    }

    Position getPosition() {
        return new Position(x, y, direction);
    }

    private final Random random = new Random();

    private int step = random.nextInt(12) + 3;

    void actRandomly() {
        Direction[] dirs = Direction.values();
        if (step == 0) {
            step = random.nextInt(12) + 3;
            this.direction = dirs[random.nextInt(dirs.length)];
            if (random.nextBoolean()) {
                this.fire();
            }
        }
        step--;
    }
}
