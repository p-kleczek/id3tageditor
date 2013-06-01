package data.tag;

/**
 * 
 * @author Pawel, Florian, Sebastian (Gruppe 4)
 */
public class URLFrame extends MP3TagFrame {

	private String url;

	public URLFrame() {
	}

	public URLFrame(byte[] frameHeader, byte[] content) {
		super(frameHeader);
		this.url = new String(content);
	}

	@Override
	public String toString() {
		return type + ": " + url;
	}

	@Override
	public Object clone() {
		return new URLFrame(getHeaderBytes(), getContentBytes());
	}

	@Override
	public byte[] getContentBytes() {
		// URL <text string>
		return url.getBytes();
	}
}
