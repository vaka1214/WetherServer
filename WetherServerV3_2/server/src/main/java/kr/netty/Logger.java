package kr.netty;

import java.util.ArrayDeque;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.*;

public class Logger implements Runnable
{
	private ArrayDeque<TaskAndCtx> qOut;
	
	private boolean isThreadActive;
	
	public Logger(ArrayDeque<TaskAndCtx> qOut)
	{
		this.qOut=qOut;
		this.isThreadActive = true;
	}
	
	@Override
	public void run()
	{
		while (isThreadActive)
		{
			TaskAndCtx curTask = null;
			synchronized(qOut)
			{
				curTask = qOut.poll();
			}
			if (curTask!=null)
			{
				ChannelHandlerContext ctx = curTask.ctx;
				if(!curTask.task.city.equals("exit")){
					System.out.println("Sending current weather on request: "+curTask.task.temperature);
				}
				ByteBuf out = Unpooled.wrappedBuffer(curTask.task.temperature.getBytes());
				final ChannelFuture f = ctx.writeAndFlush(out);
				f.addListener(new ChannelFutureListener()
				{
					@Override
					public void operationComplete(ChannelFuture future)
					{
						assert f == future;
						ctx.close();
					}
				});
			}
			else
				try{Thread.sleep(100);}
				catch (InterruptedException e)
				{/*e.printStackTrace();*/}
		}
	}
	
	public void close()
	{
		isThreadActive = false;
	}
}