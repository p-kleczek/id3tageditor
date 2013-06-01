package id3editor.parser;

import id3editor.data.MP3File;
import id3editor.data.MP3Object;
import id3editor.data.tag.DefaultFrame;
import id3editor.data.tag.MP3TagFrame;
import id3editor.data.tag.PictureFrame;
import id3editor.data.tag.TextFrame;
import id3editor.data.tag.URLFrame;
import id3editor.toolbox.ByteOpperations;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * 
 * @author Pawel, Florian, Sebastian (Gruppe 4)
 */
public class Parser {

	public static int getTagSize(byte[] tagHeader) throws Exception {
		byte[] sizeArray = new byte[4];
		System.arraycopy(tagHeader, 6, sizeArray, 0, 4);

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
		byte[] sizeArray = new byte[4];
		System.arraycopy(frameHeader, 4, sizeArray, 0, 4);

		return ByteOpperations.convertByteToInt(sizeArray);
	}

	/**
	 * Reads an parse the original mp3-data and stores the id3-tags in file.
	 * 
	 * @param file
	 *            - Model witch represents the mp3-data.
	 */
	public static void parseMP3File(MP3File file) {
		try {
			RandomAccessFile raf = new RandomAccessFile(file.getFilePath(), "r");

			byte[] tagHeader = new byte[10];
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
					+ file.getFilePath().getName() + " in parser");
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
		int counter = 0;
		while (counter + 10 < content.length && content[counter + 1] != 0x00) {
			byte[] frameHeader = new byte[10];
			System.arraycopy(content, counter, frameHeader, 0, 10);

			int frameSize = getFrameSize(frameHeader);
			byte[] frameContent = new byte[frameSize];
			System.arraycopy(content, counter + 10, frameContent, 0,
					frameContent.length);

			if (new String(frameHeader).startsWith("T")) {
				TextFrame textFrame = new TextFrame(frameHeader, frameContent);
				result.add(textFrame);
			} else if (new String(frameHeader).startsWith("W")) {
				URLFrame urlFrame = new URLFrame(frameHeader, frameContent);
				result.add(urlFrame);
				// TODO repair and test the commentFrame
			} else if (new String(frameHeader).startsWith("APIC")) {
				PictureFrame pictureFrame = new PictureFrame(frameHeader,
						frameContent);
				result.add(pictureFrame);
			} else {
				MP3TagFrame frame = new DefaultFrame(frameHeader, frameContent);
				result.add(frame);
			}
			counter += 10 + frameContent.length;
		}

		return result;
	}
}
