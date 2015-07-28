package com.xwp.jt809.wzjtj.tools;

import java.util.List;

public class Transform {
	/**
	 * 反转义，转换成目标byte流
	 * @param blist
	 * @return
	 */
	public static List<Byte> unchange(List<Byte> blist){
		for(int i=1;i<blist.size()-1;i++){
			if(blist.get(i)==0x5a){
				if(0x02==blist.get(i+1)){
					blist.remove(i+1);
				}else if(0x01==blist.get(i+1)){
					blist.remove(i+1);
					blist.set(i, (byte) 0x5b);
				}
			}else if(blist.get(i)==0x5e){
				if(0x01==blist.get(i+1)){
					blist.remove(i+1);
					blist.set(i, (byte) 0x5d);
				}else if(0x02==blist.get(i+1)){
					blist.remove(i+1);
				}
			}
		}
		return blist;
	}
	/**
	 * 转义，转换成目标byte流
	 * @param blist
	 * @return
	 */
	public static List<Byte> change(List<Byte> blist){
		for(int i=1;i<blist.size()-1;i++){
			if(blist.get(i).intValue()==0x5b){
				blist.set(i, (byte) 0x5a);
				blist.add(i+1, (byte) 0x01);
				i++;
			}else if(blist.get(i).intValue()==0x5a){
				blist.set(i, (byte) 0x5a);
				blist.add(i+1, (byte) 0x02);
				i++;
			}else if(blist.get(i).intValue()==0x5d){
				blist.set(i, (byte) 0x5e);
				blist.add(i+1, (byte) 0x01);
				i++;			
			}else if(blist.get(i).intValue()==0x5e){
				blist.set(i, (byte) 0x5e);
				blist.add(i+1, (byte) 0x02);
				i++;
			}
		}
		return blist;
	}
}
