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

public class IntegrationClientHandler extends ChannelInboundHandlerAdapter
{
	static String s;
	int i;
    @Override
    public void channelActive(final ChannelHandlerContext ctx)
	{
		i=0;
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter name of the city:\nOr enter exit to close program:\n");
		s = scan.nextLine();
		if(s.equals("exit")){

			System.out.println("Shutdown.....");
			i++;
		}
		final ByteBuf out = Unpooled.wrappedBuffer(s.getBytes());
		ctx.writeAndFlush(out);
	}
	
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
	{
		ByteBuf in = (ByteBuf) msg;
		String str;
		try
		{
			if (i==0) {
				str = in.toString(Charset.forName("utf-8"));
				System.out.println("Info about temperature: " + str);
			}
		}
		finally
		{
			ReferenceCountUtil.release(msg);
		}
	}
	
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	{
        cause.printStackTrace();
        ctx.close();
    }
}
