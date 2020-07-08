package simModel;

import cern.jet.random.engine.RandomSeedGenerator;

public class Seeds 
{
	int seed1;
	int seed2;
	int seed3;
	int seed4;

	public Seeds(RandomSeedGenerator rsg)
	{
		seed1=rsg.nextSeed();
		seed2=rsg.nextSeed();
		seed3=rsg.nextSeed();
		seed4=rsg.nextSeed();
	}
}

