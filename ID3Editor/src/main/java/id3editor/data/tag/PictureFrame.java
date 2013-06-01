package id3editor.data.tag;

/**
 * 
 * @author Pawel, Florian, Sebastian (Gruppe 4)
 */

import id3editor.toolbox.ImageOpperations;
import id3editor.xml.ByteArrayMarshallerAdapter;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author Gruppe4
 */
public class PictureFrame extends MP3TagFrame {

	public static int COVER_FRONT = 3;

	public static final String[] PIC_TYPES = { "Other",
			"32x32 pixels file icon", "Other file icon", "Cover (front)",
			"Cover (back)", "Leaflet page", "Media (e.g. lable side of CD)",
			"Lead artist/lead performer/soloist", "Artist/performer",
			"Conductor", "Band/Orchestra", "Composer", "Lyricist/text writer",
			"Recording Location", "During recording", "During performance",
			"Movie/video screen capture", "A bright coloured fish",
			"Illustration", "Band/artist logotype", "Publisher/Studio logotype" };

	private byte encoding = 0;
	private String mimeType = "";
	private byte picturesType = 0;
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
		try {
			int offset = 0;

			this.encoding = data[0];
			offset++;

			byte[] mimeTypeArray = new byte[0];
			while (data[offset] != 0x00 && offset < data.length) {
				byte[] tmp = new byte[mimeTypeArray.length + 1];
				System
						.arraycopy(mimeTypeArray, 0, tmp, 0,
								mimeTypeArray.length);
				tmp[mimeTypeArray.length] = data[offset];
				mimeTypeArray = tmp;
				offset++;
			}
			this.mimeType = new String(mimeTypeArray, ENC_TYPES.get(encoding));
			offset++;

			this.picturesType = data[offset];
			offset++;

			byte[] descriptionTypeArray = new byte[0];
			while (data[offset] != 0x00 && offset < data.length) {
				byte[] tmp = new byte[descriptionTypeArray.length + 1];
				System.arraycopy(descriptionTypeArray, 0, tmp, 0,
						descriptionTypeArray.length);
				tmp[descriptionTypeArray.length] = data[offset];
				descriptionTypeArray = tmp;
				offset++;
			}
			this.description = new String(descriptionTypeArray,
					ENC_TYPES.get(encoding));

			offset++;

			this.image = new byte[data.length - offset];

			System.arraycopy(data, offset, image, 0, image.length);

		} catch (UnsupportedEncodingException ex) {
			Logger.getLogger(PictureFrame.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	public PictureFrame(byte pictureType, File image) {
		super("APIC");
		this.picturesType = pictureType;
		setImageFromFile(image);
		String extension = image.getName().substring(
				image.getName().lastIndexOf('.') + 1);

		mimeType = "image/" + extension;
		picturesType = 3; // = Cover (front)
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

		encoding = 0x00;

		String extension = imageFile.getName().substring(
				imageFile.getName().lastIndexOf('.') + 1);
		mimeType = "image/" + extension;

		picturesType = 3; // = Cover (front)
		description = "";

		this.image = ImageOpperations.imageToByteArray(imageFile);
	}

	public String getPictureType() {
		return PIC_TYPES[(int) picturesType];
	}

	@XmlElement(name = "encoding")
	public byte getEncoding() {
		return encoding;
	}

	public void setEncoding(byte i) {
		this.encoding = i;
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
		return type + ": " + "PictureFrame" + " with " + encoding
				+ " encryption.\nmimeType: " + mimeType + "\npictureType: "
				+ PIC_TYPES[(int) picturesType] + "\ndescription: "
				+ description;
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

		byte[] descriptionBytes = new byte[0];
		try {
			descriptionBytes = description.getBytes(ENC_TYPES.get(encoding));
		} catch (Exception e) {
			System.err.println("Error in TextFrame.getContentBytes()");
		}

		byte[] result = new byte[4 + mimeType.getBytes().length
				+ descriptionBytes.length + image.length];

		int offset = 0;

		result[offset] = encoding;
		offset++;

		System.arraycopy(mimeType.getBytes(), 0, result, offset, mimeType
				.getBytes().length);
		offset += mimeType.getBytes().length + 1;

		result[offset] = picturesType;
		offset++;

		System.arraycopy(descriptionBytes, 0, result, offset,
				descriptionBytes.length);
		offset += descriptionBytes.length + 1;

		System.arraycopy(image, 0, result, offset, image.length);

		return result;
	}

}
