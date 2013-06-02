package id3editor.data.tag;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TextFrameTest {
	private final byte[] header = new byte[] { 0x54, 0x30, 0x30, 0x30, 0x00,
			0x00, 0x00, 0x08, (byte) 0b11100000, (byte) 0b11100000 };
	private final byte[] content = new byte[] { 0x00, 0x63, 0x6f, 0x6d, 0x6d,
			0x65, 0x6e, 0x74 };

	@Test
	public void testGetContentBytes() {
		TextFrame frame = new TextFrame();

		frame.setEncoding((byte) 0x00);
		frame.setText("comment");

		assertArrayEquals(content, frame.getContentBytes());
	}

	@Test
	public void testTextFrameByteArrayByteArray() {
		TextFrame frame = new TextFrame(header, content);

		assertEquals(0x00, frame.getEncoding());
		assertEquals("comment", frame.getText());
	}

}
