package threads;

import java.io.IOException;

import com.jcabi.ssh.Shell;

import constants.Constants;
/**
 * This class implements a thread to update the assignments file on the specified
 * server with a new grade. This is so that the overhead of running an SSH command
 * is not incurred in the user interface after a grade is updated.
 * 
 * @author Gabriel Miller
 * @version 1.0
 * @since 11/9/2016
 */
public class GradeUpdateThread implements Runnable {

   private Thread t;
   private String threadName;
   private String updateCommand;
   
   /**
    * Thread class constructor.
    * 
    * @param threadName thread name
    * @param updateCommand the Unix command that will be executed on the server
    */
   public GradeUpdateThread(String threadName, String updateCommand) {
	  this.threadName    = threadName;
      this.updateCommand = updateCommand;
   }
   
   /**
    * When the thread is run, the shell will execute the specified update command.
    */
   public void run() {
	   String message = null;
	   try {
			message = new Shell.Plain(Constants.shell).exec(updateCommand);
		} catch (IOException e) {
			System.out.println(message);
			e.printStackTrace();
		}
   }
   
   /**
    * Creates the thread.
    */
   public void start () {
      System.out.println("Starting " +  threadName );
      if (t == null) {
         t = new Thread (this, threadName);
         t.start ();
      }
   }

}
