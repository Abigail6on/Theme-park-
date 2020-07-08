package simModel;

import simulationModelling.OutputSequence;
import simulationModelling.ScheduledAction;

class Initialise extends ScheduledAction
{
	static ThemeParks model;


	double [] ts = { 0.0, -1.0 }; // -1.0 ends scheduling
	int tsix = 0;  // set index to first entry.
	public double timeSequence() 
	{
		return ts[tsix++];  // only invoked at t=0
	}

	public void actionEvent() 
	{
		
		//Initialize the output variables
		model.output.ssovNumType1 = 0;
		model.output.ssovNumType2 = 0;
		model.output.ssovNumType3 = 0;
		model.output.ssovNumType4 = 0;




		for(int stid = Constants.FROG ; stid <= Constants.RACCOON ; stid++){
		model.qStations[stid] = new Stations();
		model.qTrainTrack[stid] = new TrainTrack();
		}







		for (int tid=0; tid<model.numTrains; tid++) {
		    model.rgTrain[tid] = new Train();
		    model.rgTrain[tid].boarding = false;
			model.rgTrain[tid].capacity = model.numCars[tid] * 25;
		    if(tid%4 == 0)
		    	model.qTrainTrack[Constants.FROG].spInsertQueue(tid);
		    else if(tid%4 == 1)
				model.qTrainTrack[Constants.SKUNK].spInsertQueue(tid);
			else if(tid%4 == 2)
				model.qTrainTrack[Constants.GATOR].spInsertQueue(tid);
			else if(tid%4 == 3)
				model.qTrainTrack[Constants.RACCOON].spInsertQueue(tid);

		}



		//Creating arrays of size numTrains for output
		model.output.trjTrainFull = new OutputSequence[model.numTrains];
		model.output.lastTrainFull = new double[model.numTrains];
		for (int i = 0 ; i < model.numTrains ; i++){
			model.output.trjTrainFull[i] = new OutputSequence("TrainFull[" + i + "]");
			model.output.trjTrainFull[i].put(0.0D, 0.0D);
		}






	}
}
