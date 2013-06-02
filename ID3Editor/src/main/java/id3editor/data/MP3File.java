package id3editor.data;

import static id3editor.toolbox.Constants.BITS_PER_BYTE;
import id3editor.data.tag.MP3TagFrame;
import id3editor.data.tag.MP3TagFrameTypes;
import id3editor.data.tag.PictureFrame;
import id3editor.data.tag.TextFrame;
import id3editor.parser.Parser;
import id3editor.toolbox.ByteOpperations;
import id3editor.xml.ByteArrayMarshallerAdapter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.BitSet;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * The <code>MP3File</code> class represents an ID3v2.3 tag header.
 * 
 * @author Pawel, Florian, Sebastian (Gruppe 4)
 */
public class MP3File extends MP3Object {

	private static final int FILE_IDENTIFIER_BYTES = 3;
	private static final int VERSION_BYTES = 2;
	private static final int FLAG_BYTES = 1;

	public static enum Flag {
		UNSYNCHRONIZATION(7), EXTENDED_HEADER(6), EXPERIMENTAL_INDICATOR(5);

		int bitNo;

		Flag(int bitNo) {
			this.bitNo = bitNo;
		}
	}

	private File file = new File("");

	private boolean modified = false;

	private byte[] fileIdentifier = new byte[FILE_IDENTIFIER_BYTES];
	private byte[] version = new byte[VERSION_BYTES];

	private BitSet flags = new BitSet(FLAG_BYTES * BITS_PER_BYTE);

	public MP3File() {
	}

	public MP3File(String pathname) {
		this.file = new File(pathname);
	}

	/**
	 * Parses ID3v2.3 tag header.
	 * 
	 * @param tagHeader
	 *            tag header as byte array
	 */
	public void createTag(byte[] tagHeader) {
		try (ByteArrayInputStream input = new ByteArrayInputStream(tagHeader)) {
			input.read(fileIdentifier);
			input.read(version);

			byte[] flagArray = new byte[FLAG_BYTES];
			input.read(flagArray);
			flags = BitSet.valueOf(flagArray);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@XmlElement(name = "path")
	public File getFile() {
		return file;
	}

	public void setFile(File path) {
		this.file = path;
	}

	@Override
	public String toString() {
		String modificationMarker = isModified() ? "(*) " : "";
		return String.format("%s%s", modificationMarker, file.getName());
	}

	@Override
	public Object clone() {
		MP3File fileCopy = new MP3File();

		fileCopy.file = new File(file.getPath());
		fileCopy.fileIdentifier = Arrays.copyOf(fileIdentifier,
				fileIdentifier.length);
		fileCopy.version = Arrays.copyOf(version, version.length);
		fileCopy.flags = (BitSet) flags.clone();

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
		return flags.get(Flag.UNSYNCHRONIZATION.bitNo);
	}

	public void setUnsynchronisation(boolean value) {
		flags.set(Flag.UNSYNCHRONIZATION.bitNo, value);
	}

	@XmlElement(name = "extendedHeader")
	public boolean isExtendedHeader() {
		return flags.get(Flag.EXTENDED_HEADER.bitNo);
	}

	public void setExtendedHeader(boolean value) {
		flags.set(Flag.EXTENDED_HEADER.bitNo, value);
	}

	@XmlElement(name = "experimentalIndicator")
	public boolean isExperimentalIndicator() {
		return flags.get(Flag.EXPERIMENTAL_INDICATOR.bitNo);
	}

	public void setExperimentalIndicator(boolean value) {
		flags.set(Flag.EXPERIMENTAL_INDICATOR.bitNo, value);
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

		if (frame == null) {
			return new byte[0];
		} else {
			PictureFrame picFrame = (PictureFrame) frame;
			boolean isCoverPicture = picFrame.getPicturesType() == PictureFrame.COVER_FRONT_INDEX;
			return (isCoverPicture ? picFrame.getImage() : new byte[0]);
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
			String coverString = PictureFrame.PICTURE_TYPES[PictureFrame.COVER_FRONT_INDEX];
			if (foundPicFrame.getPictureDescription().equals(coverString)) {
				picFrame = foundPicFrame;
			}
		}

		modified = true;

		if (picFrame == null) {
			// No frame and new image - add image frame.
			addChild(new PictureFrame(PictureFrame.COVER_FRONT_INDEX, image));
		} else {
			if (image == null)
				removeChild(picFrame);
			else
				picFrame.setImageFromFile(image);
		}
	}

	/**
	 * Converts modified (new) frames into a byte stream.
	 * 
	 * @return modified frames as a byte stream
	 */
	public byte[] getBytes() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		try {
			outputStream.write("ID3".getBytes());
			outputStream.write(version);
			outputStream.write(flags.toByteArray());

			int framesSize = 0;
			for (MP3Object objectFrame : childs) {
				byte[] frameBytes = ((MP3TagFrame) objectFrame).getFrameBytes();
				framesSize += frameBytes.length;
				outputStream.write(frameBytes);
			}

			outputStream.write(ByteOpperations
					.convertSynchsafeIntToByte(framesSize));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return outputStream.toByteArray();
	}

	/**
	 * Update the frames for the song when the song is changed
	 */
	@Override
	public void checkForUpdate(double cacheTimestamp) {
		if (file.lastModified() > cacheTimestamp) {
			removeAllChilds();
			Parser.parseMP3File(this);
		}
	}

}
