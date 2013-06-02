package id3editor.data.tag;

/**
 * 
 * @author Pawel, Florian, Sebastian (Gruppe 4)
 */

import static id3editor.toolbox.Constants.NUL_CHAR;
import id3editor.toolbox.ByteOpperations;
import id3editor.toolbox.ImageOpperations;
import id3editor.xml.ByteArrayMarshallerAdapter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * The <code>CommentFrame</code> class represents an APIC frame.
 * 
 * @author Gruppe4
 */
public class PictureFrame extends MP3TagFrame {

	public static byte COVER_FRONT_INDEX = 3;

	public static final String[] PICTURE_TYPES = {
			"Other",
			"32x32 pixels file icon",
			"Other file icon",
			"Cover (front)",
			"Cover (back)",
			"Leaflet page",
			"Media (e.g. lable side of CD)",
			"Lead artist/lead performer/soloist",
			"Artist/performer",
			"Conductor",
			"Band/Orchestra",
			"Composer",
			"Lyricist/text writer",
			"Recording Location",
			"During recording",
			"During performance",
			"Movie/video screen capture",
			"A bright coloured fish",
			"Illustration",
			"Band/artist logotype",
			"Publisher/Studio logotype" };

	private byte encoding = NUL_CHAR;
	private String mimeType = "";
	private byte picturesType = NUL_CHAR;
	private String description = "";
	private byte[] image = new byte[0];

	public PictureFrame() {
	}

	public PictureFrame(byte[] frameHeader, byte[] data) {
		// Text encoding $xx
		// MIME type <text string> $00
		// Picture type $xx
		// Description <text string according to encoding> $00 (00)
		// Picture data <binary data>
		super(frameHeader);

		try (ByteArrayInputStream input = new ByteArrayInputStream(data)) {
			encoding = (byte) input.read();

			byte[] mimeTypeArray = ByteOpperations.readString(input);
			mimeType = new String(mimeTypeArray, ENCODINGS[encoding]);

			picturesType = (byte) input.read();

			byte[] descriptionArray = ByteOpperations.readString(input);
			description = new String(descriptionArray, ENCODINGS[encoding]);

			// Text consist of the very last bytes of the content (after $00
			// byte).
			int imageOffset = 1 + mimeType.length() + 1 + description.length()
					+ 1;
			image = Arrays.copyOfRange(data, imageOffset, data.length);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public PictureFrame(byte pictureType, File image) {
		super(MP3TagFrameTypes.ATTACHED_PICTURE);

		// Currently only cover (front) is supported.
		setImageFromFile(image);
	}

	@XmlElement(name = "image")
	@XmlJavaTypeAdapter(ByteArrayMarshallerAdapter.class)
	public byte[] getImage() {
		return this.image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public void setImageFromFile(File imageFile) {
		encoding = NUL_CHAR;
		mimeType = "image/" + getExtension(imageFile);
		picturesType = COVER_FRONT_INDEX;
		image = ImageOpperations.imageToByteArray(imageFile);
	}

	public String getPictureDescription() {
		return PICTURE_TYPES[(int) picturesType];
	}

	@XmlElement(name = "encoding")
	public byte getEncoding() {
		return encoding;
	}

	public void setEncoding(byte encoding) {
		this.encoding = encoding;
	}

	@XmlElement(name = "mimetype")
	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	@XmlElement(name = "picturetype")
	public byte getPicturesType() {
		return picturesType;
	}

	public void setPicturesType(byte picturesType) {
		this.picturesType = picturesType;
	}

	@XmlElement(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return getType() + ": " + "PictureFrame" + " with " + encoding
				+ " encryption.\nmimeType: " + mimeType + "\npictureType: "
				+ getPictureDescription() + "\ndescription: " + description;
	}

	@Override
	public Object clone() {
		return new PictureFrame(getHeaderBytes(), getContentBytes());
	}

	@Override
	public byte[] getContentBytes() {
		// Text encoding $xx
		// MIME type <text string> $00
		// Picture type $xx
		// Description <text string according to encoding> $00 (00)
		// Picture data <binary data>

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		try {
			outputStream.write(encoding);
			outputStream.write(mimeType.getBytes());
			outputStream.write(NUL_CHAR);
			outputStream.write(picturesType);
			outputStream.write(description.getBytes(ENCODINGS[encoding]));
			outputStream.write(NUL_CHAR);
			outputStream.write(image);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return outputStream.toByteArray();
	}

	private String getExtension(File file) {
		return file.getName().substring(file.getName().lastIndexOf('.') + 1);
	}
}
