package com.xwp.jt809.wzjtj;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.xwp.jt809.wzjtj.tools.ChangeType;
import com.xwp.jt809.wzjtj.tools.LogicMark;
import com.xwp.jt809.wzjtj.tools.PackageMessage;
import com.xwp.jt809.wzjtj.tools.TabCode;
import com.xwp.jt809.wzjtj.tools.Tools;

public class test {

	public static void main(String[] args) {
		byte[] b = {0, 0, 17, 17};
		System.out.println(ChangeType.bytesTo4Int(b, 0));
		System.out.println(0x1111);
		System.out.println(0x5d);
	}

}
