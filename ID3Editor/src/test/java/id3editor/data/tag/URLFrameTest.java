package id3editor.data.tag;

import static org.junit.Assert.*;

import org.junit.Test;

public class URLFrameTest {
	private final byte[] header = new byte[] { 0x54, 0x30, 0x30, 0x30, 0x00,
			0x00, 0x00, 0x08, (byte) 0b11100000, (byte) 0b11100000 };
	private final byte[] content = new byte[] { 0x77, 0x77, 0x77, 0x2e, 0x67,
			0x6f, 0x6f, 0x67, 0x6c, 0x65, 0x2e, 0x63, 0x6f, 0x6d };

	@Test
	public void testGetContentBytes() {
		URLFrame frame = new URLFrame();

		frame.setUrl("www.google.com");

		assertArrayEquals(content, frame.getContentBytes());
	}

	@Test
	public void testURLFrameByteArrayByteArray() {
		URLFrame frame = new URLFrame(header, content);

		assertEquals("www.google.com", frame.getUrl());
	}

}
