package xml;

/**
 * @author Pawel, Florian, Sebastian (Gruppe 4)
 */

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.codec.binary.Base64;

public class ByteArrayMarshallerAdapter extends XmlAdapter<String, byte[]> {

	@Override
	public byte[] unmarshal(String value) {
		return Base64.decodeBase64(value);
	}

	@Override
	public String marshal(byte[] data) {
		return Base64.encodeBase64String(data);
	}

}