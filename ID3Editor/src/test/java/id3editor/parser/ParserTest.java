package id3editor.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class ParserTest {

	@Test
	public void testGetTagSize() {
		final byte[] nullHeader = new byte[] { 0x49, 0x44, 0x33, 0x03,
				0x00, (byte) 0b11100000, 0x00, 0x00, 0b0000001, 0b00111111 };

		assertEquals(191, Parser.getTagSize(nullHeader));
	}

	@Test
	public void testParseMP3File() {
		fail("Not yet implemented");
	}

}
