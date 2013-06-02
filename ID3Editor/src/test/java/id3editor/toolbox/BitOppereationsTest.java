package id3editor.toolbox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BitOppereationsTest {

	@Test
	public void testSetBit() {
		assertEquals((byte) 0b00000001,
				BitOppereations.setBit((byte) 0b00000000, 0));
		assertEquals((byte) 0b10000000,
				BitOppereations.setBit((byte) 0b00000000, 7));
	}

	@Test
	public void testClearBit() {
		assertEquals((byte) 0b00010000,
				BitOppereations.clearBit((byte) 0b00010001, 0));
		assertEquals((byte) 0b00010000,
				BitOppereations.clearBit((byte) 0b10010000, 7));
	}

	@Test
	public void testFlipBit() {
		assertEquals((byte) 0b00010000,
				BitOppereations.flipBit((byte) 0b00010001, 0));
		assertEquals((byte) 0b00010001,
				BitOppereations.flipBit((byte) 0b00010000, 0));
	}

	@Test
	public void testTestBit() {
		assertTrue(BitOppereations.testBit((byte) 0b00000001, 0));
		assertFalse(BitOppereations.testBit((byte) 0b00000000, 0));
	}

}
