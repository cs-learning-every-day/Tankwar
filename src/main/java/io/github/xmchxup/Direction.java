package io.github.xmchxup;

import java.awt.*;
import java.util.Optional;

/**
 * @author xmchx (sunhuayangak47@gmail.com)
 */
public enum Direction {
	UP("U"),
	DOWN("D"),
	LEFT("L"),
	RIGHT("R"),
	LEFT_UP("LU"),
	RIGHT_UP("RU"),
	LEFT_DOWN("LD"),
	RIGHT_DOWN("RD");

	private final String abbrev;

	Direction(String abbrev) {
		this.abbrev = abbrev;
	}

	Optional<Image> getImage(String prefix) {
		return Optional.of(Tools.getImage(prefix + abbrev + ".gif"));
	}
}
