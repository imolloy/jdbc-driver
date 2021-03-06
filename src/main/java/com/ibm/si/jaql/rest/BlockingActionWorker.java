package com.ibm.si.jaql.rest;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ibm.si.jaql.rest.RESTClient.Result;

/**
 * Blocking thread that will wait until the ariel search query completes and returns data
 * @author IBM
 *
 */
public class BlockingActionWorker implements Runnable
{
	static final Logger logger = LogManager.getLogger(BlockingActionWorker.class.getName());

	private RESTClient client = null;
	private String request = null;
	private Result result = null;
	
	public BlockingActionWorker(final RESTClient client, final String req)
	{
		this.client = client;
		this.request = req;
	}
	
	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				result = client.doGet(request);
				int status = result.getStatus();
				int uniqueErrorcode = result.getCode();
				if (status == 404 && uniqueErrorcode == 1003)
				{
					logger.debug(String.format("Status was %d", result.getStatus()));
					logger.debug(String.format("Status Code was %d", result.getCode()));
					Thread.sleep(5000);
				}
				else
				{
					break;
				}
			}
		}
		catch (IOException e)
		{
			//for now, dump the checked exception, as we cannot propagate it 
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			//for now, dump the checked exception, as we cannot propagate it 
			e.printStackTrace();
		}
	}
	
	public Result getResult()
	{
		return result;
	}
}
