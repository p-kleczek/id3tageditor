package id3editor.toolbox;

/**
 * The <code>BitOperation</code> class provides methods to operate on a single
 * byte's bit in a convenient way.
 * 
 * @author Pawel, Florian, Sebastian (Gruppe 4)
 */
public class BitOppereations {

	/**
	 * Ensure that the position (bit No.) is valid.
	 * 
	 * @param pos
	 *            number of a bit
	 */
	private static void testPosition(int pos) {
		final int LSB = 0;
		final int MSB = 7;

		if (pos < LSB || pos > MSB)
			throw new IllegalArgumentException(String.format(
					"pos=%d and MSB=%d", pos, MSB));
	}

	/**
	 * Set a bit in a byte to 1
	 * 
	 * @param b
	 *            the single byte that have to change
	 * @param pos
	 *            the bit position in the byte(7..0)
	 * @return the changed byte
	 */
	public static byte setBit(byte b, int pos) {
		testPosition(pos);
		return (byte) (b | (1 << pos));
	}

	/**
	 * Set a bit in a byte to 0
	 * 
	 * @param b
	 *            the single byte that have to change
	 * @param pos
	 *            the bit position in the byte(7..0)
	 * @return the changed byte
	 */
	public static byte clearBit(byte b, int pos) {
		testPosition(pos);
		return (byte) (b & ~(1 << pos));
	}

	/**
	 * Change a bit in a byte
	 * 
	 * @param b
	 *            the single byte that have to change
	 * @param pos
	 *            the bit position in the byte(7..0)
	 * @return the changed byte
	 */
	public static byte flipBit(byte b, int pos) {
		testPosition(pos);
		return (byte) (b ^ (1 << pos));
	}

	/**
	 * Read a bit in a byte
	 * 
	 * @param b
	 *            the single byte that have to read
	 * @param pos
	 *            the bit position in the byte(7..0)
	 * @return the value of the bit
	 */
	public static boolean testBit(byte b, int pos) {
		testPosition(pos);

		int mask = 1 << pos;
		return (b & mask) == mask;
	}
}
