package kr.netty;

import io.netty.bootstrap.ServerBootstrap;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.ArrayDeque;

public class Server
{
    private int port;
	
	public static BlockingQueue<TaskAndCtx> qIn = new LinkedBlockingQueue<TaskAndCtx>();
	public static ArrayDeque<TaskAndCtx> qOut = new ArrayDeque<TaskAndCtx>();
	
	public static int threadNumber = 5;
	
	public static void main(String[] args) throws Exception
	{
		TaskExecutor taskExecutor = new TaskExecutor(qIn, qOut);
		Thread[] threadExecutorArr = new Thread[threadNumber];
		for (int i=0; i<threadNumber;++i)
			threadExecutorArr[i] = new Thread(taskExecutor);
		
		Logger logger = new Logger(qOut);
		Thread threadLogger = new Thread(logger);
		
		for (int i=0; i<threadNumber;++i)
			threadExecutorArr[i].start();
        threadLogger.start();
		
        int port;
        if (args.length > 0)
		{
            port = Integer.parseInt(args[0]);
        }
		else
		{
            port = 5415;
        }
        new Server(port).run();
    }
	
    public Server(int port)
	{
        this.port = port;
    }
	
    public void run() throws Exception
	{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try
		{
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .childHandler(new ChannelInitializer<SocketChannel>()
			 {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception
				 {
                     ch.pipeline().addLast(new ServerHandler(qIn));
                 }
             })
             .option(ChannelOption.SO_BACKLOG, 128)
             .childOption(ChannelOption.SO_KEEPALIVE, true);
    
            ChannelFuture f = b.bind(port).sync();
    
            f.channel().closeFuture().sync();
        }
		finally
		{
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
