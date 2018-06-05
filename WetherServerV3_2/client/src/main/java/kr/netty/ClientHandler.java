package kr.netty;

import java.util.Scanner;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;
import io.netty.buffer.Unpooled;

import io.netty.util.concurrent.BlockingOperationException;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.*;

public class ClientHandler extends ChannelInboundHandlerAdapter
{
	static String s = "";
    @Override
    public void channelActive(final ChannelHandlerContext ctx)
	{	String s1;
		s1 = enterStr();
		final ByteBuf out = Unpooled.wrappedBuffer(s1.getBytes());
		ctx.writeAndFlush(out);
	}

	public String enterStr() {
		if (!s.equals("test")) {
			Scanner scan = new Scanner(System.in);
			System.out.print("Enter name of the city\n");
			s = scan.nextLine();
			if (s.equals("exit")) {
				System.out.println("Shutdown.....");
			}
		} else {
			s = "Moscow";
		}
		return s;
    }
	
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
	{
		ByteBuf in = (ByteBuf) msg;
		String str;
		if(!s.equals("exit")) {
			str = in.toString(Charset.forName("utf-8"));
			System.out.println("Info about temperature: " + str);

		} else {
			System.out.println("..........");
		}

			ReferenceCountUtil.release(msg);
	}
	
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	{
        cause.printStackTrace();
        ctx.close();
    }
}
