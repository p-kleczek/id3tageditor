package xml;

/**
 * @author Pawel, Florian, Sebastian (Gruppe 4)
 */

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import data.MP3Object;

/**
 * 
 * @author Spunky
 */
public class XMLReader {

	public static MP3Object readFolderCache(File folder) {
		MP3Object result = null;

		File cache = new File(folder.getAbsolutePath() + File.separator
				+ "cache.xml");

		if (cache.exists() && cache.lastModified() > folder.lastModified()) {
			JAXBContext jc;
			Unmarshaller um;
			try {

				jc = JAXBContext.newInstance(MP3Object.class);
			} catch (Exception e) {
				System.err.println("Error in readFolderCache - jaxbcontent");
				return null;
			}
			try {
				um = jc.createUnmarshaller();

			} catch (Exception e) {
				System.err
						.println("Error in readFolderCache - createunmarshaller");
				return null;
			}
			try {
				result = (MP3Object) um.unmarshal(cache);
			} catch (Exception e) {
				System.err.println("Error in readFolderCache - unmarshal");
				e.printStackTrace();
				return null;
			}
		}

		return result;
	}
}
