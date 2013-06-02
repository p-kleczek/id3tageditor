package id3editor;

import id3editor.data.MP3File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Observable;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class MP3Player extends Observable {

	private static MP3Player mp3Player = new MP3Player();
	private MP3File song;
	private MyPlayer player;

	private MP3Player() {
	}

	public static MP3Player getInstance() {
		return mp3Player;
	}

	public void playSong(MP3File file) {
		stopSong();
		player = new MyPlayer(file);
		player.start();
		song = file;
		updateGUI();
	}

	@SuppressWarnings("deprecation")
	public void stopSong() {
		if (player != null) {
			player.stop();
			player = null;
			song = null;
			updateGUI();
		}
	}

	/**
	 * @return <code>true</code> if the player playing a song
	 */
	public boolean isPlaying() {
		return player != null;
	}

	public MP3File getSong() {
		return song;
	}

	private void updateGUI() {
		setChanged();
		notifyObservers();
	}

	private class MyPlayer extends Thread {
		private Player player;
		private FileInputStream in;

		public MyPlayer(MP3File song) {
			if (song == null)
				return;
			
			try {
				in = new FileInputStream(song.getFile());
				player = new Player(in);
			} catch (FileNotFoundException | JavaLayerException e) {
				System.err.println("Error opening or playing a song.");
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			if (player != null) {
				try {
					player.play();
				} catch (JavaLayerException e) {
					System.err.println("Error while playing a song");
				}
			}
		}
	}
}
