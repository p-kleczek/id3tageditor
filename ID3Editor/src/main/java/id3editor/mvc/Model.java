package id3editor.mvc;

import id3editor.data.MP3File;
import id3editor.data.MP3Folder;
import id3editor.data.MP3Object;
import id3editor.data.MP3ObjectTreeModel;
import id3editor.data.tag.MP3TagFrameTypes;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;



/**
 * The <code>Model</code> class serves as a store for all the data used by the
 * MP3Tag editor.
 * 
 * @author Pawel, Florian, Sebastian (Gruppe 4)
 * 
 */
public class Model {

	static Model model = new Model();

	private MP3Folder folderInWork = null;
	private MP3File fileInWork = null;
	private MP3ObjectTreeModel treeModel = new MP3ObjectTreeModel();
	private DefaultTableModel tableModel = new DefaultTableModel(new String[] {
			"Titel", "Interpret", "Album", "Jahr" }, 0);

	private Model() {
	}

	public static Model getModel() {
		return model;
	}

	public MP3File getFileInWork() {
		return fileInWork;
	}

	public MP3Folder getFolderInWork() {
		return folderInWork;
	}

	public MP3ObjectTreeModel getTreeModel() {
		return treeModel;
	}

	public TableModel getTableModel() {
		return tableModel;
	}

	/**
	 * Updates GUI according according to the selected node element type.
	 * 
	 * @param selected
	 *            type of node element
	 */
	public void setSelectedTreeNode(MP3Object selected) {
		if (selected instanceof MP3File) {
			fileInWork = (MP3File) selected;
			folderInWork = (MP3Folder) selected.getParent();
		} else if (selected instanceof MP3Folder) {
			fileInWork = null;
			folderInWork = (MP3Folder) selected;

			// Update GUI.
			tableModel.fireTableDataChanged();
			tableModel.fireTableStructureChanged();
			updateTable();
		} else {
			fileInWork = null;
			folderInWork = null;
		}
	}

	/**
	 * Displays information about all files and folders in currently selected
	 * folder.
	 */
	private void updateTable() {

		// Clear the table (by removing all rows.
		while (tableModel.getRowCount() > 0) {
			tableModel.removeRow(0);
		}

		if (folderInWork != null) {

			// Add information about each child according to it's type
			// (folder/file).
			for (int i = 0; i < folderInWork.getChildCount(); i++) {
				Object child = folderInWork.getChildAt(i);

				if (child instanceof MP3Folder) {
					tableModel.addRow(new String[] { "+ " + child.toString() });
				} else if (child instanceof MP3File) {
					MP3File song = (MP3File) child;
					String name = song
							.getTextContentById(MP3TagFrameTypes.SONGNAME);
					String composer = song
							.getTextContentById(MP3TagFrameTypes.COMPOSER);
					String album = song
							.getTextContentById(MP3TagFrameTypes.ALBUM);
					String year = song
							.getTextContentById(MP3TagFrameTypes.YEAR);

					tableModel.addRow(new String[] { "- " + name, composer,
							album, year });
				}

				tableModel.fireTableDataChanged();
				tableModel.fireTableStructureChanged();
			}
		}
	}

	/**
	 * Returns content of a specified text frame.
	 * 
	 * @param key
	 *            ID of the text frame to be read
	 * @return content of the text frame
	 */
	public String getTextFromActualFile(String key) {
		if (fileInWork != null) {
			return fileInWork.getTextContentById(key);
		} else {
			return "";
		}
	}
}
