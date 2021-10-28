package ie.gmit.sw.ai.personality;

import java.io.File;
import java.io.IOException;

import org.encog.Encog;
import org.encog.ml.MLRegression;
import org.encog.ml.data.MLData;
import org.encog.ml.data.versatile.NormalizationHelper;
import org.encog.ml.data.versatile.VersatileMLDataSet;
import org.encog.ml.data.versatile.columns.ColumnDefinition;
import org.encog.ml.data.versatile.columns.ColumnType;
import org.encog.ml.data.versatile.sources.CSVDataSource;
import org.encog.ml.data.versatile.sources.VersatileDataSource;
import org.encog.ml.factory.MLMethodFactory;
import org.encog.ml.model.EncogModel;
import org.encog.util.csv.CSVFormat;
import org.encog.util.simple.EncogUtility;

public class EncogNeuralNetwork {

	private static final EncogNeuralNetwork instance = new EncogNeuralNetwork();
	
	private EncogNeuralNetwork() {}
	
	public static EncogNeuralNetwork getInstance() {
		return instance;
	}
	
	private static MLRegression bestMethod;
	private static NormalizationHelper helper;
	
	private static MLRegression getBestMethod() {
		return bestMethod;
	}

	private static void setBestMethod(MLRegression bm) {
		bestMethod = bm;
	}
	
	private static NormalizationHelper getHelper() {
		return helper;
	}

	private static void setHelper(NormalizationHelper h) {
		helper = h;
	}
	

	public static void train() throws IOException {
		String file = "./resources/neural/data.txt"; 
		
		VersatileDataSource  source = new CSVDataSource(new File(file), false, CSVFormat.DECIMAL_POINT);
		VersatileMLDataSet data = new VersatileMLDataSet(source);
		data.defineSourceColumn("hunger", 0, ColumnType.continuous);
		data.defineSourceColumn("fear", 1, ColumnType.continuous);
		data.defineSourceColumn("health", 2, ColumnType.continuous);
		ColumnDefinition out = data.defineSourceColumn("personality", 3, ColumnType.nominal);
		
		data.analyze();
		data.defineSingleOutputOthersInput(out);

		EncogModel model = new EncogModel(data);
		model.selectMethod(data, MLMethodFactory.TYPE_FEEDFORWARD);
		data.normalize();
		
		model.holdBackValidation(0.3, true, 1001);
		model.selectTrainingType(data);
		EncogNeuralNetwork.setBestMethod((MLRegression) model.crossvalidate(10, true));
		System.out.println( "Training error: " + EncogUtility.calculateRegressionError(bestMethod, model.getTrainingDataset()));
		System.out.println( "Validation error: " + EncogUtility.calculateRegressionError(bestMethod, model.getValidationDataset()));

		EncogNeuralNetwork.setHelper(data.getNormHelper());
		Encog.getInstance().shutdown();
	}
	
	
	public static int getClassification(double[] d) throws ClassNotFoundException, IOException {
		String[] line = new String[4];
		MLData input = EncogNeuralNetwork.getHelper().allocateInputVector();
		
		line[0] = Double.toString(d[0]);
		line[1] = Double.toString(d[1]);
		line[2] = Double.toString(d[2]);
		
		helper.normalizeInputVector(line, input.getData(), false);
		MLData output = EncogNeuralNetwork.getBestMethod().compute(input);
		String actual = helper.denormalizeOutputVectorToString(output)[0];

		int classification = 0;
		switch(actual) {
			case "Skittish":
					classification = 0;
				break;
			case "Aggressive":
					classification = 2;
					break;
			default:
			case "Friendly":
					classification = 1;
		}
		Encog.getInstance().shutdown();
		return classification;
		
	}
}
