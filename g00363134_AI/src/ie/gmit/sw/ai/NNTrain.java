package ie.gmit.sw.ai;

import ie.gmit.sw.ai.nn.BackpropagationTrainer;
import ie.gmit.sw.ai.nn.NeuralNetwork;
import ie.gmit.sw.ai.nn.Utils;
import ie.gmit.sw.ai.nn.activator.Activator;

public class NNTrain {

	// This needs to be fixed
	private static NeuralNetwork nn = new NeuralNetwork(Activator.ActivationFunction.Sigmoid, 3, 3, 3);
	
	private static final NNTrain instance = new NNTrain();
	
	private NNTrain() {}
	
	public static NNTrain getInstance() {
		return instance;
	}
	
	public void train() {
		
		// This needs to be fixed
		BackpropagationTrainer trainer = new BackpropagationTrainer(nn);
		trainer.train(data, expected, 0.6, 10000);
		
	}
	
	public static NeuralNetwork getNN() {
		return nn;
	}
	
	
	/*
	 *  Inputs
	 *  -------------
	 *  1) Hunger (2 = full, 1 = satisfied, 0 = starving)
	 *  2) Fear (2 = coward, 1 = normal, 0 = brave)
	 *  3) Health (2 = Healthy, 1 = Minor Injuries, 0 = Serious Injuries)
	 *  
	 *  Outputs
	 *  -------------
	 *  1) Skittish 
	 *  2) Friendly 
	 *  3) Aggressive 
	 *  
	 */

	// double check the data...
	private double[][] data = {
	{0, 0, 0}, {0, 0, 1}, {0, 0, 2}, {0, 1, 0},
	{0, 2, 0}, {1, 0, 0}, {2, 0, 0},
	{0, 1, 1}, {0, 1, 2}, {0, 2, 1}, {0, 2, 2},
	{1, 0, 1}, {1, 0, 2}, {2, 0, 1}, {2, 0, 2},
	{1, 1, 0}, {1, 2, 0}, {2, 1, 0}, {2, 2, 0},
	{1, 1, 1}, {1, 1, 2}, {1, 2, 1}, {2, 1, 1},
	{1, 2, 2}, {2, 2, 1}, {2, 1, 2}, {2, 2, 2}

	};

	private double[][] expected = {
	{1, 0, 0}, {0, 0, 1}, {0, 0, 1}, {1, 0, 0},
	{1, 0, 0}, {1, 0, 0}, {1, 0, 0},
	{0, 0, 1}, {0, 0, 1}, {0, 0, 1}, {0, 0, 1},
	{0, 1, 0}, {0, 1, 0}, {0, 1, 0}, {0, 1, 0},
	{1, 0, 0}, {1, 0, 0}, {1, 0, 0}, {1, 0, 0},
	{0, 1, 0}, {0, 1, 0}, {1, 0, 0}, {0, 1, 0},
	{1, 0, 0}, {1, 0, 0}, {0, 1, 0}, {1, 0, 0},

	};
}
