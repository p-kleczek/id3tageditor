package toolbox;

/**
 * The <code>BitOperation</code> class provides methods to operate on a single
 * byte's bit in a convenient way.
 * 
 * @author Pawel, Florian, Sebastian (Gruppe 4)
 */
public class BitOppereations {

	/**
	 * Set a bit in a byte to 1
	 * 
	 * @param b
	 *            the single byte that have to change
	 * @param pos
	 *            the bit position in the byte(0..7)
	 * @return the changed byte
	 */
	public static byte setBit(byte b, int pos) {
		if (pos >= 0 && pos <= 7) {
			b = (byte) (b | (1 << pos));
		}
		return b;
	}

	/**
	 * Set a bit in a byte to 0
	 * 
	 * @param b
	 *            the single byte that have to change
	 * @param pos
	 *            the bit position in the byte(0..7)
	 * @return the changed byte
	 */
	public static byte clearBit(byte b, int pos) {
		if (pos >= 0 && pos <= 7) {
			b = (byte) (b & ~(1 << pos));
		}
		return b;
	}

	/**
	 * Change a bit in a byte
	 * 
	 * @param b
	 *            the single byte that have to change
	 * @param pos
	 *            the bit position in the byte(0..7)
	 * @return the changed byte
	 */
	public static byte flipBit(byte b, int pos) {
		if (pos >= 0 && pos <= 7) {
			b = (byte) (b ^ (1 << pos));
		}
		return b;
	}

	/**
	 * Read a bit in a byte
	 * 
	 * @param b
	 *            the single byte that have to read
	 * @param pos
	 *            the bit position in the byte(0..7)
	 * @return the value of the bit
	 */
	public static boolean testBit(byte b, int pos) {
		int mask = 1 << pos;
		return (b & mask) == mask;
	}
}
