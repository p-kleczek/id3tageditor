package id3editor.data.tag;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * 
 * @author Pawel, Florian, Sebastian (Gruppe 4)
 */
public class CommentFrame extends MP3TagFrame {

	private byte encoding = 0;
	private String language = "";
	private String shortDescription = "";
	private String actualText = "";

	public CommentFrame() {
	}

	public CommentFrame(byte[] frameHeader, byte[] content) {
		super(frameHeader);
		this.encoding = content[0];

		byte[] languageArray = new byte[3];
		System.arraycopy(content, 1, languageArray, 0, 3);
		this.language = new String(languageArray);

		int counter = 0;
		while (content[4 + counter] != 0x00) {
			counter++;
		}

		try {
			byte[] discrArray = new byte[counter];
			System.arraycopy(content, 4, discrArray, 0, discrArray.length);
			this.shortDescription = new String(discrArray,
					ENC_TYPES[(int) encoding]);

			byte[] textArray = new byte[content.length - (5 + counter)];
			System.arraycopy(content, 5 + counter, textArray, 0,
					textArray.length);
			this.actualText = new String(textArray, ENC_TYPES[(int) encoding]);
		} catch (UnsupportedEncodingException ex) {
			System.err.println("Error while decoding commentFrame");
			System.err.println(ex.toString());
		}
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	@Override
	public String toString() {
		String result = "Comment";
		if (!shortDescription.equals("")) {
			result += " - short: " + shortDescription;
		}
		if (!actualText.equals("")) {
			result += " - text: " + actualText;
		}
		return result;
	}

	@Override
	public byte[] getContentBytes() {
		// Text encoding $xx
		// Language $xx xx xx
		// Short content descrip. <text string according to encoding> $00 (00)
		// The actual text <full text string according to encoding>
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		try {
			outputStream.write(encoding);
			outputStream.write(language.getBytes());
			outputStream.write(shortDescription
					.getBytes(MP3TagFrame.ENC_TYPES[(int) encoding]));
			outputStream.write((byte) 0x00);
			outputStream.write(actualText
					.getBytes(MP3TagFrame.ENC_TYPES[(int) encoding]));
		} catch (Exception e) {
			System.err.println("Error in CommentFrame.getContentBytes");
		}

		return outputStream.toByteArray();
	}

	public byte getEncoding() {
		return encoding;
	}

	public void setEncoding(byte encoding) {
		this.encoding = encoding;
	}

	public String getActualText() {
		return actualText;
	}

	public void setActualText(String actualText) {
		this.actualText = actualText;
	}

	@Override
	public Object clone() {
		return new CommentFrame(getHeaderBytes(), getContentBytes());
	}

}
