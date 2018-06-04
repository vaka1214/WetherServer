package kr.netty;

import org.junit.Test;

import java.util.ArrayDeque;

import static org.junit.Assert.*;
public class LoggerTest {

    private ArrayDeque<TaskAndCtx> qOut;
    int maxSize = 100;

    @Test
    public void runTest() {
        qOut = new ArrayDeque<TaskAndCtx>(maxSize);
        Task task = new Task(1,"Moskow");
        task.temperature = "+10";

        TaskAndCtx curt = new TaskAndCtx(task, null);
        qOut.push(curt);

        Logger logger = new Logger(qOut);
        Thread loggerStream = new Thread(logger);

        loggerStream.start();
        loggerStream.interrupt();

        assertFalse(qOut.isEmpty());
    }
}