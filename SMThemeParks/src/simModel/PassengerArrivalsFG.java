package simModel;

import cern.jet.random.Exponential;
import cern.jet.random.engine.MersenneTwister;
import simulationModelling.ScheduledAction;

public class PassengerArrivalsFG extends ScheduledAction {

	static ThemeParks model;

	@Override
	protected double timeSequence() {
		
		return rvpUpassengerArrivalFP();
	}


	@Override
	protected void actionEvent() {
		// Adds passenger to queue
		model.qStations[Constants.FROG].n += 1;
		
	}

	static void initRVPs(Seeds sd){
	interArrDistFrog = new Exponential(1.0/getMeanFP(), new MersenneTwister(sd.seed1));
}

	// RVP for interarrival times.
	static private Exponential interArrDistFrog;

	static protected double getMeanFP() {
		double mean = 0;
		if(model.getClock() < 60) mean = 0.133;
		else if (model.getClock() > 60 && model.getClock() < 120) mean = 0.2;
		else if (model.getClock() > 120 && model.getClock() < 180) mean = 0.218;
		else if (model.getClock() > 180 && model.getClock() < 240) mean = 0.211;
		else if (model.getClock() > 240 && model.getClock() < 300) mean = 0.194;
		else if (model.getClock() > 300 && model.getClock() < 360) mean = 0.188;
		else if (model.getClock() > 360 && model.getClock() < 420) mean = 0.214;
		else if (model.getClock() > 420 && model.getClock() < 480) mean = 0.231;
		else if (model.getClock() > 480 && model.getClock() < 540) mean = 0.207;
		else if (model.getClock() > 540 && model.getClock() < 600) mean = 0.19;
		else if (model.getClock() > 600 && model.getClock() < 660) mean = 0.156;
		else if (model.getClock() > 660 && model.getClock() < 720) mean = 0.145;
		//else mean = ;

		return mean;
	}

	static protected double rvpUpassengerArrivalFP() {
		double nxtInterArr;

		// Note that inter-arrival time is added to current
		// clock value to get the next arrival time.
		nxtInterArr = model.getClock()+interArrDistFrog.nextDouble(1.0/getMeanFP());
		if(nxtInterArr > model.closingTime)
			nxtInterArr = -1.0;  // Ends time sequence
		return(nxtInterArr);
	}

}
