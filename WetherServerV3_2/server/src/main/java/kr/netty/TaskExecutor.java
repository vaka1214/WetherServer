package kr.netty;

import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.ArrayDeque;

public class TaskExecutor implements Runnable
{
	private BlockingQueue<TaskAndCtx> qIn;
	private ArrayDeque<TaskAndCtx> qOut;
	
	private boolean isThreadActive;

	
	public TaskExecutor(BlockingQueue<TaskAndCtx> qIn, ArrayDeque<TaskAndCtx> qOut)
	{
		this.qIn=qIn;
		this.qOut=qOut;
		this.isThreadActive = true;
	}
	
	@Override
	public void run()
	{
		while (isThreadActive)
		{
			TaskAndCtx curTask = null;
			synchronized(qIn)
			{
				curTask = qIn.poll();
			}
			if (curTask!=null){


				boolean ignore=true;
				for(String city :DataBase.Weather.getCities()){
					if(curTask.task.city.equals(city)){
						ignore=false;
					}
				}
				if(ignore==false){
					try{
						curTask.task.temperature=DataBase.Weather.getWeather(curTask.task.city);
					}catch(SQLException e){
						e.printStackTrace();
					}
				}else{
					if(curTask.task.city.equals("quit")){

						curTask.task.temperature="Exit";
					}else{
						curTask.task.temperature="No info";
					}
				}

				synchronized (qOut)
				{
					qOut.add(curTask);
				}
			}
			try{
				Thread.sleep(100);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	
	public void close()
	{
		isThreadActive = false;
	}
}