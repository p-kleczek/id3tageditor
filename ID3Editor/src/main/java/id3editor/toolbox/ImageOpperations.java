package id3editor.toolbox;

import id3editor.mvc.Application;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


/**
 * The <code>ImageOperations</code> class provides methods for loading and
 * manipulating images in a convenient way (eg. without nasty try-catch blocks).
 * 
 * @author Pawel, Florian, Sebastian (Gruppe 4)
 * 
 */
public class ImageOpperations {

	/**
	 * Reads image from the given file as a byte array.
	 * 
	 * @param imageFile
	 *            file to be read
	 * @return image as a byte array
	 */
	public static byte[] imageToByteArray(File imageFile) {
		FileInputStream fileInputStream;
		byte[] data = new byte[(int) imageFile.length()];

		try {
			fileInputStream = new FileInputStream(imageFile);
			fileInputStream.read(data);
			fileInputStream.close();
		} catch (Exception e) {
			System.err.println("Failure in imageToArray()");
			data = new byte[0];
		}
		return data;
	}

	/**
	 * Converts byte array into an ImageIcon.
	 * 
	 * @param imageArray
	 *            input data
	 * @return image icon
	 */
	public static ImageIcon byteArrayToIcon(byte[] imageArray) {
		return new ImageIcon(imageArray);
	}

	/**
	 * Converts byte array into an ImageIcon of the specific size.
	 * 
	 * @param imageArray
	 *            input data
	 * @param width
	 *            icon width
	 * @param height
	 *            icon height
	 * @return scaled image icon or "no cover" icon if errors occured
	 */
	public static ImageIcon ByteArrayToIcon(byte[] imageArray, int width,
			int height) {

		// In case of errors return "no cover" icon.
		if (imageArray.length == 0 || width <= 0 || height <= 0)
			return Application.getApplication().noCoverImageIcon;

		try {
			BufferedImage image = ImageIO.read(new ByteArrayInputStream(
					imageArray));

			return bufferedImageToImageIcon(image, width, height);
		} catch (Exception e) {
			return Application.getApplication().noCoverImageIcon;
		}
	}

	/**
	 * Reads image from the given file.
	 * 
	 * @param imageFile
	 *            file to be read
	 * @return image read from the file or empty image if an error occurred
	 */
	public static BufferedImage imageToBufferedImage(File imageFile) {
		try {
			return ImageIO.read(imageFile);
		} catch (IOException e) {
			return new BufferedImage(0, 0, BufferedImage.TYPE_3BYTE_BGR);
		}
	}

	/**
	 * Reads content of the image file and converts it into an ImageIcon.
	 * 
	 * @param imageFile
	 *            image file
	 * @return image icon
	 */
	public static ImageIcon imageToImageIcon(File imageFile) {
		try {
			return new ImageIcon(ImageIO.read(imageFile));
		} catch (IOException e) {
			return new ImageIcon();
		}
	}

	/**
	 * Reads content of the image file, scales it and converts into a as
	 * ImageIcon.
	 * 
	 * @param imageFile
	 *            image file
	 * @param width
	 *            icon width
	 * @param height
	 *            icon height
	 * @return scaled image icon
	 */
	public static ImageIcon imageToImageIcon(File imageFile, int width,
			int height) {
		return new ImageIcon(imageToBufferedImage(imageFile).getScaledInstance(
				width, height, Image.SCALE_DEFAULT));
	}

	/**
	 * Returns image icon created from the given image.
	 * 
	 * @param bufImg
	 *            image to be converted to the icon
	 * @param width
	 *            icon width
	 * @param height
	 *            icon height
	 * @return icon from the image
	 */
	public static ImageIcon bufferedImageToImageIcon(BufferedImage bufImg,
			int width, int height) {
		try {
			return new ImageIcon(bufImg.getScaledInstance(width, width,
					Image.SCALE_DEFAULT));
		} catch (Exception e) {
			System.err.println("Error in bufferedImageToImageIcon");
			return new ImageIcon();
		}

	}
}
