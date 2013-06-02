package id3editor.data.tag;

import static id3editor.toolbox.Constants.NUL_CHAR;

import id3editor.toolbox.ByteOpperations;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * The <code>CommentFrame</code> class represents a COMM frame.
 * 
 * @author Pawel, Florian, Sebastian (Gruppe 4)
 */
public class CommentFrame extends MP3TagFrame {

	private static final int LANGUAGE_CODE_SIZE = 3;

	private byte encoding = 0;
	private String language = "";
	private String shortDescription = "";
	private String actualText = "";

	public CommentFrame() {
	}

	public CommentFrame(byte[] frameHeader, byte[] content) {
		super(frameHeader);

		try (ByteArrayInputStream input = new ByteArrayInputStream(content)) {
			encoding = (byte) input.read();

			byte[] languageArray = new byte[LANGUAGE_CODE_SIZE];
			input.read(languageArray);
			language = new String(languageArray);

			byte[] shortDescriptionArray = ByteOpperations.readString(input);
			shortDescription = new String(shortDescriptionArray,
					ENCODINGS[encoding]);

			// Text consist of the very last bytes of the content (after $00
			// byte).
			int textOffset = 1 + LANGUAGE_CODE_SIZE + shortDescription.length()
					+ 1;
			byte[] actualTextArray = Arrays.copyOfRange(content, textOffset,
					content.length);
			actualText = new String(actualTextArray, ENCODINGS[encoding]);
		} catch (IOException e) {
			e.printStackTrace();
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
					.getBytes(MP3TagFrame.ENCODINGS[encoding]));
			outputStream.write(NUL_CHAR);
			outputStream.write(actualText
					.getBytes(MP3TagFrame.ENCODINGS[encoding]));
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
