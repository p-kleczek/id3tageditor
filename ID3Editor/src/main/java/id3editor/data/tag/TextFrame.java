package id3editor.data.tag;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.annotation.XmlElement;

/**
 * 
 * @author Pawel, Florian, Sebastian (Gruppe 4)
 */
public class TextFrame extends MP3TagFrame {

	private byte encoding = 0;
	private String text = "";

	public TextFrame() {
	}

	public TextFrame(byte[] frameHeader, byte[] data) {
		super(frameHeader);

		encoding = data[0];

		byte[] textContent = new byte[data.length - 1];
		System.arraycopy(data, 1, textContent, 0, textContent.length);

		try {
			text = new String(textContent, ENCODINGS[(int) encoding]);
		} catch (UnsupportedEncodingException ex) {
			System.err.println("Error while decoding textFrame");
			System.err.println(ex.toString());
		}
	}

	@XmlElement(name = "encoding")
	public byte getEncoding() {
		return encoding;
	}

	public void setEncoding(byte encoding) {
		this.encoding = encoding;
	}

	@XmlElement(name = "text")
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return getType() + ": " + "TextFrame" + " with " + encoding
				+ " encrypted.\ncontent: " + text;
	}

	@Override
	public Object clone() {
		return new TextFrame(getHeaderBytes(), getContentBytes());
	}

	@Override
	public byte[] getContentBytes() {
		// Text encoding $xx
		// Information <text string according to encoding>

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		try {
			outputStream.write(encoding);
			outputStream.write(text.getBytes(ENCODINGS[(int) encoding]));
		} catch (IOException e) {
			System.err.println("Error in CommentFrame.getContentBytes");
		}

		return outputStream.toByteArray();		
	}
}
