package id3editor.data.tag;

import id3editor.xml.ByteArrayMarshallerAdapter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 * @author Pawel, Florian, Sebastian (Gruppe 4)
 */
public class DefaultFrame extends MP3TagFrame implements Cloneable {

	private byte[] data;

	public DefaultFrame() {
	}

	public DefaultFrame(byte[] frameHeader, byte[] data) {
		super(frameHeader);
		this.data = data;
	}

	@XmlElement(name = "data")
	@XmlJavaTypeAdapter(ByteArrayMarshallerAdapter.class)
	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	@Override
	public byte[] getContentBytes() {
		return data;
	}

	@Override
	public String toString() {
		return "Unknow: " + getType();
	}

	@Override
	public Object clone() {
		return new DefaultFrame(getHeaderBytes(), getContentBytes());
	}

}
