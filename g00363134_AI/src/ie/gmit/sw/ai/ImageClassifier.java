package ie.gmit.sw.ai;

import ie.gmit.sw.ai.nn.BackpropagationTrainer;
import ie.gmit.sw.ai.nn.NeuralNetwork;
import ie.gmit.sw.ai.nn.Utils;
import ie.gmit.sw.ai.nn.activator.Activator;

import java.io.*;
import java.awt.image.*;

import javax.imageio.*;

public class ImageClassifier {
	private String[] names = {"blue", "gred", "orange", "pink", "red"};
	private double[][] data;
	private double[][] expected;
	private int NUM_CATEGORIES = 5;
	
	public ImageClassifier(String directory) throws Exception{
		parse(directory);
		
		//Configure Neural Network Here
		NeuralNetwork nn = new NeuralNetwork(Activator.ActivationFunction.Sigmoid, 4096, 64,
				NUM_CATEGORIES);
		BackpropagationTrainer trainer = new BackpropagationTrainer(nn);
		trainer.train(Utils.normalize(data, 0, 1), Utils.normalize(expected, 0, 1), 0.01, 10000);
		
		System.out.println("Training completed.");
		int testIndex = 29;
		double[] result = nn.process(Utils.normalize(data[testIndex], 0, 1));
		System.out.println("It's " + names[Utils.getMaxIndex(result)]);
		for (int i = 0; i < result.length; i++){
		 System.out.println(result[i]);
		}
		System.out.println(Utils.getMaxIndex(result) + 1);
	}
	
	private void parse(String directory) throws Exception{
		File[] images = new File(directory).listFiles();
		System.out.println("Processing " + images.length + " images.");
		data = new double[images.length][];
		expected = new double[images.length][];
		
		BufferedImage image = null;
		
		for (int i = 0; i < images.length; i++) {
			double[] expectVector = new double[NUM_CATEGORIES]; 
			expected[i] = expectVector;
			expectVector[i / 8] = 1;
			
			image = ImageIO.read(images[i]);
			double[] pixels = new double[image.getWidth() * image.getHeight()];
			int index = 0;
			
			System.out.println(i +  ". W:" + image.getWidth() + " H:" + image.getHeight());
			
			for (int x = 0; x < image.getWidth(); x++){
				for (int y = 0; y < image.getHeight(); y++){
					int rgb = image.getRGB(y, x);
					int r = (rgb >> 16) & 0xFF;
					int g = (rgb >> 8) & 0xFF;
					int b = (rgb & 0xFF);

					double colour = (r + g + b) / 3;
					pixels[index] = colour; 
					index++;
				}
			}
			
			
			data[i] = pixels;
		}
	}
	
	public static void main(String[] args) throws Exception{
		new ImageClassifier("res/");
	}
}