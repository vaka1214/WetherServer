package kr.netty;
import org.junit.Test;
import junit.framework.Assert;
import org.junit.Test;
import io.netty.channel.ChannelHandlerContext;

import static org.junit.Assert.*;

public class TaskAndCtxTest {
    public Task task;
    public ChannelHandlerContext ctx;
    @Test
    public void TaskAndCtxTest() {

        assertTrue(null== task);
        assertTrue(null== ctx);
    }
}