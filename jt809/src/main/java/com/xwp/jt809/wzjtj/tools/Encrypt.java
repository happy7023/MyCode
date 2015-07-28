package com.xwp.jt809.wzjtj.tools;

public class Encrypt {

	public static byte[] en(long key, byte[] buff, int size) {
		int M1 = 0xFAFAFAFA;
		int IA1 = 0xF7F7F7F7;
		int IC1 = 0xF5F5F5F5;
		int idx = 0;
		if (key == 0) {
			key = 1;
		}
		while (idx < size) {
			key = IA1 * (key % M1) + IC1;
			long abc = (key >> 20) & 0xFF;
			long nabc = abc >= 0 ? abc : 256 + abc;
			int n = idx++;
			long b = buff[n] ^= nabc;
		}
		return (buff);
	}

}
