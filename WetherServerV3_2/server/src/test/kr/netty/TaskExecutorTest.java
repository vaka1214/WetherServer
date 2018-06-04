package kr.netty;
import org.junit.Test;
import java.util.ArrayDeque;
import java.util.concurrent.BlockingQueue;
import static org.junit.Assert.*;



public class TaskExecutorTest {

    private ArrayDeque<TaskAndCtx> qOut;
    int maxSize = 50;

    @Test
    public void runTest() {
        qOut = new ArrayDeque<TaskAndCtx>(maxSize);
        Task task = new Task(3,"Kair");
        task.temperature = "+40";

        TaskAndCtx curt = new TaskAndCtx(task, null);
        qOut.push(curt);

        Logger logger = new Logger(qOut);
        Thread loggerStream = new Thread(logger);

        loggerStream.start();
        loggerStream.interrupt();

        assertFalse(qOut.isEmpty());
        assertEquals("+40",curt.task.temperature);
    }
}