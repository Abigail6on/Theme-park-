package simModel;

import cern.jet.random.Exponential;
import cern.jet.random.engine.MersenneTwister;
import simulationModelling.ScheduledAction;

public class PassengerArrivalsSH extends ScheduledAction {

	static ThemeParks model;

	@Override
	protected double timeSequence() {
		
		return rvpUpassengerArrivalSH();
	}


	@Override
	protected void actionEvent() {

		// Adds passenger to queue
		model.qStations[Constants.SKUNK].n += 1;
		
	}

	static void initRVPs(Seeds sd){
		interArrDistSkunk = new Exponential(1.0/getMeanSH(), new MersenneTwister(sd.seed1));
	}

	// RVP for interarrival times.
	static private Exponential interArrDistSkunk;

	static protected double getMeanSH() {
		double mean = 0;

		if(model.getClock() < 60) mean = 0.15;
		else if (model.getClock() > 60 && model.getClock() < 120) mean = 0.171;
		else if (model.getClock() > 120 && model.getClock() < 180) mean = 0.24;
		else if (model.getClock() > 180 && model.getClock() < 240) mean = 0.218;
		else if (model.getClock() > 240 && model.getClock() < 300) mean = 0.207;
		else if (model.getClock() > 300 && model.getClock() < 360) mean = 0.197;
		else if (model.getClock() > 360 && model.getClock() < 420) mean = 0.2;
		else if (model.getClock() > 420 && model.getClock() < 480) mean = 0.214;
		else if (model.getClock() > 480 && model.getClock() < 540) mean = 0.194;
		else if (model.getClock() > 540 && model.getClock() < 600) mean = 0.188;
		else if (model.getClock() > 600 && model.getClock() < 660) mean = 0.167;
		else if (model.getClock() > 660 && model.getClock() < 720) mean = 0.148;
		//else mean = ;

		return mean;
	}

	static protected double rvpUpassengerArrivalSH() {
		double nxtInterArr;
		// Note that inter-arrival time is added to current
		// clock value to get the next arrival time.
		nxtInterArr = model.getClock()+interArrDistSkunk.nextDouble(1.0/getMeanSH());
		if(nxtInterArr > model.closingTime)
			nxtInterArr = -1.0;  // Ends time sequence
		return(nxtInterArr);
	}
}
