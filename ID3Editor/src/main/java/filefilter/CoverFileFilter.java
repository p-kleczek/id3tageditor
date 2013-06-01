package filefilter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * The <code>CoverFileFilter</code> class provides a filter for <i>PNG</i> and
 * <i>JPG</i> files (eg. used as a cover).
 *
 * @author Pawel, Florian, Sebastian (Gruppe 4)
 * 
 */
public class CoverFileFilter extends FileFilter {

	@Override
	public boolean accept(File file) {
		if (file.isDirectory())
			return true;

		String fileName = file.getName().toLowerCase();
		return (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName
				.endsWith(".jpeg"));
	}

	@Override
	public String getDescription() {
		return "PNG and JPG";
	}

}
