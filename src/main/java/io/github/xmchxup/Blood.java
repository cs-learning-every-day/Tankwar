package io.github.xmchxup;

import java.awt.*;

/**
 * @author huayang (sunhuayangak47@gmail.com)
 */
public class Blood {
    private int x, y;

    private final Image image;

    Blood(int x, int y) {
        this.x = x;
        this.y = y;
        this.image = Tools.getImage("blood.png");
    }

    private boolean live = true;

    boolean isLive() {
        return live;
    }

    void setLive(boolean live) {
        this.live = live;
    }

    void draw(Graphics g) {
        g.drawImage(image, x, y, null);
    }

    Rectangle getRectangle() {
        return new Rectangle(x, y, image.getWidth(null), image.getHeight(null));
    }
}
