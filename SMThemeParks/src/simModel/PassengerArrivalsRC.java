package simModel;

import cern.jet.random.Exponential;
import cern.jet.random.engine.MersenneTwister;
import simulationModelling.ScheduledAction;

public class PassengerArrivalsRC extends ScheduledAction {

	static ThemeParks model;

	@Override
	protected double timeSequence() {
		
		return rvpUpassengerArrivalRC();
	}


	@Override
	protected void actionEvent() {

		// Adds passenger to queue
		model.qStations[Constants.RACCOON].n += 1;
		
	}

	static void initRVPs(Seeds sd) {
		interArrDistRaccoon = new Exponential(1.0/getMeanRC(), new MersenneTwister(sd.seed1));
	}
	// RVP for interarrival times.
	static private Exponential interArrDistRaccoon;

	static protected double getMeanRC() {
		double mean = 0;

		if(model.getClock() < 60) mean = 0.156;
		else if (model.getClock() > 60 && model.getClock() < 120) mean = 0.188;
		else if (model.getClock() > 120 && model.getClock() < 180) mean = 0.214;
		else if (model.getClock() > 180 && model.getClock() < 240) mean = 0.226;
		else if (model.getClock() > 240 && model.getClock() < 300) mean = 0.207;
		else if (model.getClock() > 300 && model.getClock() < 360) mean = 0.190;
		else if (model.getClock() > 360 && model.getClock() < 420) mean = 0.2;
		else if (model.getClock() > 420 && model.getClock() < 480) mean = 0.188;
		else if (model.getClock() > 480 && model.getClock() < 540) mean = 0.214;
		else if (model.getClock() > 540 && model.getClock() < 600) mean = 0.194;
		else if (model.getClock() > 600 && model.getClock() < 660) mean = 0.167;
		else if (model.getClock() > 660 && model.getClock() < 720) mean = 0.152;
		//else mean = ;

		return mean;
	}

	static protected double rvpUpassengerArrivalRC() {
		double nxtInterArr;
		// Note that inter-arrival time is added to current
		// clock value to get the next arrival time.
		nxtInterArr = model.getClock()+interArrDistRaccoon.nextDouble(1.0/getMeanRC());
		if(nxtInterArr > model.closingTime)
			nxtInterArr = -1.0;  // Ends time sequence
		return(nxtInterArr);
	}

}
