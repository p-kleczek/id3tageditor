package id3editor.data;

import id3editor.data.tag.MP3TagFrame;
import id3editor.data.tag.MP3TagFrameTypes;
import id3editor.data.tag.PictureFrame;
import id3editor.data.tag.TextFrame;
import id3editor.parser.Parser;
import id3editor.toolbox.BitOppereations;
import id3editor.toolbox.ByteOpperations;
import id3editor.xml.ByteArrayMarshallerAdapter;

import static id3editor.toolbox.Constants.*;

import java.io.File;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 
 * @author Pawel, Florian, Sebastian (Gruppe 4)
 */
public class MP3File extends MP3Object {

	private static final int FLAGS_OFFSET = 5;
	private static final int UNSYNCHRONIZATION_BIT_NUMBER = 7;
	private static final int EXTENDED_HEADER_BIT_NUMBER = 6;
	private static final int EXPERIMENTAL_INDICATOR_BIT_NUMBER = 5;

	private File path = new File("");

	private boolean modified = false;

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
	 * Parses ID3v2 tag header.
	 * 
	 * @param tagHeader
	 *            tag header as byte array
	 */
	public void createTag(byte[] tagHeader) {
		System.arraycopy(tagHeader, 0, fileIdentifier, 0, fileIdentifier.length);
		System.arraycopy(tagHeader, 3, version, 0, version.length);
		unsynchronisation = BitOppereations.testBit(tagHeader[FLAGS_OFFSET],
				UNSYNCHRONIZATION_BIT_NUMBER);
		extendedHeader = BitOppereations.testBit(tagHeader[FLAGS_OFFSET],
				EXTENDED_HEADER_BIT_NUMBER);
		experimentalIndicator = BitOppereations.testBit(
				tagHeader[FLAGS_OFFSET], EXPERIMENTAL_INDICATOR_BIT_NUMBER);
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

	private MP3TagFrame getFrameById(String frameId) {
		for (MP3Object object : childs) {
			String objectType = ((MP3TagFrame) object).getType();
			if (objectType.equals(frameId)) {
				return (MP3TagFrame) object;
			}
		}

		return null;
	}

	/**
	 * Returns the content of the given text frame.
	 * 
	 * @param frameId
	 *            ID of the text frame
	 * @return content of the text frame
	 */
	public String getTextContentById(String frameId) {
		MP3TagFrame frame = getFrameById(frameId);
		return (frame == null) ? "" : ((TextFrame) frame).getText();
	}

	/**
	 * Sets new content of the tag.
	 * 
	 * @param frameId
	 *            keyname of the tag
	 * @param text
	 *            value to be set
	 */
	public void setTextContent(String frameId, String text) {
		modified = true;
		MP3TagFrame frame = getFrameById(frameId);

		if (frame == null) {
			addNewTextFrame(frameId, text);
		} else {
			((TextFrame) frame).setText(text);
		}
	}

	private void addNewTextFrame(String tagId, String text) {
		TextFrame newFrame = new TextFrame();
		newFrame.setType(tagId);
		newFrame.setText(text);
		childs.add(newFrame);
		newFrame.setParent(this);
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
		MP3TagFrame frame = getFrameById(MP3TagFrameTypes.ATTACHED_PICTURE);
		String coverString = PictureFrame.PIC_TYPES[PictureFrame.COVER_FRONT];
		
		if (frame == null) {
			return new byte[0];
		} else {
			PictureFrame picFrame = (PictureFrame) frame;
			return (picFrame.getPictureType().equals(coverString) ? picFrame.getImage() : null);
		}
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
		MP3TagFrame frame = getFrameById(MP3TagFrameTypes.ATTACHED_PICTURE);
		
		if (frame != null) {
			PictureFrame foundPicFrame = (PictureFrame) frame;
			String coverString = PictureFrame.PIC_TYPES[PictureFrame.COVER_FRONT];
			if (foundPicFrame.getPictureType().equals(coverString)) {
				picFrame = foundPicFrame;
			}
		}

		modified = true;

		if (picFrame == null) {
			// No frame and new image - add image frame.
			this.addChild(new PictureFrame(PictureFrame.COVER_FRONT, image));
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
		byte[] result = new byte[TAG_HEADER_LENGTH];

		System.arraycopy("ID3".getBytes(), 0, result, 0, 3);
		System.arraycopy(version, 0, result, 3, version.length);

		if (unsynchronisation)
			result[5] = BitOppereations.setBit(result[FLAGS_OFFSET],
					UNSYNCHRONIZATION_BIT_NUMBER);

		if (extendedHeader)
			result[5] = BitOppereations.setBit(result[FLAGS_OFFSET],
					EXTENDED_HEADER_BIT_NUMBER);

		if (experimentalIndicator)
			result[5] = BitOppereations.setBit(result[FLAGS_OFFSET],
					EXPERIMENTAL_INDICATOR_BIT_NUMBER);

		for (MP3Object objectFrame : childs) {

			MP3TagFrame frame = (MP3TagFrame) objectFrame;
			byte[] part = frame.getFrameBytes();
			byte[] buildResult = new byte[result.length + part.length];

			System.arraycopy(result, 0, buildResult, 0, result.length);
			System.arraycopy(part, 0, buildResult, result.length, part.length);
			result = buildResult;
		}

		byte[] tagSize = new byte[BYTES_PER_INT];
		tagSize = ByteOpperations.convertSynchsafeIntToByte(result.length - TAG_HEADER_LENGTH);
		System.arraycopy(tagSize, 0, result, 6, tagSize.length);

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
