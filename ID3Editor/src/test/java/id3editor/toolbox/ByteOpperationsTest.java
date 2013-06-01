package id3editor.toolbox;

import static org.junit.Assert.*;

import org.junit.Test;

public class ByteOpperationsTest {

	@Test
	public void testConvertSynchsafeByteToInt() {
		byte[] bytes = new byte[]{(byte) 0b0000001, (byte) 0b01111111};
		assertEquals(255, ByteOpperations.convertSynchsafeByteToInt(bytes));
	}

	@Test
	public void testConvertByteToInt() {
		byte[] bytes = new byte[]{0x00, 0x00, (byte) 0b0000001, (byte) 0b11111111};
		assertEquals(511, ByteOpperations.convertByteToInt(bytes));
	}

	@Test
	public void testConvertIntToByte() {
		byte[] bytes = new byte[]{0x00, 0x00, (byte) 0b0000001, (byte) 0b11111111};
		assertArrayEquals(bytes, ByteOpperations.convertIntToByte(511));
	}

	@Test
	public void testConvertSynchsafeIntToByte() {
		byte[] bytes = new byte[]{0x00, 0x00, (byte) 0b0000001, (byte) 0b01111111};
		assertArrayEquals(bytes, ByteOpperations.convertSynchsafeIntToByte(255));
	}

}
