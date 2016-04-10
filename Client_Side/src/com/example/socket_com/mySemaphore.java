package com.example.socket_com;

/*  how it's work:
 * 1.semaphore is just a variable than when it equals 0 it causes the current thread to busy_wait until another thread 
 *   increments it by one which causes the first thread to exit the busy wait.
 *   in that manner,to control threads sync we call 'wait' at the thread we want to run last and signal at the end of the thread we want to run first. 
 * 2.wait() tells the calling thread to give up the monitor and go to sleep until some other thread enters the same monitor and calls notify( ).
 * 3.notify() wakes up the first thread that called wait() on the same object.
 * 4.synchronized method: 
 *   only one thread at a time can be executing in this or any other synchronized method of the object
 *   representing the monitor.
 *   i.e : if one thread running synchronized method then another thread cant run this method or any other 
 *   synchronized method of the monitor!!!
 * 5.semaphore permits: permits allow to n threads to continue executing and not to block.
 *	 each wait() removes permit and each signal() adds permit.
 *	 therefore the n+1 thread which called wait() will be blocked.
 *	 if permit=0 the first thread who called wait() will be blocked.
 * 
 * 
 * 
 */


/**note about timedOut:
 * I used in this project semaphore for thread synchronization.
 * when a packet is sent to server with a request for data(for example: games list) the thread is blocked until the data is received from the server.
 * however, if the server is down ,then the blocked thread will be blocked forever.
 * for this reason, i block the thread for a period of time; therefore if the server is down the thread will be timeout from the semaphore.
 * in that case , permits will be stay at zero value(because release() from the server will inc permits be one)
 * the function isTimedOut lets you know if the semaphore is released be the response of the server or it released by timeout of no response from the server.
 * 
 *
 */

public class mySemaphore {
	protected int permits;
	protected boolean timedOut=false;
	public mySemaphore(){
		this.permits=0;
	}
	public mySemaphore(int permits){
		this.permits=permits;
	}




	public synchronized void acquire() throws InterruptedException{
		timedOut=false;
		if(permits>0)
			permits--;
		else
		{
			if(permits<=0)
			{
				this.wait(5000);//puts to sleep	
				if(permits==0)
					timedOut=true;
			}
			permits--;
		}
	}
	public synchronized void s_wait(int thread_index) throws InterruptedException{

	}

	public boolean isTimedOut(){
		return timedOut;
	}
	public synchronized void release(){
		permits++;
		this.notify();//wakes the FIRST thread that put to sleep.
	}


}
