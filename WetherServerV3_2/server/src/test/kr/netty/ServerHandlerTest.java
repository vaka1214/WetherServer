package kr.netty;
import org.junit.Test;
import static org.junit.Assert.*;

public class ServerHandlerTest {
    @Test
    public void serverStringTest(){
        ServerHandler  sh = new ServerHandler(Server.qIn);
        String name = sh.serverString("Moscow");
        assertEquals("Moscow", name);
    }

}
