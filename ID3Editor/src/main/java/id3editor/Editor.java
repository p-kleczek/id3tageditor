package id3editor;

/**
 * @author Pawel, Florian, Sebastian (Gruppe 4)
 */

import id3editor.mvc.Application;
import id3editor.mvc.Control;

import java.io.File;


public class Editor {

	public static void main(String[] args) {
		Application.initialize();

		// XXX: debug
		Control.getControl().addWatchedFolder(
				new File("C:\\Users\\Pawel\\Documents\\id3test"));
	}

}
