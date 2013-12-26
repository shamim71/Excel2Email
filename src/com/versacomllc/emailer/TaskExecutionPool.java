package com.versacomllc.emailer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;



public class TaskExecutionPool {
	
	private int poolSize = 20;
	 
   private  int maxPoolSize = 200;
 
    private long keepAliveTime = 100;
 
    private ThreadPoolExecutor threadPool = null;
    
    private final ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(15);
    private static final TaskExecutionPool instance;
    static{
    	instance = new TaskExecutionPool();
    }

    private TaskExecutionPool(){
        threadPool = new ThreadPoolExecutor(poolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS, queue);
    }
    public static TaskExecutionPool getInstance(){
    	return instance;
    }
    public void addTaskToPool(Runnable task)
    {
    	
    	//logger.debug("Task count in pool .."+threadPool.getTaskCount() );
        threadPool.execute(task);
        //logger.debug("Task addded");
    	//logger.debug("Task in pool: .."+threadPool.getTaskCount() );
        //logger.debug("Queue size: .." + queue.size());
        
 
    }

    public void shutDown()
    {
        threadPool.shutdown();
    }
    
}
