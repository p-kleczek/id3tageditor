package id3editor.data.tag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import id3editor.data.tag.MP3TagFrame.Flag;

import org.junit.Test;

public class MP3TagFrameTest {

	private final byte[] header = new byte[] { 0x45, 0x4d, 0x50, 0x54, 0x00, 0x00, 0x00, 0x00, (byte) 0b11100000, (byte) 0b11100000};

	@Test
	public void testMP3TagFrame() {
		MP3TagFrame frame = new MockMP3TagFrame(header);
		
		assertEquals("EMPT", frame.getType());

		assertTrue(frame.isFlagSet(Flag.TAG_ALTER_PRESERVATION));
		assertTrue(frame.isFlagSet(Flag.FILE_ALTER_PRESERVATION));
		assertTrue(frame.isFlagSet(Flag.READ_ONLY));
		assertTrue(frame.isFlagSet(Flag.GROUPING_IDENTITY));
		assertTrue(frame.isFlagSet(Flag.COMPRESSION));
		assertTrue(frame.isFlagSet(Flag.ENCRYPTION));
	}
	
	
}
