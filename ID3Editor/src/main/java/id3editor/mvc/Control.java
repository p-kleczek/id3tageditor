package id3editor.mvc;

import id3editor.MP3Player;
import id3editor.data.MP3File;
import id3editor.data.MP3Folder;
import id3editor.data.MP3Object;
import id3editor.filefilter.CoverFileFilter;
import id3editor.parser.Writer;
import id3editor.xml.XMLReader;
import id3editor.xml.XMLWriter;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;

/**
 * The <code>Control</code> class provides method to modify the data (eg.
 * changing cover, changing text tags) as well as listeners for View part.
 * 
 * @author Pawel, Florian, Sebastian (Gruppe 4)
 * 
 */
public class Control extends Observable {

	private static Control control = new Control();

	public EditorWindowListener mainWindowListener = new EditorWindowListener();

	private Control() {

	}

	public static Control getControl() {
		return control;
	}

	public void valueChanged(TreeSelectionEvent e) {
		if (e != null && e.getNewLeadSelectionPath() != null) {
			MP3Object selected = (MP3Object) e.getNewLeadSelectionPath()
					.getLastPathComponent();
			if (selected != null) {
				Model.getModel().setSelectedTreeNode(selected);
			}
		}

		callGUI();
	}

	/**
	 * Writes a new cache or override the old one and after it overrides the
	 * mp3-file.
	 */
	public void saveChangesInModel() {
		MP3Object root = (MP3Object) Model.getModel().getTreeModel().getRoot();
		XMLWriter.writeFolderCache((MP3Folder) root);

		List<MP3Object> nodes = new ArrayList<MP3Object>();

		for (int i = 0; i < root.getChildCount(); i++) {
			nodes.add(root.getChildAt(i));
		}

		while (!nodes.isEmpty()) {
			MP3Object node = nodes.get(0);

			for (int i = 0; i < node.getChildCount(); i++) {
				nodes.add(node.getChildAt(i));
			}

			if (node instanceof MP3File) {
				MP3File workingFile = (MP3File) node;
				if (workingFile.isModified()) {
					Writer.getWriter().addJob((MP3File) workingFile.clone());
					workingFile.setModified(false);
				}
			}
			nodes.remove(node);
		}
		callGUI();
	}

	/**
	 * This window listener's implementation prevents user from closing
	 * accidentaly the application while there the writing process is still in
	 * progress.
	 * <p>
	 * When user chooses not to quit instantly, the application will wait until
	 * all the data are stores and then quit automatically.
	 * 
	 * @author Pawel Kleczek
	 * @version 0.1
	 * @since 03-02-2012
	 * 
	 */
	public class EditorWindowListener extends WindowAdapter {

		@Override
		public void windowClosing(WindowEvent e) {
			super.windowClosing(e);

			if (Writer.getWriter().getJobCount() > 0) {
				int ret = JOptionPane.showConfirmDialog(
						Application.getApplication(),
						"Writing process in progress. Quit anyway?", "Warning",
						JOptionPane.YES_NO_OPTION);

				if (ret == JOptionPane.NO_OPTION) {
					while (Writer.getWriter().getJobCount() > 0) {
						try {
							Thread.sleep(50);
						} catch (InterruptedException ie) {
						}
					}
				}
			}
		}
	}

	// ///////////// NEW /////////////////////////
	/**
	 * Changes the current watched folder by using a filechooser.
	 */
	public void addWatchedFolder() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int returnVal = fc.showOpenDialog(Application.getApplication());

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File selectedFolder = fc.getSelectedFile();
			addWatchedFolder(selectedFolder);
		}
	}

	/**
	 * Changes the current watched folder with file.
	 * 
	 * @param file
	 *            folder to be watched
	 */
	public void addWatchedFolder(File file) {

		if (file.exists() && file.isDirectory()) {
			MP3Folder folder = (MP3Folder) XMLReader.readFolderCache(file);

			if (folder == null) {
				folder = new MP3Folder(file.getAbsolutePath());
			} else {
				File cache = new File(file.getAbsolutePath() + File.separator
						+ "cache.xml");
				folder.checkForUpdate(cache.lastModified());
			}
			XMLWriter.writeFolderCache(folder);
			Model.getModel().getTreeModel().setRoot(folder);

			callGUI();
		}
	}

	/**
	 * Changes the value of the specified tag in the currently selected node.
	 * 
	 * @param tagType
	 *            type of the tag to be changed
	 * @param value
	 *            new value of the tag
	 */
	public void modifyCurrentNodeTextTag(String tagType, String value) {
		MP3File file = Model.getModel().getFileInWork();

		file.setTextContent(tagType, value);
		callGUI();
	}

	public void changeCover() {
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new CoverFileFilter());

		int returnVal = fc.showOpenDialog(Application.getApplication());

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File f = fc.getSelectedFile();

			Model.getModel().getFileInWork().setCoverPicture(f);
			callGUI();
		}
		callGUI();
	}

	public void deleteCover() {
		Model.getModel().getFileInWork().setCoverPicture(null);
		callGUI();
	}

	public void doMenuGuiAction(String actionCommand) {
		switch (actionCommand) {
		case "EXIT_MENU":
			System.exit(0);
			break;
		case "ADD_FOLDER_MENU":
			addWatchedFolder();
			break;
		case "SAVE_MENU":
			saveChangesInModel();
			break;
		default:
			throw new IllegalArgumentException(
					"doMenuAction recive unknown command: " + actionCommand);
		}

		callGUI();
	}

	public void doCoverAction(String actionCommand) {

		switch (actionCommand) {
		case "CHANGE_COVER":
			changeCover();
			break;
		case "DELETE_COVER":
			deleteCover();
			break;
		default:
			throw new IllegalArgumentException(
					"doCoverAction recive unknown command: " + actionCommand);
		}
		
		callGUI();
	}

	public void doPlayerAction(String actionCommand) {

		switch (actionCommand) {
		case "START_PLAYER":
			MP3Player.getInstance().playSong(Model.getModel().getFileInWork());
			break;
		case "STOP_PLAYER":
			MP3Player.getInstance().stopSong();
			break;
		default:
			throw new IllegalArgumentException(
					"doPlayerAction recive unknown command: " + actionCommand);
		}

		callGUI();
	}

	public void callGUI() {
		setChanged();
		notifyObservers();
	}
}
