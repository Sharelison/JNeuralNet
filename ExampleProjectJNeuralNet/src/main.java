import jneuralnetwork.NeuralAnalyzer;
import jneuralnetwork.neuralnet.RNeuralNet;

public class main {
	public static void main(String[] args) {
		try {
			//Call neuralanalyzer to create & train neuralnet and make predictions.  
			NeuralAnalyzer na = new NeuralAnalyzer(new MyDataReader(),
												   new RNeuralNet(0.0, new Integer[]{}, false, "minimal"), //using the Rneuralnet. Caution! threshold (0.0) is a numeric value specifying the threshold for the partial derivatives of the error function as stopping criteria";
												   "C:/Users/share/Desktop/mylibneuralnet.png", //this is the save filepath of the neuralnetwork picture (change it to whatever you want).
												   new MyForecaster());
			na.start(300);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
