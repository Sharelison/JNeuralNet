package jneuralnetwork;

import java.util.List;
import java.util.logging.Logger;

import jneuralnetwork.data.DataReader;
import jneuralnetwork.data.RObject;
import jneuralnetwork.neuralnet.Forecaster;
import jneuralnetwork.neuralnet.NeuralNetwork;

/**
 * Starter class
 */
public class NeuralAnalyzer {
	private DataReader reader;
	private NeuralNetwork net;
	private String netSavePath;
	private Forecaster forecaster;
	
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public NeuralAnalyzer(DataReader reader, NeuralNetwork net, String netSavePath, Forecaster forecaster) {
		this.reader = reader;
		this.net = net;
		this.netSavePath = netSavePath;
		this.forecaster = forecaster;
	}
	
	/**
	 * Start application, create neuralnet and use forecaster to make predictions on DataReader.readToPredictObjects list.
	 */
	public void start(int trainingInputCount) throws Exception {
		List<RObject> input = reader.readTrainObjects(trainingInputCount);
		List<RObject> toPredictObjects = reader.readToPredictObjects();
		if(input.size() > 1) {
			net.loadDataset(input);
			net.loadTrainset(1, input.size()-1);
			net.createNeuralNet();
			net.saveNetToPng(netSavePath);
			makePredictions(toPredictObjects);
		} else{
			LOGGER.warning("Not enough trainingdata to perform operation. List must contain at least 2 elements");
		}
	}
	
	protected void makePredictions(List<RObject> toPredictObjects) throws Exception {
		LOGGER.info("Making predictions");
		for(RObject r : toPredictObjects) {
			forecaster.predict(net, r);
		}
		LOGGER.info("Done making predictions");
	}
	
	public void close() throws Exception{
		net.close();
	}
}
