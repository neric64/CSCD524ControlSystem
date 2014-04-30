package cs524.controlsystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

//===============================================================================================================================
/**
 * Defines the driver for the tests.  You may change this however you want.
 * 
 * @author Dan Tappan [23.04.14]
 */
public class Driver
{
   // ------------------------------------------------------------------------------------------------------------------------------
	
	public static double P = -0.01;
	public static double I = 0.000001;
	public static double D = -0.03;
	public static double prev_error = 0;
	
	
	
   /**
    * Runs the driver.
    * 
    * @param arguments - the are no command-line arguments
    */
   public static void main(final String[] arguments)
   {
      Driver driver = new Driver();

      driver.runTest1();
   }

   // ----------------------------------------------------------------------------------------------------------------------------
   /**
    * zzz
    */
   public Driver()
   {
   }

   // ------------------------------------------------------------------------------------------------------------------------------
   /**
    * Adjusts the target azimuth delta based on where the agent is with respect to the horizontal line at y=0. The agent 
    * should be aiming to be on the line. The
    * only element of the agent that may written to setAzimuthDeltaTarget(). Anything may be read.
    * 
    * This solution defines a "bang-bang" controller with poor stability.
    * 
    * @param agent - the agent being controlled
    */
   private void doControllerBangBang(final Agent agent)
   {
      Assert.nonnull(agent);

      double targetY = 0;
      double agentY = agent.getY();

      if (agentY < targetY)
      {
         agent.setAzimuthDeltaTarget(-1);
      }
      else
      {
         agent.setAzimuthDeltaTarget(+1);
      }
   }

   // ------------------------------------------------------------------------------------------------------------------------------
   /**
    * Adjusts the target azimuth delta based on where the agent is with respect to the horizontal line at y=0. The agent 
    * should be aiming to be on the line. The
    * only element of the agent that may written to setAzimuthDeltaTarget(). Anything may be read.
    * 
    * This solution will define a PID controller. The implementation is your choice, but it must be a PID controller.
    * 
    * @param agent - the agent being controlled
    */
   private void doControllerPID(final Agent agent)
   {
      Assert.nonnull(agent);

      agent.getY();
      
      double targetY = 0;
      
      double error = 0;
      double integral = 0;
      double y = agent.getY();
      
      error = targetY - y;
      integral = I + error;
      double derivative = (error - prev_error);
      double output = (P * error) + (I * integral) + (D * derivative);
      prev_error = error;
      
      /*
       * previous_error = 0
integral = 0 
start:
  error = setpoint - measured_value
  integral = integral + error*dt
  derivative = (error - previous_error)/dt
  output = Kp*error + Ki*integral + Kd*derivative
  previous_error = error
  wait(dt)
  goto start
       */
      
      
      
      if (output < targetY)
      {
         agent.setAzimuthDeltaTarget(-1);
      }
      else
      {
         agent.setAzimuthDeltaTarget(+1);
      }
      
   }

   // ----------------------------------------------------------------------------------------------------------------------------
   /**
    * Runs a sample test with the bang-bang controller. 
    */
   private void runTest1()
   {
	   PrintWriter pw = null;
	   try {
			pw = new PrintWriter(new File("gnuoutput.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
      // set your iterations to whatever is necessary to achieve stability
      int iterationCount = 5000;

      // this defines the agent at position (0,-20) with azimuth 45 degrees and speed 1, with instantaneous delta deflection 
      Agent agent = new Agent(0, -20, 1, 45, false);

      //System.out.println(agent.getStateGnuplot());
      pw.println(agent.getStateGnuplot());

      for (int iIteration = 0; iIteration < iterationCount; ++iIteration)
      {
    	  doControllerPID(agent);

         agent.update_();

         //System.out.println(agent.getStateGnuplot());
         
         pw.println(agent.getStateGnuplot());
         
         
      }
      
      pw.close();
   }
}
