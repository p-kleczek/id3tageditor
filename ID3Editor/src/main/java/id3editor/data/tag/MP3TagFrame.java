package id3editor.data.tag;

/**
 * @author Gruppe4
 */

import static id3editor.toolbox.Constants.BITS_PER_BYTE;
import id3editor.data.MP3Object;
import id3editor.toolbox.ByteOpperations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.BitSet;

import javax.xml.bind.annotation.XmlTransient;

public abstract class MP3TagFrame extends MP3Object {

	public static enum Flag {
		TAG_ALTER_PRESERVATION(7 + BITS_PER_BYTE),
		FILE_ALTER_PRESERVATION(6 + BITS_PER_BYTE),
		READ_ONLY(5 + BITS_PER_BYTE),
		COMPRESSION(7),
		ENCRYPTION(6),
		GROUPING_IDENTITY(5);

		private final int bitNo;

		private Flag(int bitNo) {
			this.bitNo = bitNo;
		}
	}

	@XmlTransient
	public static final int HEADER_LENGTH = 10;

	public static final String[] ENC_TYPES = { "ISO-8859-1", "UTF16",
			"UTF-16BE", "UTF-8" };
	private String type = "EMPT";

	private BitSet flags = new BitSet(2 * BITS_PER_BYTE);

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
		type = new String(Arrays.copyOfRange(frameHeader, 0, 4));
		flags = BitSet.valueOf(Arrays.copyOfRange(frameHeader, 8, 10));
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isFlagSet(Flag flag) {
		return flags.get(flag.bitNo);
	}

	public void setFlag(Flag flag, boolean value) {
		flags.set(flag.bitNo, value);
	}

	byte[] getHeaderBytes() {
		// Frame ID $xx xx xx xx
		// Size $xx xx xx xx (not known here)
		// Flags $xx xx

		byte[] result = new byte[10];
		System.arraycopy(type.getBytes(), 0, result, 0, type.length());

		byte[] flagArray = flags.toByteArray();
		System.arraycopy(flagArray, 0, result, 8, flagArray.length);

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
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		try {
			outputStream.write(getHeaderBytes());
			
			byte[] content = getContentBytes();
			outputStream.write(content);
			outputStream.write(ByteOpperations.convertIntToByte(content.length));
		} catch (IOException e) {
			System.err.println("Error in CommentFrame.getContentBytes");
		}		
		
		return outputStream.toByteArray();
	}

	public int getContentSize() {
		return getContentBytes().length;
	}

	@Override
	public abstract Object clone();

}
