package id3editor.toolbox;

import java.nio.ByteBuffer;

/**
 * 
 * @author Pawel, Florian, Sebastian (Gruppe 4)
 */
public class ByteOpperations {

	/**
	 * Convert a synchsafe integer from byte-array to normal integer
	 * 
	 * @param b
	 *            the synchsafe integer as byte-array
	 * @return the normal integer
	 */
	public static int convertSynchsafeByteToInt(byte[] b) {
		int result = 0;

		for (int i = 0; i < b.length; i++) {
			int offset = (b.length - 1 - i) * 7;
			result += b[i] << offset;
		}

		return result;
	}

	/**
	 * Convert a integer from byte-array to normal integer
	 * 
	 * @param intByteArray
	 *            the integer as byte-array
	 * @return the integer
	 */
	public static int convertByteToInt(byte[] intByteArray) {
		return ByteBuffer.wrap(intByteArray).getInt();
	}

	/**
	 * Convert a integer to byte-array
	 * 
	 * @param intByteArray
	 *            the integer
	 * @return the integer as byte-array
	 */
	public static byte[] convertIntToByte(int value) {
		ByteBuffer dbuf = ByteBuffer.allocate(4);
		dbuf.putInt(value);
		return dbuf.array();
	}

	/**
	 * Convert a integer to syncsafe byte-array
	 * 
	 * @param b
	 *            the integer
	 * @return the syncsafe byte-array
	 */
	public static byte[] convertSynchsafeIntToByte(int value) {
		byte[] b = new byte[4];
		int j = 0;
		for (int i = 3; i >= 0; i--) {
			b[i] = (byte) ((value >> (j * 7)) & 0x7f);
			j++;
		}
		return b;
	}
}
