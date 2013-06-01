package id3editor.filefilter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * The <code>MP3FileFilter</code> class provides a filter for <i>MP3</i> files.
 * 
 * @author Pawel, Florian, Sebastian (Gruppe 4)
 */
public class MP3FileFilter extends FileFilter {

	@Override
	public boolean accept(File file) {
		String path = file.getAbsolutePath().toLowerCase();
		return (path.endsWith(".mp3"));
	}

	@Override
	public String getDescription() {
		return "MP3 files";
	}
}
