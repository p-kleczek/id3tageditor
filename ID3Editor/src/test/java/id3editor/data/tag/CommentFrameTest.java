package id3editor.data.tag;

import static org.junit.Assert.*;

import org.junit.Test;

public class CommentFrameTest {

	private final byte[] header = new byte[] { 0x43, 0x4f, 0x4d, 0x4d, 0x00,
			0x00, 0x00, 0x0d, (byte) 0b11100000, (byte) 0b11100000 };
	private final byte[] content = new byte[] { 0x00, 0x70, 0x6f, 0x6c, 0x00,
			0x63, 0x6f, 0x6d, 0x6d, 0x65, 0x6e, 0x74};

	@Test
	public void testGetContentBytes() {
		CommentFrame frame = new CommentFrame();
		
//		frame.setType("COMM");
//		
//		frame.setTagAlterPreservation(true);
//		frame.setFileAlterPreservation(true);
//		frame.setReadOnly(true);
//		frame.setGroupingIdentity(true);
//		frame.setCompression(true);
//		frame.setEncryption(true);
		
		frame.setLanguage("pol");
		frame.setEncoding((byte) 0x00);
		frame.setShortDescription("");
		frame.setActualText("comment");

		assertArrayEquals(content, frame.getContentBytes());
	}

	@Test
	public void testCommentFrameByteArrayByteArray() {
		CommentFrame frame = new CommentFrame(header, content);
		
		assertEquals(0x00, frame.getEncoding());
		assertEquals("pol", frame.getLanguage());
		assertEquals("", frame.getShortDescription());
		assertEquals("comment", frame.getActualText());
	}

}
