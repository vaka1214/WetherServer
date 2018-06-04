package kr.netty;

import io.netty.channel.ChannelHandlerContext;

public class TaskAndCtx
{
	public Task task;
	public ChannelHandlerContext ctx;
	
	public TaskAndCtx()
	{
		this.task=null;
		this.ctx=null;
	}
	public TaskAndCtx(Task task, ChannelHandlerContext ctx)
	{
		this.task=task;
		this.ctx=ctx;
	}
}