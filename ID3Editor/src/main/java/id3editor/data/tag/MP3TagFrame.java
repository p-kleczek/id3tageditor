package id3editor.data.tag;

/**
 * @author Gruppe4
 */

import id3editor.data.MP3Object;
import id3editor.toolbox.BitOppereations;
import id3editor.toolbox.ByteOpperations;

import javax.xml.bind.annotation.XmlTransient;

public abstract class MP3TagFrame extends MP3Object {

	private final static int TAG_ALTER_PRESERVATION_BIT_NUMBER = 7;
	private final static int FILE_ALTER_PRESERVATION_BIT_NUMBER = 6;
	private final static int READ_ONLY_BIT_NUMBER = 5;
	private final static int COMPRESSION_BIT_NUMBER = 7;
	private final static int ENCRYPTION_BIT_NUMBER = 6;
	private final static int GROUPING_IDENTITY_BIT_NUMBER = 5;

	@XmlTransient
	public static final int HEADER_LENGTH = 10;

	public static final String[] ENC_TYPES = { "ISO-8859-1", "UTF16",
			"UTF-16BE", "UTF-8" };
	private String type = "EMPT";
	private boolean tagAlterPreservation = false;
	private boolean fileAlterPreservation = false;
	private boolean readOnly = false;
	private boolean compression = false;
	private boolean encryption = false;
	private boolean groupingIdentity = false;

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
		tagAlterPreservation = BitOppereations.testBit(flagArray[0],
				TAG_ALTER_PRESERVATION_BIT_NUMBER);
		fileAlterPreservation = BitOppereations.testBit(flagArray[0],
				FILE_ALTER_PRESERVATION_BIT_NUMBER);
		readOnly = BitOppereations.testBit(flagArray[0], READ_ONLY_BIT_NUMBER);
		compression = BitOppereations.testBit(flagArray[1],
				COMPRESSION_BIT_NUMBER);
		encryption = BitOppereations.testBit(flagArray[1],
				ENCRYPTION_BIT_NUMBER);
		groupingIdentity = BitOppereations.testBit(flagArray[1],
				GROUPING_IDENTITY_BIT_NUMBER);
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
			result[8] = BitOppereations.setBit(result[8],
					TAG_ALTER_PRESERVATION_BIT_NUMBER);
		if (fileAlterPreservation)
			result[8] = BitOppereations.setBit(result[8],
					FILE_ALTER_PRESERVATION_BIT_NUMBER);
		if (readOnly)
			result[8] = BitOppereations.setBit(result[8], READ_ONLY_BIT_NUMBER);

		if (compression)
			result[9] = BitOppereations.setBit(result[9],
					COMPRESSION_BIT_NUMBER);
		if (encryption)
			result[9] = BitOppereations
					.setBit(result[9], ENCRYPTION_BIT_NUMBER);
		if (groupingIdentity)
			result[9] = BitOppereations.setBit(result[9],
					GROUPING_IDENTITY_BIT_NUMBER);

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
