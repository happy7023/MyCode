package com.xwp.jt809.mina.server.fromLink;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.xwp.jt809.mina.codec.ExplainMessage;
import com.xwp.jt809.tools.Transform;

public class ServerFromProtocalDecode  extends CumulativeProtocolDecoder {

	private Charset charset;
	private final AttributeKey CONTEXT = new AttributeKey(getClass(),"context");
	
	public ServerFromProtocalDecode(Charset charset){
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
				if(blist.size() == 27 || blist.size() == 26){
					out.write(ExplainMessage.explainResponse(blist));
				} else {
					System.out.println("未识别的数据长度："+blist.size());
				}
				ctx.reset();
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
}
