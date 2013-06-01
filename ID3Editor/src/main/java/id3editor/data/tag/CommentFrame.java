package id3editor.data.tag;

import java.io.UnsupportedEncodingException;

/**
 * 
 * @author Pawel, Florian, Sebastian (Gruppe 4)
 */
public class CommentFrame extends MP3TagFrame {

	byte encoding = 0;
	String language = "";
	String shortDescription = "";
	String actualText = "";

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

			byte[] textArray = new byte[content.length - (5 + counter + 1)];
			System.arraycopy(content, 6 + counter, textArray, 0,
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
		byte[] shortDescriptionArray = new byte[0];
		byte[] actualTextArray = new byte[0];

		try {
			shortDescriptionArray = shortDescription
					.getBytes(MP3TagFrame.ENC_TYPES[(int) encoding]);
			actualTextArray = actualText
					.getBytes(MP3TagFrame.ENC_TYPES[encoding]);
		} catch (Exception e) {
			System.err.println("Error in CommentFrame.getContentBytes");
		}

		byte[] result = new byte[5 + shortDescriptionArray.length
				+ actualTextArray.length];

		int offset = 0;

		result[offset] = encoding;
		offset++;

		System.arraycopy(language.getBytes(), 0, result, offset, 3);
		offset += 3;

		System.arraycopy(shortDescriptionArray, 0, result, offset,
				shortDescription.length());
		offset += shortDescriptionArray.length + 1;

		System.arraycopy(actualTextArray, 0, result, offset,
				actualTextArray.length);

		return result;
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
