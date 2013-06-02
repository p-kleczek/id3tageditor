package id3editor.data.tag;

/**
 * The <code>URLFrame</code> class represents a Wnnn frame.
 * 
 * @author Pawel, Florian, Sebastian (Gruppe 4)
 */
public class URLFrame extends MP3TagFrame {

	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public URLFrame() {
	}

	public URLFrame(byte[] frameHeader, byte[] content) {
		super(frameHeader);
		this.url = new String(content);
	}

	@Override
	public String toString() {
		return getType() + ": " + url;
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
