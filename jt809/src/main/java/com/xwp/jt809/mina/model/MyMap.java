package com.xwp.jt809.mina.model;

public class MyMap {

	private int name;
	
	private byte value;
	
	public MyMap(int name,byte value){
		this.name = name;
		this.value = value;
	}

	public int getName() {
		return name;
	}

	public void setName(int name) {
		this.name = name;
	}

	public byte getValue() {
		return value;
	}

	public void setValue(byte value) {
		this.value = value;
	}
	
}
