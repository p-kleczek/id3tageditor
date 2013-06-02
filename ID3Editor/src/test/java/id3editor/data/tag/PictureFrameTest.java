package id3editor.data.tag;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PictureFrameTest {

	private final byte[] header = new byte[] { 0x43, 0x4f, 0x4d, 0x4d, 0x00,
			0x00, 0x00, 0x0b, (byte) 0b11100000, (byte) 0b11100000 };
	private final byte[] content = new byte[] { 0x00, 0x6a, 0x70, 0x67, 0x00,
			0x03, 0x78, 0x78, 0x78, 0x00, 0x01 };

	@Test
	public void testGetContentBytes() {
		PictureFrame frame = new PictureFrame();

		frame.setEncoding((byte) 0x00);
		frame.setMimeType("jpg");
		frame.setPicturesType((byte) 0x03);
		frame.setDescription("xxx");
		frame.setImage(new byte[] { 0x01 });
		
		assertArrayEquals(content, frame.getContentBytes());
	}

	@Test
	public void testPictureFrameByteArrayByteArray() {
		PictureFrame frame = new PictureFrame(header, content);

		assertEquals((byte) 0x00, frame.getEncoding());
		assertEquals("jpg", frame.getMimeType());
		assertEquals((byte) 0x03, frame.getPicturesType());
		assertEquals("xxx", frame.getDescription());
		assertArrayEquals(new byte[] { 0x01 }, frame.getImage());
	}

}
