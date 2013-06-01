package id3editor.data;

import id3editor.parser.Parser;

import java.io.File;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The <code>MP3Folder </code> class is structure representing a folder on the
 * disc. It serves mainly as a logical unit to easily create navigator tree.
 * 
 * @author Pawel, Florian, Sebastian (Gruppe 4)
 */
@XmlRootElement(namespace = "MP3Tool")
public class MP3Folder extends MP3Object {

	File path = new File("");

	public MP3Folder() {

	}

	@XmlElement(name = "path")
	public File getFilePath() {
		return path;
	}

	public void setFilePath(File path) {
		this.path = path;
	}

	/**
	 * Constructor for a Mp3Folder witch creates the complete treestrukture and
	 * parse the Mp3Files.
	 * 
	 * @param pathname
	 *            . Path to the root-Folder
	 */
	public MP3Folder(String pathname) {
		this.path = new File(pathname);
		readChildsFromDisc();
	}

	/**
	 * Reads the structure of this folder and parses all MP3 files stored in it.
	 */
	public void readChildsFromDisc() {
		for (File child : path.listFiles()) {
			if (child.isDirectory()) {
				MP3Folder folder = new MP3Folder(child.getAbsolutePath());
				this.addChild(folder);
			} else if (child.isFile() && child.getName().endsWith(".mp3")) {
				MP3File file = new MP3File(child.getAbsolutePath());
				this.addChild(file);
				Parser.parseMP3File(file);
			}
		}
	}

	@Override
	public String toString() {
		return path.getName();
	}

	@Override
	public void checkForUpdate(double cacheTimestamp) {
		for (int i = 0; i < this.getChildCount(); i++) {
			getChildAt(i).checkForUpdate(cacheTimestamp);
		}
	}

}
