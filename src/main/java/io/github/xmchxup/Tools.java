package io.github.xmchxup;

import javax.swing.*;
import java.awt.*;

/**
 * @author xmchx (sunhuayangak47@gmail.com)
 */
public class Tools {
	public static Image getImage(String imageName) {
		return new ImageIcon("assets/images/" + imageName).getImage();
	}
}
