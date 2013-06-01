package id3editor.data.tag;

/**
 * @author Gruppe4
 */

import id3editor.data.MP3Object;
import id3editor.toolbox.BitOppereations;
import id3editor.toolbox.ByteOpperations;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;



public abstract class MP3TagFrame extends MP3Object {

	@XmlElements( {
			@XmlElement(name = "defaultframe", type = DefaultFrame.class),
			@XmlElement(name = "commentframe", type = CommentFrame.class),
			@XmlElement(name = "pictureframe", type = PictureFrame.class),
			@XmlElement(name = "textframe", type = TextFrame.class) })
	public static final int HEADER_LENGTH = 10;
	public static final String[] ENC_TYPES = { "ISO-8859-1", "UTF16",
			"UTF-16BE", "UTF-8" };
	String type = "EMPT";
	boolean tagAlterPreservation = false;
	boolean fileAlterPreservation = false;
	boolean readOnly = false;
	boolean compression = false;
	boolean encryption = false;
	boolean groupingIdentity = false;

	public MP3TagFrame() {

	}

	public MP3TagFrame(byte[] frameHeader) {
		decryptHeader(frameHeader);
	}

	public MP3TagFrame(String frameType) {
		if (frameType.length() == 4) {
			this.type = frameType;
		}
	}

	private void decryptHeader(byte[] frameHeader) {
		byte[] typeArray = new byte[4];
		System.arraycopy(frameHeader, 0, typeArray, 0, 4);
		byte[] flagArray = new byte[2];
		System.arraycopy(frameHeader, 8, flagArray, 0, 2);

		type = new String(typeArray);
		tagAlterPreservation = BitOppereations.testBit(flagArray[0], 0);
		fileAlterPreservation = BitOppereations.testBit(flagArray[0], 1);
		readOnly = BitOppereations.testBit(flagArray[0], 2);
		compression = BitOppereations.testBit(flagArray[1], 0);
		encryption = BitOppereations.testBit(flagArray[1], 1);
		groupingIdentity = BitOppereations.testBit(flagArray[1], 2);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isTagAlterPreservation() {
		return tagAlterPreservation;
	}

	public void setTagAlterPreservation(boolean tagAlterPreservation) {
		this.tagAlterPreservation = tagAlterPreservation;
	}

	public boolean isFileAlterPreservation() {
		return fileAlterPreservation;
	}

	public void setFileAlterPreservation(boolean fileAlterPreservation) {
		this.fileAlterPreservation = fileAlterPreservation;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public boolean isCompression() {
		return compression;
	}

	public void setCompression(boolean compression) {
		this.compression = compression;
	}

	public boolean isEncryption() {
		return encryption;
	}

	public void setEncryption(boolean encryption) {
		this.encryption = encryption;
	}

	public boolean isGroupingIdentity() {
		return groupingIdentity;
	}

	public void setGroupingIdentity(boolean groupingIdentity) {
		this.groupingIdentity = groupingIdentity;
	}

	byte[] getHeaderBytes() {
		// Frame ID $xx xx xx xx
		// Size $xx xx xx xx (not known here)
		// Flags $xx xx

		byte[] result = new byte[10];
		System.arraycopy(type.getBytes(), 0, result, 0, type.length());

		if (tagAlterPreservation)
			result[8] = BitOppereations.setBit(result[8], 0);
		if (fileAlterPreservation)
			result[8] = BitOppereations.setBit(result[8], 1);
		if (readOnly)
			result[8] = BitOppereations.setBit(result[8], 2);

		if (compression)
			result[9] = BitOppereations.setBit(result[9], 0);
		if (encryption)
			result[9] = BitOppereations.setBit(result[9], 1);
		if (groupingIdentity)
			result[9] = BitOppereations.setBit(result[9], 2);

		return result;
	}

	/**
	 * 
	 * @return content of a frame as byte[]
	 */
	abstract byte[] getContentBytes();

	/**
	 * 
	 * @return frame including frame-header and frame-content as byte[]
	 */
	public byte[] getFrameBytes() {
		System.out.println(getType());
		byte[] header = getHeaderBytes();
		byte[] content = getContentBytes();

		byte[] result = new byte[header.length + content.length];
		System.arraycopy(header, 0, result, 0, HEADER_LENGTH);
		System.arraycopy(content, 0, result, HEADER_LENGTH, content.length);

		byte[] size = ByteOpperations.convertIntToByte(content.length);
		System.arraycopy(size, 0, result, 4, size.length);

		return result;
	}

	public int getContentSize() {
		return getContentBytes().length;
	}

	@Override
	public abstract Object clone();

}
