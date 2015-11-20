package com.xwp.jt809.tools;

/**
 * 逻辑标识，用于服务器与客户端之间应答
 * @author 伟平
 *
 */
public class LogicMark {
	/**成功*/
	public static final byte SUCCESS  = 0x00;
	/**IP错误*/
	public static final byte IP_ERROR  = 0x01;
	/**接入码错误*/
	public static final byte  GNSSCENTERID_ERROR = 0x02;
	/**用户未注册*/
	public static final byte USERNAME_ERROR  = 0x03;
	/**密码错误*/
	public static final byte PASSWORD_ERROR  = 0x04;
	/**忙，请等待*/
	public static final byte BUSY  = 0x05;
	/**其他错误*/
	public static final byte OTHER  = 0x06;
	/**接入码*/
	public static final int GNSSCENTERID  = 3303001;
	/**校验码*/
	public static final int jym = 0x1111;
	/**用户名*/
	public static final int USERNAME  = 3303001;
	/**密码*/
	public static final String PASSWORD  = "123456";
	
}
