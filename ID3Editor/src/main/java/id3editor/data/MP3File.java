package id3editor.data;

import id3editor.data.tag.MP3TagFrame;
import id3editor.data.tag.PictureFrame;
import id3editor.data.tag.TextFrame;
import id3editor.parser.Parser;
import id3editor.toolbox.BitOppereations;
import id3editor.toolbox.ByteOpperations;
import id3editor.xml.ByteArrayMarshallerAdapter;

import java.io.File;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 
 * @author Pawel, Florian, Sebastian (Gruppe 4)
 */
public class MP3File extends MP3Object {
	File path = new File("");

	private boolean modified;

	private byte[] fileIdentifier = new byte[3];
	private byte[] version = new byte[2];
	private boolean unsynchronisation = false;
	private boolean extendedHeader = false;
	private boolean experimentalIndicator = false;

	public MP3File() {
	}

	public MP3File(String pathname) {
		this.path = new File(pathname);

		modified = false;
	}

	/**
	 * Reads and set the taginformations for the song
	 * 
	 * @param the
	 *            tagheader as bytearray
	 */
	public void createTag(byte[] tagHeader) {
		System.arraycopy(tagHeader, 0, fileIdentifier, 0, 3);
		System.arraycopy(tagHeader, 3, version, 0, 2);
		unsynchronisation = BitOppereations.testBit(tagHeader[5], 0);
		extendedHeader = BitOppereations.testBit(tagHeader[5], 1);
		experimentalIndicator = BitOppereations.testBit(tagHeader[5], 2);
	}

	@XmlElement(name = "path")
	public File getFilePath() {
		return path;
	}

	public void setFilePath(File path) {
		this.path = path;
	}

	/**
	 * Marks modified files.
	 */
	@Override
	public String toString() {
		String nodeName = this.path.getName();

		if (isModified()) {
			nodeName = "(*) " + nodeName;
		}

		return nodeName;
	}

	/**
	 * Makes a deep copy of the file's data.
	 */
	@Override
	public Object clone() {

		MP3File fileCopy = new MP3File();

		fileCopy.setFilePath(path);

		fileCopy.fileIdentifier = new byte[fileIdentifier.length];
		System.arraycopy(fileIdentifier, 0, fileCopy.fileIdentifier, 0,
				fileIdentifier.length);

		fileCopy.version = new byte[version.length];
		System.arraycopy(version, 0, fileCopy.version, 0, version.length);

		fileCopy.unsynchronisation = unsynchronisation;
		fileCopy.extendedHeader = extendedHeader;
		fileCopy.experimentalIndicator = experimentalIndicator;

		for (MP3Object child : childs) {
			if (child instanceof MP3TagFrame) {
				MP3TagFrame frame = (MP3TagFrame) child;
				fileCopy.addChild((MP3Object) frame.clone());
			}
		}

		return fileCopy;
	}

