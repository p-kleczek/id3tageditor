package id3editor.parser;

import id3editor.data.MP3File;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Observable;




/**
 * 
 * @author Pawel, Florian, Sebastian (Gruppe 4)
 */
public class Writer extends Observable {

	private static Writer writer = new Writer();
	private boolean running = false;
	private ArrayList<MP3File> jobList = new ArrayList<MP3File>();
	private Thread writeThread;

	private Writer() {
		writeThread = new Thread(new WriterThread());
		writeThread.start();
	}

	public static Writer getWriter() {
		return writer;
	}

	public int getJobCount() {
		return jobList.size();
	}

	public void addJob(MP3File file) {
		jobList.add(file);
	}

	public void addJob(ArrayList<MP3File> files) {
		jobList.addAll(files);
	}

	public void stopWorking() {
		running = false;
	}

	class WriterThread implements Runnable {

		@Override
		public void run() {
			running = true;
			while (running) {
				if (jobList.size() > 0) {
					writeFile(jobList.get(0));
					jobList.remove(0);
				} else {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// no Problem
					}
				}
			}
		}

		/**
		 * Stores changes in the mp3-data
		 * 
		 * @param song
		 *            - Model of a mp3-data
		 */
		private void writeFile(MP3File song) {
			try {

				File orginal = song.getFilePath();
				RandomAccessFile org = new RandomAccessFile(orginal, "rw");
				byte[] tag = song.getBytes();
				byte[] orginalTagHeader = new byte[10];
				org.read(orginalTagHeader);
				int tagSize = Parser.getTagSize(orginalTagHeader);

				org.seek(tagSize);

				byte[] music = new byte[(int) org.length() - tagSize];
				org.read(music);

				org.setLength(0);
				org.seek(0);
				org.write(tag);
				org.write(music);

				org.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
