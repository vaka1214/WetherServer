package kr.netty;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.BlockingQueue;

import java.nio.charset.Charset;
import java.util.Scanner;

public class ServerHandler extends ChannelInboundHandlerAdapter
{
	private BlockingQueue<TaskAndCtx> qIn;
	ServerHandler(BlockingQueue<TaskAndCtx> qIn)
	{
		this.qIn = qIn;
	}


	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
	{
        ByteBuf in = (ByteBuf) msg;
		String str;
		str = in.toString(Charset.forName("utf-8"));
		String name = serverString(str);
		if(!name.equals("exit")){
			System.out.println("Received a temperature request for the city: "+name);
		}
		synchronized(qIn)
		{
			qIn.add(new TaskAndCtx(new Task(0,name),ctx));
		}

		ReferenceCountUtil.release(msg);

    }

  	public String serverString(String str){
		String name;
		Scanner sc = new Scanner(str);
		name = sc.nextLine();
		name=name.replaceAll(" ","");
		String capitalized = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
		return capitalized;
	}


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	{
        cause.printStackTrace();
        ctx.close();
    }
}
