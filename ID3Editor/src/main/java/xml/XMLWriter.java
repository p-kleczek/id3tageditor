package xml;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import data.MP3Folder;
import data.MP3Object;

/**
 * 
 * @author Pawel, Florian, Sebastian (Gruppe 4)
 */
public class XMLWriter {

	public static void writeFolderCache(MP3Folder folder) {
		if (folder instanceof MP3Folder && folder != null
				&& folder.getFilePath().exists()) {

			File target = new File(folder.getFilePath() + File.separator
					+ "cache.xml");

			JAXBContext jc;
			try {
				jc = JAXBContext.newInstance(MP3Object.class);
				Marshaller m = jc.createMarshaller();
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				m.marshal(folder, target);
			} catch (JAXBException e) {
				System.err.println("WriteXMLCacheError");
				e.printStackTrace();
			}
		} else {
			System.err.println("UnvalidFolderForXMLCacheError");
		}
	}
}
