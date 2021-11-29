package io.github.xmchxup;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * @author xmchx (sunhuayangak47@gmail.com)
 */
public class Tools {
	public static Image getImage(String imageName) {
		return new ImageIcon("assets/images/" + imageName).getImage();
	}

	public static void playAudio(String fileName) {
		Media sound = new Media(new File("assets/audios/" + fileName).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
	}
}
