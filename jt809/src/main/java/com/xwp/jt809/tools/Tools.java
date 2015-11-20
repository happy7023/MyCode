package com.xwp.jt809.tools;

import java.util.List;

public class Tools {

	/**
	 * 拼接目标list<Byte>
	 * @param byte[]
	 * @param b
	 * @return
	 */
	public static List<Byte> combine4(List<Byte> list,byte[] b){
		if(b.length >4){
			return null;
		}else {
			for(int i = 0;i<b.length;i++){
				list.add(b[i]);
			}
			if(b.length < 4){
				for(int i=0;i<(4-b.length);i++){
					list.add((byte) 0x00);
				}
			}
			return list;
		}
	}
	
	/**
	 * 拼接目标list<Byte>
	 * @param byte[]
	 * @param b
	 * @return
	 */
	public static List<Byte> combine8(List<Byte> list,byte[] b){
		if(b.length > 8){
			return null;
		}else {
			for(int i = 0;i<b.length;i++){
				list.add(b[i]);
			}
			if(b.length < 8){
				for(int i=0;i<(8-b.length);i++){
					list.add((byte) 0x00);
				}
			}
			return list;
		}
	}
	
	/**
	 * 拼接目标list<Byte>
	 * @param byte[]
	 * @param b
	 * @return
	 */
	public static List<Byte> combine32(List<Byte> list,byte[] b){
		if(b.length >32){
			return null;
		}else {
			for(int i = 0;i<b.length;i++){
				list.add(b[i]);
			}
			if(b.length < 32){
				for(int i=0;i<(32-b.length);i++){
					list.add((byte) 0x00);
				}
			}
			return list;
		}
	}
	
	/**
	 * 拼接目标list<Byte>
	 * @param list
	 * @param b
	 * @return
	 */
	public static List<Byte> combine(List<Byte> list,List<Byte> b,int n){
		if(b.size() > n){
			return null;
		}else{
			for(int i = 0;i<b.size();i++){
				list.add(b.get(i));
			}
			return list;
		}
	}
	public static List<Byte> combine(List<Byte> list,byte[] bs,int n){
		if(bs.length > n){
			return null;
		}else{
			for(int i = 0;i<bs.length;i++){
				list.add(bs[i]);
			}
			for(int i=0;i<(n-bs.length);i++){
				list.add((byte) 0x00);
			}
			return list;
		}
	}
	
	/**
	 * 清除数据前面加0x00
	 * @param srcb
	 * @return
	 */
	public static byte[] cleanBefore0x00(byte[] srcb){
		byte[] target = null;
		boolean pd = true;
		int index = 0;
		for(int i=0;i<srcb.length;i++){
			if(pd){
				if(srcb[i] != 0x00){
					target = new byte[srcb.length-i];
					target[index] = srcb[i]; 
					index++;
					pd = false;
				}
			}else{
				target[index] = srcb[i];
				index++;
			}
		}
		return target;
	}
	/**
	 * 清除数据后面加0x00
	 * @param srcb
	 * @return
	 */
	public static byte[] cleanAfter0x00(byte[] srcb){
		byte[] target = null;
		int index = srcb.length-1;
		for(int i=index;i>0;i--){
			if(srcb[i] != 0x00){
				target = new byte[i+1];
				for(int j=0;j<=i;j++){
					target[j] = srcb[j];
				}
				return target;
			}
		}
		return target;
	}
}
