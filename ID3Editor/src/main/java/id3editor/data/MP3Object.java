package id3editor.data;

import id3editor.data.tag.MP3TagFrame;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 * The <code>Mp3Object</code> class is a parent class for these files, folders
 * and tags which can be processed by the MP3Tag editor.
 * 
 * @author Pawel, Florian, Sebastian (Gruppe 4)
 */
@XmlRootElement
public abstract class MP3Object implements Cloneable {

	MP3Object parent = null;

	@XmlElements( { @XmlElement(name = "folder", type = MP3Folder.class),
			@XmlElement(name = "file", type = MP3File.class),
			@XmlElement(name = "tagframe", type = MP3TagFrame.class) })
	ArrayList<MP3Object> childs = new ArrayList<MP3Object>();

	public MP3Object() {
	}

	@XmlTransient
	public MP3Object getParent() {
		return parent;
	}

	public void setParent(MP3Object parent) {
		this.parent = parent;
	}

	public void addChild(MP3Object child) {
		child.setParent(this);
		childs.add(child);
	}

	public void removeChild(MP3Object child) {
		childs.remove(child);
	}

	public void removeAllChilds() {
		childs = new ArrayList<MP3Object>();
	}

	public int getChildCount() {
		return childs.size();
	}

	public MP3Object getChildAt(int index) {
		return childs.get(index);
	}

	public int getIndexOfChild(MP3Object child) {
		return childs.indexOf(child);
	}

	/**
	 * If the timestamp of the file is more current than that of the cache, the
	 * file will be parsed.
	 * 
	 * @param cacheTimestamp
	 */
	public void checkForUpdate(double cacheTimestamp) {
	}
}