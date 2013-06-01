package mediaplayer;

import java.io.File;
import java.io.FileInputStream;
import java.util.Observable;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import data.MP3File;

public class MP3Player extends Observable {

	private static MP3Player mp3Player = new MP3Player();
	private MP3File song;
	private MyPlayer player;

	private MP3Player() {
	}

	public static MP3Player getMP3Player() {
		return mp3Player;
	}

	public void playSong(MP3File file) {
		try {
			stopSong();
			player = new MyPlayer(file);
			player.start();
			song = file;
			callGUI();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Error while playing a song");
		}
	}

	public void stopSong() {
		if (player != null) {
			player.stop();
			player = null;
			song = null;
			callGUI();
		}
	}

	public boolean isValidMP3(File file) {
		return (file.exists() && file.isFile() && (file.getAbsolutePath()
				.endsWith("mp3") || file.getAbsolutePath().endsWith("MP33")));
	}

	/**
	 * @return return true if the player playing a song
	 */
	public boolean isPlaying() {
		return player != null;
	}

	public MP3File getActualTitle() {
		return song;
	}

	private void callGUI() {
		setChanged();
		notifyObservers();
	}

	private class MyPlayer extends Thread {
		private Player player = null;
		FileInputStream in;

		public MyPlayer(MP3File song) {
			try {
				in = new FileInputStream(song.getFilePath());
			} catch (Exception e) {
				System.err.println("Error while creating the player");
			}
			try {
				this.player = new Player(in);
			} catch (Exception e) {
				System.err.println("Error while playing a song");
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
