package toolbox;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * 
 * @author Pawel, Florian, Sebastian (Gruppe 4)
 */
public class ByteOpperations {

	/**
	 * Convert a syncsafe integer from byte-array to normal integer
	 * 
	 * @param b
	 *            the syncsafe integer as byte-array
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
		int size = 0;
		ByteArrayInputStream arrayInputStream = null;
		DataInputStream dataInputStream = null;

		try {
			arrayInputStream = new ByteArrayInputStream(intByteArray);
			dataInputStream = new DataInputStream(arrayInputStream);
			size = dataInputStream.readInt();
		} catch (IOException e) {
			System.err
					.println("Failure while create inputstreams in \"convertByteToInt\"");
			System.err.println(e.toString());
		} finally {
			try {
				if (arrayInputStream != null) {
					arrayInputStream.close();
				}

				if (dataInputStream != null) {
					dataInputStream.close();
				}
			} catch (Exception e) {
				System.err
						.println("Failure while closing inputstreams in \"convertByteToInt\"");
				System.err.println(e.toString());
			}
		}
		return size;
	}

	/**
	 * Convert a integer to byte-array
	 * 
	 * @param intByteArray
	 *            the integer
	 * @return the integer as byte-array
	 */
	public static byte[] convertIntToByte(int value) {
		byte[] result = new byte[4];
		result[0] = (byte) (value >> 24);
		result[1] = (byte) ((value << 8) >> 24);
		result[2] = (byte) ((value << 16) >> 24);
		result[3] = (byte) ((value << 24) >> 24);
		return result;
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
