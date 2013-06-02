package id3editor.data.tag;

import static id3editor.toolbox.Constants.NUL_CHAR;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
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
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int b;
		
		try (ByteArrayInputStream input = new ByteArrayInputStream(content)) {
			encoding = (byte) input.read();
			
			byte[] languageArray = new byte[LANGUAGE_CODE_SIZE];
			input.read(languageArray);
			language = new String(languageArray);
			
			while ((b = input.read()) != NUL_CHAR)
				baos.write(b);
			shortDescription = new String(baos.toByteArray(),
					ENC_TYPES[(int) encoding]);

			int textLength = 0;
			while (content[content.length - textLength - 1] != NUL_CHAR)
				textLength++;
			
			byte[] actualTextArray = new byte[textLength];
			input.read(actualTextArray);
			actualText = new String(actualTextArray, ENC_TYPES[(int) encoding]);
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
					.getBytes(MP3TagFrame.ENC_TYPES[(int) encoding]));
			outputStream.write(NUL_CHAR);
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
