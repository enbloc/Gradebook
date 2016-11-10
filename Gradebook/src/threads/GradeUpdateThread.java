/*
 * GradeUpdateThread.java
 * 
 * This class implements a thread to update the assignments file on the specified
 * server with a new grade. This is so that the overhead of running an SSH command
 * is not incurred in the user interface after a grade is updated.
 * 
 * Gabriel Miller
 * 11/9/2016
 */
package threads;

import java.io.IOException;

import com.jcabi.ssh.Shell;

import constants.Constants;

public class GradeUpdateThread implements Runnable {

   private Thread t;
   private String threadName;
   private String updateCommand;
   
   public GradeUpdateThread(String threadName, String updateCommand) {
	  this.threadName    = threadName;
      this.updateCommand = updateCommand;
   }
   
   public void run() {
	   String message = null;
	   try {
			message = new Shell.Plain(Constants.shell).exec(updateCommand);
		} catch (IOException e) {
			System.out.println(message);
			e.printStackTrace();
		}
   }
   
   public void start () {
      System.out.println("Starting " +  threadName );
      if (t == null) {
         t = new Thread (this, threadName);
         t.start ();
      }
   }

}
