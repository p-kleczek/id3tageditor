package id3editor.main;

/**
 * @author Pawel, Florian, Sebastian (Gruppe 4)
 */

import java.io.File;

import id3editor.MVC.Control;
import id3editor.ViewElements.Application;

public class Editor {

	public static void main(String[] args) {
		Application.initialize();
		
		// XXX: debug
		Control.getControl().addWatchedFolder(new File("C:\\Users\\Pawel\\Documents\\id3test"));
	}

}
