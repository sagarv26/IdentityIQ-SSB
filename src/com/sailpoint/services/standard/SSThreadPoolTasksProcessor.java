package com.sailpoint.services.standard;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * A new java class by the name GEThreadPoolTasksProcessor is developed to create thread pool and time out tasks based on the URL parameters.
 * This class uses the inner class GEAppConnectivityCallable callable objects to create task for each application.
 * All tasks were processed and computed by using the thread pool. 
 * A task will be cancelled in case it doesn't get response from the application in the specified time frame
 * @author <a href="mailto:rohit.gupta@sailpoint.com">Rohit Gupta</a>
 */

public class SSThreadPoolTasksProcessor 
{
	private  int numberOfThreads;
	private  int timeOutWindow;
	private  Collection<Callable <Map<String, Object>>> callableList;
	private static final Log log = LogFactory.getLog(SSThreadPoolTasksProcessor.class);
	private  List<Map<String,Object>>  results;
    private int success = 0;
    private int failure = 0;
    /**
     * Default Constructor
	 * @param int numberOfThreads, int timeOutWindow, List<Callable <Map<String, Object>>> callableList,List<Map<String,Object>> results
	 * @return void
	 * 
	 */
	public SSThreadPoolTasksProcessor(int numberOfThreads, int timeOutWindow, List<Callable <Map<String, Object>>> callableList,List<Map<String,Object>> results)
	{
		this.numberOfThreads=numberOfThreads;
		this.timeOutWindow=timeOutWindow;
		this.callableList=callableList;
		this.results=results;
		
	}
	/**
	 * This methods starts the processing of tasks using thread pool
	 * There will be one task for each application.
	 * @param none 
	 * @return void
	 * 
	 */
	public void start() 
	{
	    ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
	    log.debug("Executor "+executor);
	    long startTime=0;
	    try
	    {
	    List< Future< Map<String, Object> > > listFutureResult=null;
	    startTime = new Date().getTime();
	   	listFutureResult = ( List< Future< Map<String, Object> > >) executor.invokeAll(callableList,timeOutWindow, TimeUnit.MILLISECONDS);
	   	calculateResults( listFutureResult);
	    }
	    catch (Exception ex)
	    {
	    	log.error("Exception from GEThreadPoolTasksProcessor "+ex.getMessage());
	    }
	    finally
	    {
		       log.debug("TOTAL SUCCESS - "+ success);
		       log.debug("TOTAL FAILURE - "+ failure);
		       log.debug("Total time - " + (new Date().getTime() - startTime)/1000 + "seconds");
		       executor.shutdown();
		       
		}
	}  
	/**
	 * This methods calculates  the result of each task/application to the 
	 * There will be one task for each application.
	 * @param List< Future< Map<String, Object> > > listFutureResult 
	 * @return void
	 * 
	 */
	  public void calculateResults(List< Future< Map<String, Object> > > listFutureResult)
	  {
			 log.debug("listFutureResult "+listFutureResult);
			 List< Future< Map<String, Object> > > trackFutureResult = new ArrayList<Future<Map<String, Object>>>();
			 try {
			  if(listFutureResult!=null && listFutureResult.size()>0)
			    	    {
			          
			          		for(Future<Map<String, Object>> futureResult : listFutureResult)
			          		{
				          		log.debug("FutureResult before get "+futureResult);
				          		if(futureResult!=null)
				          		{
				          			
				          			log.debug("FutureResult Not Null");
				          			if (futureResult.isDone() && !futureResult.isCancelled()) 
					          		{
				          				log.debug("FutureResult Is done");
				          			 	log.debug("SUCCESS");
				          				Map<String, Object> map = futureResult.get();
						          		log.debug("FutureResult after get"+map);
						          		if(map!=null)
						          		{
						          		results.add(map);
						          		success=success+1;
						          		trackFutureResult.add(futureResult);
						          		}
					          		}
				          			else
				          			{
				          				failure=failure+1;
				          			}
				          			
				          		}
				      		}
				    } 
			    }
			    catch (CancellationException cancellationException) 
			    {
					log.error("CancellationException from GEThreadPoolTasksProcessor "+cancellationException.getMessage());
				}
			    catch (ExecutionException executionException)
				{
					log.error("ExecutionException from GEThreadPoolTasksProcessor "+executionException.getMessage());
				} 
			    catch (InterruptedException interruptedException) 
			    {
					log.error("InterruptedException from GEThreadPoolTasksProcessor "+interruptedException.getMessage());
				}
			    catch (Exception exception) 
			    {
					log.error("Exception from GEThreadPoolTasksProcessor "+exception.getMessage());
				}
	   
	  }

   }
