package com.xwp.jt809.mina.server.hostLink;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.xwp.jt809.mina.codec.ExplainMessage;
import com.xwp.jt809.mina.model.MyMap;
import com.xwp.jt809.tools.ChangeType;
import com.xwp.jt809.tools.DataExplain;
import com.xwp.jt809.tools.Encrypt;
import com.xwp.jt809.tools.LogicMark;
import com.xwp.jt809.tools.TabCode;
import com.xwp.jt809.tools.Transform;

public class JT809MessageProtocalDecoder extends CumulativeProtocolDecoder {

	private DataExplain dataExplain = new DataExplain();
	private final Charset charset;
	private final AttributeKey CONTEXT = new AttributeKey(getClass(),"context");

	public JT809MessageProtocalDecoder(Charset charset) {
		this.charset = charset;
	}
	
	@Override
	protected boolean doDecode(IoSession session, IoBuffer in,ProtocolDecoderOutput out) throws Exception {		
		Context ctx = getContext(session);
		IoBuffer buf = ctx.innerBuffer;
		while(in.hasRemaining()){
			byte b = in.get();
			buf.put(b);
			if(ctx.isMark() && b == 0x5d){
				buf.flip();
				byte[] message = new byte[buf.remaining()];
				buf.get(message);
				List<Byte> blist = array2List(message);
				blist = Transform.unchange(blist);
				if (blist.size() == 72) { // 从链路登录请求
					out.write(ExplainMessage.explainLogin(blist));
				} else if (blist.size() == 27 || blist.size() == 26) { // 解析应答
					out.write(ExplainMessage.explainResponse(blist));
				} else {
					byte[] megLength = new byte[4];
					byte[] megGnsscenterid = new byte[4];
					byte[] encryptKey = new byte[4];
					int k;
					int gnsscenterid;
					int key;
					byte[] msgidByte = new byte[2];
					Byte encrypt;
					int MSGID;
					for (int j = 0; j < 4; j++) {
						megLength[j] = blist.get(j + 1);
						megGnsscenterid[j] = blist.get(j + 11);
						encryptKey[j] = blist.get(j + 19);
					}
					k = ChangeType.bytesTo4Int(megLength, 0);
					gnsscenterid = ChangeType.bytesTo4Int(
							megGnsscenterid, 0);
					key = ChangeType.bytesTo4Int(encryptKey, 0);
					// 数据长度和接入码都正确，才进行处理
					if (blist.size() == k
							&& gnsscenterid == LogicMark.GNSSCENTERID) {
						msgidByte[0] = blist.get(9);
						msgidByte[1] = blist.get(10);
						encrypt = blist.get(18);
						MSGID = ChangeType.bytesTo2Int(msgidByte, 0);
						blist = new ArrayList<Byte>(blist.subList(23,
								blist.size() - 3));
						if (encrypt.intValue() == 1) {
							byte[] tmp = new byte[blist.size()];
							for (int j = 0; j < blist.size(); j++) {
								tmp[j] = blist.get(j);
							}
							tmp = Encrypt.en(key, tmp, tmp.length);
							blist.clear();
							for (int j = 0; j < tmp.length; j++) {
								blist.add(tmp[j]);
							}
						}
						try{
							switch (MSGID) {
								case TabCode.UP_DISCONNECT_REQ: { // 主
									byte[] userb = { blist.get(0),blist.get(1), blist.get(2),blist.get(3) };
									String userId = new String(userb);
									byte[] passwardb = new byte[8];
									for (int j = 0; j < 8; j++) {
										passwardb[j] = blist.get(j + 4);
									}
									int password = ChangeType.bytesTo4Int(passwardb, 0);
									out.write((new MyMap(TabCode.UP_DISCONNECT_RSP,(byte) -1)));
									System.out.println("客户: " + userId+ " 密码: " + password + " 主动断开连接.");
									break;
								}
								case TabCode.UP_DISCONNECT_INFORM: { // 从
									Byte errorCode = blist.get(0);
									if (errorCode.intValue() == 0x00) {
										System.out.println("主链路断开，错误代码:" + 0x00);
									} else {
										System.out.println("主链路断开,其他原因,错误代码:" + 0x01);
									}
									break;
								}
								case TabCode.UP_CLOSELINK_INFORM: { // 从
									Byte reasonCode = blist.get(0);
									if (reasonCode == 0x00) {
										System.out.println("链路关闭，原因:网卡重启,错误代码:" + 0x00);
									} else {
										System.out.println("链路关闭，原因:其他,错误代码:" + 0x00);
									}
									break;
								}
								case TabCode.UP_EXG_MSG: {
									out.write(dataExplain.explainEXG(blist));
									break;
								}
								case TabCode.UP_PLATFORM_MSG: {
									dataExplain.explainPLATFORM(blist);
									break;
								}
								case TabCode.UP_WARN_MSG: {
									dataExplain.explainWARN(blist);
									break;
								}
								case TabCode.UP_CTRL_MSG: {
									dataExplain.explainCTRL(blist);
									break;
								}
								case TabCode.UP_BASE_MSG: {
									dataExplain.explainBASE(blist);
									break;
								}
								default: {
									System.out.println("数据未被正常解析,业务代码:" + MSGID);
									break;
								}
							}
						}catch (Exception e){
							return true;
						}
					}
				}
				ctx.reset();
				return true;
			}else if(b == 0x5b){
				ctx.setMark(true);
			}
		}
		return true;
	}

	private List<Byte> array2List(byte[] bytes) {
		List<Byte> listByte = new ArrayList<Byte>();
		if (null != bytes) {
			for (Byte b : bytes) {
				listByte.add(b);
			}
		}
		return listByte;
	}
	
	private Context getContext(IoSession session) {
		Context context = (Context) session.getAttribute(CONTEXT);
		if (context == null) {
		context = new Context();
		session.setAttribute(CONTEXT, context);
		}
		return context;
	}
	private class Context {
		private final IoBuffer innerBuffer;
		private boolean mark = false;
		
		public Context() {
			innerBuffer = IoBuffer.allocate(100).setAutoExpand(true);
		}
		
		public boolean isMark() {
			return mark;
		}
		
		public void setMark(boolean mark) {
			this.mark = mark;
		}

		public void reset() {
			this.innerBuffer.clear();
			this.mark = false;
		}
	}	
}
