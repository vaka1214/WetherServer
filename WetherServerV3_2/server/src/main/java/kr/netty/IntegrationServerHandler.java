package kr.netty;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.BlockingQueue;

import java.nio.charset.Charset;
import java.util.Scanner;

public class IntegrationServerHandler extends ChannelInboundHandlerAdapter
{
	private BlockingQueue<TaskAndCtx> qIn;
	IntegrationServerHandler(BlockingQueue<TaskAndCtx> qIn)
	{
		this.qIn = qIn;
	}
	
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
	{
        ByteBuf in = (ByteBuf) msg;
		String str;
		String name;
		try
		{
			str = in.toString(Charset.forName("utf-8"));
			Scanner sc = new Scanner(str);
			name = sc.nextLine();
			name=name.replaceAll(" ","");
			if(!name.equals("quit")){
				System.out.println("Received a temperature request for the city: "+name);
			}
			synchronized(qIn)
			{
				qIn.add(new TaskAndCtx(new Task(0,name),ctx));
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