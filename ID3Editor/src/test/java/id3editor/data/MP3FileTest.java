package id3editor.data;

import static org.junit.Assert.*;
import id3editor.data.tag.TextFrame;

import org.junit.Ignore;
import org.junit.Test;

public class MP3FileTest {

	private final byte[] nullHeader = new byte[] { 0x49, 0x44, 0x33, 0x03,
			0x00, (byte) 0b11100000, 0x00, 0x00, 0x00, 0x00 };

	@Test
	public void testCreateTag() {
		// set up
		MP3File file = new MP3File();
		file.createTag(nullHeader);

		// test
		assertTrue(file.isUnsynchronisation());
		assertTrue(file.isExtendedHeader());
		assertTrue(file.isExperimentalIndicator());
	}

	@Test
	public void testGetTextContentByID() {
		// set up
		MP3File mp3file = new MP3File();

		String textContent = "content";
		String frameId = "T000";
		TextFrame textFrame = new TextFrame();
		textFrame.setType(frameId);
		textFrame.setText(textContent);

		mp3file.addChild(textFrame);

		// test
		assertEquals(textContent, mp3file.getTextContentByID(frameId));
	}

	@Test
	public void testSetTextContent() {
		// set up
		MP3File mp3file = new MP3File();

		String textContent = "content";
		String frameId = "T000";
		TextFrame textFrame = new TextFrame();
		textFrame.setType(frameId);

		mp3file.addChild(textFrame);

		// process
		mp3file.setTextContent(frameId, textContent);

		// test
		assertEquals(textContent, mp3file.getTextContentByID(frameId));
		assertTrue(mp3file.isModified());
	}

	@Ignore
	public void testGetCoverPicture() {
	}

	@Ignore
	public void testSetCoverPicture() {
	}

	@Test
	public void testGetBytes() {
		// set up
		MP3File file = new MP3File();
		file.setVersion(new byte[] { 0x03, 0x00 });
		file.setUnsynchronisation(true);
		file.setExtendedHeader(true);
		file.setExperimentalIndicator(true);

		// test
		assertArrayEquals(nullHeader, file.getBytes());
	}

}
