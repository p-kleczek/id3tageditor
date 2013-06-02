package id3editor.data.tag;

/**
 * @author Gruppe4
 */

import static id3editor.toolbox.Constants.BITS_PER_BYTE;
import static id3editor.toolbox.Constants.NUL_CHAR;
import id3editor.data.MP3Object;
import id3editor.toolbox.ByteOpperations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.BitSet;

/**
 * The <code>MP3TagFrame</code> is the base class for all ID3v2.3 frames.
 * 
 * @author Pawel
 * 
 */
public abstract class MP3TagFrame extends MP3Object {

	private static final int TYPE_OFFSET = 0;
	private static final int FLAGS_OFFSET = 8;

	private static final int TYPE_SIZE = 4;
	private static final int FLAGS_SIZE = 2;

	/**
	 * Frame header flags.
	 * 
	 * @author Pawel
	 * 
	 */
	public static enum Flag {
		TAG_ALTER_PRESERVATION(7 + BITS_PER_BYTE),
		FILE_ALTER_PRESERVATION(6 + BITS_PER_BYTE),
		READ_ONLY(5 + BITS_PER_BYTE),
		COMPRESSION(7),
		ENCRYPTION(6),
		GROUPING_IDENTITY(5);

		/**
		 * Bit number assuming flag bytes are treated as dword.
		 */
		int bitNo;

		Flag(int bitNo) {
			this.bitNo = bitNo;
		}
	}

	public static final String[] ENCODINGS = {
			"ISO-8859-1",
			"UTF16",
			"UTF-16BE",
			"UTF-8" };

	private String type = "EMPT";

	private BitSet flags = new BitSet(FLAGS_SIZE * BITS_PER_BYTE);

	public MP3TagFrame() {
	}

	public MP3TagFrame(byte[] frameHeader) {
		decryptHeader(frameHeader);
	}

	public MP3TagFrame(String frameType) {
		if (frameType.length() != TYPE_SIZE)
			throw new IllegalArgumentException(
					"frame type must consist of exactly 4 characters");
		this.type = frameType;
	}

	private void decryptHeader(byte[] frameHeader) {
		type = new String(Arrays.copyOfRange(frameHeader, TYPE_OFFSET,
				TYPE_OFFSET + TYPE_SIZE));
		flags = BitSet.valueOf(Arrays.copyOfRange(frameHeader, FLAGS_OFFSET,
				FLAGS_OFFSET + FLAGS_SIZE));
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

	/**
	 * Converts frame's header to its byte representation. Frame's content is
	 * not considered here, so the size is always zero.
	 * 
	 * @return byte representation of the header
	 */
	byte[] getHeaderBytes() {
		// Frame ID $xx xx xx xx
		// Size $xx xx xx xx (not known here)
		// Flags $xx xx

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] unknownSizeBytes = new byte[] {
				NUL_CHAR,
				NUL_CHAR,
				NUL_CHAR,
				NUL_CHAR };

		try {
			outputStream.write(type.getBytes());
			outputStream.write(unknownSizeBytes);
			outputStream.write(flags.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return outputStream.toByteArray();
	}

	/**
	 * Converts frame's content to its byte representation.
	 * 
	 * @return byte representation of the content
	 */
	protected abstract byte[] getContentBytes();

	/**
	 * Converts frame to its byte representation.
	 * 
	 * @return byte representation of the frame
	 */
	public byte[] getFrameBytes() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		try {
			outputStream.write(getHeaderBytes());

			byte[] content = getContentBytes();
			outputStream.write(content);
			outputStream
					.write(ByteOpperations.convertIntToByte(content.length));
		} catch (IOException e) {
			System.err.println("Error in CommentFrame.getContentBytes");
		}

		return outputStream.toByteArray();
	}

	@Override
	public abstract Object clone();

}
