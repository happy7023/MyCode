package com.xwp.jt809.mina.client.hostLink;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.xwp.jt809.mina.client.fromLink.ClientFromLink;
import com.xwp.jt809.mina.codec.ExplainMessage;
import com.xwp.jt809.tools.ChangeType;
import com.xwp.jt809.tools.Encrypt;
import com.xwp.jt809.tools.LogicMark;
import com.xwp.jt809.tools.TabCode;
import com.xwp.jt809.tools.Transform;

public class ClientProtocalDecode extends CumulativeProtocolDecoder {

	private Charset charset;
	private final AttributeKey CONTEXT = new AttributeKey(getClass(),"context");
	
	public ClientProtocalDecode(Charset charset){
		this.setCharset(charset);
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
				if(blist.size() == 31){
					if(explainLoginRsp(blist)){
						System.out.println("主链路建立完成"+"  "+session.getId());
						Thread t = new Thread(new ClientFromLink());
						t.start();
					}
				}else if(blist.size() == 26){
					out.write(ExplainMessage.explainResponse(blist));
				}				ctx.reset();
				return true;
			}else if(b == 0x5b){
				ctx.setMark(true);
			}
		}
		return true;
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
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
	/**
	 * 解析主链路应答
	 * @param blist
	 * @return
	 * @throws IOException
	 */
	private static boolean explainLoginRsp(List<Byte> blist) throws IOException{
		byte[] megLength = new byte[4];
		byte[] megGnsscenterid = new byte[4];
		
		byte[] encryptKey = new byte[4];
		// 反转义
		blist = Transform.unchange(blist);
		for (int i = 0; i < 4; i++) {
			megLength[i] = blist.get(i + 1);
			megGnsscenterid[i] = blist.get(i + 11);
			encryptKey[i] = blist.get(i + 19);
		}
		int k = ChangeType.bytesTo4Int(megLength, 0);
		int gnsscenterid = ChangeType.bytesTo4Int(megGnsscenterid, 0);
		int key = ChangeType.bytesTo4Int(encryptKey, 0);
		// 数据长度和接入码都正确，才进行处理
		if (blist.size() == k && gnsscenterid == LogicMark.GNSSCENTERID) {
			byte[] msgidByte = { blist.get(9), blist.get(10) };
			Byte encrypt = blist.get(18);
			int MSGID = ChangeType.bytesTo2Int(msgidByte, 0);
			if (MSGID == TabCode.UP_CONNECT_RSP) {
				blist = blist.subList(23, blist.size()-3);
				if(encrypt.intValue() == 1){
					byte[] tmp = new byte[blist.size()];
					for(int i=0;i<blist.size();i++){
						tmp[i] = blist.get(i);
					}
					tmp = Encrypt.en(key, tmp, tmp.length);
					blist.clear();
					for(int i=0;i<tmp.length;i++){
						blist.add(tmp[i]);
					}
				}
				byte b = blist.get(0);
//				byte[] verifyBytes = {blist.get(1),blist.get(2),blist.get(3),blist.get(4)};
				if(b == 0x00){
//					verify = ChangeType.bytesTo4Int(verifyBytes, 0);
					return true;
				}
			}
		}
		System.out.println("未知别的消息.数据长度:"+k);
		return false;
	}
}
