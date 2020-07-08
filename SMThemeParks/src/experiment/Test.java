package experiment;

import simModel.ThemeParks;
import simModel.Seeds;
import cern.jet.random.engine.*;
import simulationModelling.*;

public class Test {

	public static void main(String[] args) 	{
	       int i, NUMRUNS = 1;
	       double startTime=0.0, endTime=720.0;
	       Seeds[] sds = new Seeds[NUMRUNS];
	       ThemeParks thmPrk;  // Simulation object

	       // Lets get a set of uncorrelated seeds
	       RandomSeedGenerator rsg = new RandomSeedGenerator();
	       for(i=0 ; i<NUMRUNS ; i++) sds[i] = new Seeds(rsg);
	       
	       
	       // Loop for NUMRUN simulation runs for each case
	       // Case 1
		   System.out.println("--------------------------------------");
	       System.out.println("       Case1 - One Side Door");
		   System.out.println("--------------------------------------");
	       for(i=0 ; i < NUMRUNS ; i++)
	       {
	       	int NUM_INITIAL_CARS = 3;
	       	int numTrain = 1;
	       	int[] numCars = new int[numTrain];
	       	numCars = distributeCars(NUM_INITIAL_CARS,numTrain,numCars);

	          thmPrk = new ThemeParks(startTime,endTime, false, 0, sds[i], numTrain, numCars,false);
	          thmPrk.runSimulation();
	          double trainUtil[] = thmPrk.trainUtilization();

	          System.out.println("Terminated "+(i+1)+", "+"\npercentType1 = % "+thmPrk.getPercent(1) + "\npercentType2 = % " + thmPrk.getPercent(2) +
				   "\npercentType3 = % " + thmPrk.getPercent(3) + "\npercentType4 = % " + thmPrk.getPercent(4));
			   for (int tid = 0 ; tid < numTrain ; tid++){
				   System.out.println("trainUtilization[tid = "+tid+"] = % "+trainUtil[tid]);
			   }



	       }






		}





public static int[] distributeCars(int initialCars, int numTrain, int[] numCars)
{
		int carPerTrain = initialCars/numTrain;
		int remainingCars = initialCars % numTrain;
		for(int tid = 0; tid < numTrain ; tid++)
			numCars[tid] = carPerTrain;
		for (int tid = 0 ; tid < remainingCars ; tid++)
			numCars[tid]++;
		return numCars;

}
public static int findHighestUtil(int numTrain, double[] trainUtil){
		double max = 0;
		int index = 0;
		for(int tid = 0 ; tid < numTrain ; tid++){
			if(trainUtil[tid]>max){
				max = trainUtil[tid];
				index = tid;
		}
		}
		return index;
}
}
