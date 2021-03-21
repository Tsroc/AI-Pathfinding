package ie.gmit.sw.ai.personality;

import ie.gmit.sw.ai.nn.BackpropagationTrainer;
import ie.gmit.sw.ai.nn.NeuralNetwork;
import ie.gmit.sw.ai.nn.Utils;
import ie.gmit.sw.ai.nn.activator.Activator;

public class TrainPersonalityNN {

	private static NeuralNetwork nn = new NeuralNetwork(Activator.ActivationFunction.Sigmoid, 3, 2, 3);
	
	private static final TrainPersonalityNN instance = new TrainPersonalityNN();
	
	private TrainPersonalityNN() {}
	
	public static TrainPersonalityNN getInstance() {
		return instance;
	}
	
	public void train() {
		
		// This needs to be fixed
		BackpropagationTrainer trainer = new BackpropagationTrainer(nn);
		trainer.train(data, expected, 0.05, 10000);
		
		
		
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
		{0, 0, 0}, {0, 0, 30}, {0, 0, 60}, {0, 30, 0},
		{0, 60, 0}, {30, 0, 0}, {60, 0, 0},
		
		{0, 30, 30}, {0, 30, 60}, {0, 60, 30}, {0, 60, 60},
		{30, 0, 30}, {30, 0, 60}, {60, 0, 30}, {60, 0, 60},
		{30, 30, 0}, {30, 60, 0}, {60, 30, 0}, {60, 60, 0},
		{30, 30, 30}, {30, 30, 60}, {30, 60, 30}, {60, 30, 30},
		{30, 60, 60}, {60, 60, 30}, {60, 30, 60}, {60, 60, 60},


		{0, 0, 15}, {0, 80, 0}, {15, 0, 30}, {30, 30, 15},
		{30, 80, 30}, {60, 80, 30}, {0, 30, 0}

	};

	private double[][] expected = {
		{0, 0, 1}, {0, 0, 1}, {0, 0, 1}, {1, 0, 0},
		{0, 0, 1}, {1, 0, 0}, {1, 0, 0},
		{0, 0, 1}, {0, 0, 1}, {0, 0, 1}, {0, 0, 1},
		{0, 1, 0}, {0, 1, 0}, {0, 1, 0}, {0, 1, 0},
		{1, 0, 0}, {1, 0, 0}, {1, 0, 0}, {1, 0, 0},
		
		{0, 1, 0}, {0, 1, 0}, {0, 1, 0}, {0, 1, 0},
		{1, 0, 0}, {0, 1, 0}, {0, 1, 0}, {1, 0, 0},
		

		{1, 0, 0}, {1, 0, 0}, {0, 1, 0}, {0, 1, 0},
		{1, 0, 0}, {1, 0, 0}, {0, 1, 0}
		
	};
}
