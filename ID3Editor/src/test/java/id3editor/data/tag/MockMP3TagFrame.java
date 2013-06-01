package id3editor.data.tag;

public class MockMP3TagFrame extends MP3TagFrame {

	public MockMP3TagFrame(byte[] frameHeader) {
		super(frameHeader);
	}

	public MockMP3TagFrame(String frameType) {
		super(frameType);
	}

	@Override
	byte[] getContentBytes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return null;
	}

}
