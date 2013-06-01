package id3editor.data.tag;

import static org.junit.Assert.*;

import org.junit.Test;

public class MP3TagFrameTest {

	private final byte[] header = new byte[] { 0x45, 0x4d, 0x50, 0x54, 0x00, 0x00, 0x00, 0x00, (byte) 0b11100000, (byte) 0b11100000};

	@Test
	public void testMP3TagFrame() {
		MP3TagFrame frame = new MockMP3TagFrame(header);
		
		assertEquals("EMPT", frame.getType());

		assertTrue(frame.isTagAlterPreservation());
		assertTrue(frame.isFileAlterPreservation());
		assertTrue(frame.isReadOnly());
		assertTrue(frame.isGroupingIdentity());
		assertTrue(frame.isCompression());
		assertTrue(frame.isEncryption());
	}
	
	
}
