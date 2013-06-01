package data;

/**
 * @autor Pawel, Florian, Sebastian (Gruppe 4)
 */
import java.util.ArrayList;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;


public class MP3ObjectTreeModel implements TreeModel {

	private MP3Object root = null;
	private ArrayList<TreeModelListener> listeners = new ArrayList<TreeModelListener>();

	public MP3ObjectTreeModel() {
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		listeners.add(l);
	}

	@Override
	public Object getChild(Object parent, int index) {
		Object result = null;
		if (parent instanceof MP3Object) {
			result = ((MP3Object) parent).getChildAt(index);
		}
		return result;
	}

	@Override
	public int getChildCount(Object parent) {
		int result = 0;
		if (parent instanceof MP3Object) {
			result = ((MP3Object) parent).getChildCount();
		}
		return result;
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		int result = -1;
		if (parent instanceof MP3Object && child instanceof MP3Object) {
			result = ((MP3Object) parent).getIndexOfChild((MP3Object) child);
		}
		return result;
	}

	@Override
	public Object getRoot() {
		return root;
	}

	public void setRoot(MP3Folder root) {
		this.root = root;
		Object[] array = { root };
		for (TreeModelListener listener : listeners) {

			listener.treeStructureChanged(new TreeModelEvent(this, array));
		}
	}

	@Override
	public boolean isLeaf(Object node) {
		boolean result = true;

		// Folder-node if a leaf only if it does not have children.
		if (node instanceof MP3Folder) {
			result = ((MP3Object) node).getChildCount() == 0;
		}
		return result;
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		listeners.remove(l);

	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {

	}

}
