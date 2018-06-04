package kr.netty;

import io.netty.bootstrap.Bootstrap;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class IntegrationClient
{
    public static void main(String[] args) throws Exception
	{
        String host = "localhost";
        int port = Integer.parseInt("5415");
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try
		{

            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>()
			{
                @Override
                public void initChannel(SocketChannel ch) throws Exception
				{

                    ch.pipeline().addLast(new IntegrationClientHandler());

                }
            });
            do {
                ChannelFuture f = b.connect(host, port).sync();
                f.channel().closeFuture().sync();
            } while (!IntegrationClientHandler.s.equals("exit"));
            }
		finally
		{
            workerGroup.shutdownGracefully();
        }
    }
}