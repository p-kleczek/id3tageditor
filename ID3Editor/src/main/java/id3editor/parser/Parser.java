package id3editor.parser;

import static id3editor.toolbox.Constants.BYTES_PER_INT;
import static id3editor.toolbox.Constants.TAG_HEADER_LENGTH;
import id3editor.data.MP3File;
import id3editor.data.MP3Object;
import id3editor.data.tag.DefaultFrame;
import id3editor.data.tag.MP3TagFrame;
import id3editor.data.tag.MP3TagFrameTypes;
import id3editor.data.tag.PictureFrame;
import id3editor.data.tag.TextFrame;
import id3editor.data.tag.URLFrame;
import id3editor.toolbox.ByteOpperations;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * 
 * @author Pawel, Florian, Sebastian (Gruppe 4)
 */
public class Parser {

	public static int getTagSize(byte[] tagHeader) {
		final int SIZE_CHUNK_OFFSET = 6;
		byte[] sizeArray = new byte[BYTES_PER_INT];
		System.arraycopy(tagHeader, SIZE_CHUNK_OFFSET, sizeArray, 0,
				BYTES_PER_INT);

		return ByteOpperations.convertSynchsafeByteToInt(sizeArray);
	}

	/**
	 * 
	 * @param frameHeader
	 *            - as a byteArray
	 * @return Size of a frame given by its frameHeader.
	 * @throws IOException
	 *             - while reading sizeArray in convertByteToInt
	 */
	private static int getFrameSize(byte[] frameHeader) throws IOException {
		final int SIZE_CHUNK_OFFSET = 4;
		return ByteBuffer.wrap(frameHeader).getInt(SIZE_CHUNK_OFFSET);
	}

	/**
	 * Reads an parse the original mp3-data and stores the id3-tags in file.
	 * 
	 * @param file
	 *            - Model witch represents the mp3-data.
	 */
	public static void parseMP3File(MP3File file) {
		try {
			RandomAccessFile raf = new RandomAccessFile(file.getFile(), "r");

			byte[] tagHeader = new byte[TAG_HEADER_LENGTH];
			raf.read(tagHeader);

			file.createTag(tagHeader);

			int tagSize = getTagSize(tagHeader);

			byte[] tagContent = new byte[tagSize];
			raf.read(tagContent);

			for (MP3Object obj : parseTagContent(tagContent)) {
				file.addChild(obj);
			}
		} catch (Exception ex) {
			System.err.println("Error while parsing MP3File: "
					+ file.getFile().getName() + " in parser");
			System.err.println(ex.toString());
		}
	}

	/**
	 * 
	 * @param content
	 *            - The complete tag area except the tag-header
	 * @return An ArrayList with all frames of the mp3
	 * @throws IOException
	 *             - while getting frameSize
	 */
	private static ArrayList<MP3TagFrame> parseTagContent(byte[] content)
			throws IOException {

		ArrayList<MP3TagFrame> result = new ArrayList<MP3TagFrame>();
		int offset = 0;

		while (offset + TAG_HEADER_LENGTH < content.length
				&& content[offset + 1] != 0x00) {
			byte[] frameHeader = new byte[TAG_HEADER_LENGTH];
			System.arraycopy(content, offset, frameHeader, 0, TAG_HEADER_LENGTH);

			int frameSize = getFrameSize(frameHeader);
			byte[] frameContent = new byte[frameSize];
			System.arraycopy(content, offset + TAG_HEADER_LENGTH, frameContent, 0,
					frameContent.length);

			// TODO : wykorzystac istniejace metody (getType())
			String frameType = (new DefaultFrame(frameHeader, null)).getType();
			if (frameType.startsWith("T")) {
				TextFrame textFrame = new TextFrame(frameHeader, frameContent);
				result.add(textFrame);
			} else if (frameType.startsWith("W")) {
				URLFrame urlFrame = new URLFrame(frameHeader, frameContent);
				result.add(urlFrame);
				// TODO repair and test the commentFrame
			} else if (frameType.equals(MP3TagFrameTypes.ATTACHED_PICTURE)) {
				PictureFrame pictureFrame = new PictureFrame(frameHeader,
						frameContent);
				result.add(pictureFrame);
			} else {
				MP3TagFrame frame = new DefaultFrame(frameHeader, frameContent);
				result.add(frame);
			}

			offset += TAG_HEADER_LENGTH + frameContent.length;
		}

		return result;
	}
}