	@XmlTransient
	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}

	@XmlElement(name = "fileIdentifier")
	public byte[] getFileIdentifier() {
		return fileIdentifier;
	}

	public void setFileIdentifier(byte[] fileIdentifier) {
		this.fileIdentifier = fileIdentifier;
	}

	@XmlElement(name = "version")
	@XmlJavaTypeAdapter(ByteArrayMarshallerAdapter.class)
	public byte[] getVersion() {
		return version;
	}

	public void setVersion(byte[] version) {
		this.version = version;
	}

	@XmlElement(name = "unsynchronisation")
	public boolean isUnsynchronisation() {
		return unsynchronisation;
	}

	public void setUnsynchronisation(boolean unsynchronisation) {
		this.unsynchronisation = unsynchronisation;
	}

	@XmlElement(name = "extendedHeader")
	public boolean isExtendedHeader() {
		return extendedHeader;
	}

	public void setExtendedHeader(boolean extendedHeader) {
		this.extendedHeader = extendedHeader;
	}

	@XmlElement(name = "experimentalIndicator")
	public boolean isExperimentalIndicator() {
		return experimentalIndicator;
	}

	public void setExperimentalIndicator(boolean experimentalIndicator) {
		this.experimentalIndicator = experimentalIndicator;
	}

	/**
	 * Returns the content of the given text frame.
	 * 
	 * @param key
	 *            ID of the text frame
	 * @return content of the text frame
	 */
	public String getTextContentByID(String key) {
		for (MP3Object object : childs) {
			if (object instanceof TextFrame
					&& ((MP3TagFrame) object).getType().equals(key)) {
				return ((TextFrame) object).getText();
			}
		}

		return "";
	}

	/**
	 * Sets new content of the tag.
	 * 
	 * @param tagID
	 *            keyname of the tag
	 * @param text
	 *            value to be set
	 */
	public void setTextContent(String tagID, String text) {
		for (MP3Object object : childs) {
			if (object instanceof TextFrame
					&& ((MP3TagFrame) object).getType().equals(tagID)) {
				((TextFrame) object).setText(text);
				modified = true;
				return;
			}
		}
		TextFrame newFrame = new TextFrame();
		newFrame.setType(tagID);
		newFrame.setText(text);
		childs.add(newFrame);
		newFrame.setParent(this);
		modified = true;
	}

	/**
	 * Returns picture stored in the specified tag.
	 * 
	 * @param tagID
	 *            keyname of the tag
	 * @return byte byte array of image or or zero-length array if there is no
	 *         picture frame
	 */
	public byte[] getCoverPicture() {
		for (MP3Object object : childs) {
			if (object instanceof PictureFrame
					&& ((PictureFrame) object).getType().equals("APIC")) {
				PictureFrame picFrame = (PictureFrame) object;

				String coverString = PictureFrame.PIC_TYPES[PictureFrame.COVER_FRONT];
				if (picFrame.getPictureType().equals(coverString)) {
					return picFrame.getImage();
				}
			}
		}

		return new byte[0];
	}

	/**
	 * Sets the new cover.
	 * 
	 * @param image
	 *            image to be used as a cover or <code>null</code> if the cover
	 *            should be removed.
	 */
	public void setCoverPicture(File image) {
		PictureFrame picFrame = null;

		// DEBUG
		System.out.println("setCover>Piucture");

		// Check is there is already a cover associated with the file.
		for (MP3Object object : childs) {
			if (((MP3TagFrame) object).getType().equals("APIC")) {
				PictureFrame foundPicFrame = (PictureFrame) object;
				String coverString = PictureFrame.PIC_TYPES[PictureFrame.COVER_FRONT];
				if (foundPicFrame.getPictureType().equals(coverString)) {
					picFrame = foundPicFrame;
				}
			}
		}

		modified = true;

		// If there is no image cover, "Remove cover" button should be grayed
		// out.
		assert !(picFrame == null && image == null);

		if (picFrame == null) {
			// No frame and new image - add image frame.
			byte coverFrontCode = 0x03;
			this.addChild(new PictureFrame(coverFrontCode, image));
		} else {
			if (image == null)
				this.removeChild(picFrame);
			else
				picFrame.setImageFromFile(image);
		}
	}

	/**
	 * Converts modified (new) frames into a byte stream. Useful when writing
	 * into the file.
	 * 
	 * @return modified frames as a byte stream
	 */
	public byte[] getBytes() {
		byte[] result = new byte[10];

		System.arraycopy("ID3".getBytes(), 0, result, 0, 3);
		System.arraycopy(version, 0, result, 3, 2);

		if (unsynchronisation)
			BitOppereations.setBit(result[5], 0);

		if (extendedHeader)
			BitOppereations.setBit(result[5], 1);

		if (experimentalIndicator)
			BitOppereations.setBit(result[5], 2);

		System.out.println("getData nach variablen: " + result.length);

		for (MP3Object objectFrame : childs) {

			MP3TagFrame frame = (MP3TagFrame) objectFrame;
			byte[] part = frame.getFrameBytes();
			byte[] buildResult = new byte[result.length + part.length];

			System.arraycopy(result, 0, buildResult, 0, result.length);
			System.arraycopy(part, 0, buildResult, result.length, part.length);
			System.out.println("getData nach frame: " + buildResult.length);
			result = buildResult;
		}

		byte[] tagSize = new byte[4];
		tagSize = ByteOpperations.convertSynchsafeIntToByte(result.length - 10);
		System.arraycopy(tagSize, 0, result, 6, 4);

		return result;
	}

	/**
	 * Update the frames for the song when the song is changed
	 */
	@Override
	public void checkForUpdate(double cacheTimestamp) {
		if (path.lastModified() > cacheTimestamp) {
			this.removeAllChilds();
			Parser.parseMP3File(this);
		}
	}

}
