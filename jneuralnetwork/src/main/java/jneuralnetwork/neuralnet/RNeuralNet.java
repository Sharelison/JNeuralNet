package jneuralnetwork.neuralnet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import jneuralnetwork.data.RObject;
import jneuralnetwork.rconnect.RClient;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RserveException;

/**
 * Default neural network implementation of the Neural network interface. Contains all operations in the NeuralNetwork interface. 
 */
public class RNeuralNet implements NeuralNetwork{
	
	private final String dataset = "dataset";
	private final String trainset = "trainset";
	private final String net = "customnet";
	private final String predictionVar = "prediction_variable";
	
	private boolean datasetLoaded = false;
	private boolean trainsetLoaded = false;
	
	private Double threshold = 0.1;
	private Boolean linearOutput = false;
	private String lifeSign = "minimal";
	private Integer[] hiddenLayers = new Integer[]{};
	
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	private final RClient client;
	
	private RObject sampleObject;
	
	public RNeuralNet() throws RserveException{
		super();	
		client = new RClient();
		client.eval("library(neuralnet)");
	}
	
	/**
	 * standard threshold = 0.1
	 * standard no hidden layers
	 * standard linear output = false
	 * standard life sign = minimal
	 * @param threshold
	 * @param hiddenLayers
	 * @param linearOutput
	 * @param lifeSign
	 * @throws RserveException
	 */
	public RNeuralNet(Double threshold, Integer[] hiddenLayers, Boolean linearOutput, String lifeSign) throws RserveException{
		client = new RClient();
		client.eval("library(neuralnet)");
		this.threshold = threshold;
		this.hiddenLayers = hiddenLayers;
		this.linearOutput = linearOutput;
		this.lifeSign = lifeSign;
	}
	public void loadDataset(List<RObject> list) throws RserveException, REXPMismatchException, IllegalArgumentException, IllegalAccessException, NeuralAnnotationException {
		if(list.size() > 0) {
			LOGGER.info("loading dataset in R");
			sampleObject = list.get(0);
			client.createDataFrame(dataset, list);
			LOGGER.info("finished loading dataset in R");	
			datasetLoaded = true;
		} else{
			LOGGER.warning("dataset contains no elements!");
		}
	}
	
	public void loadTrainset(Integer start, Integer end) throws REngineException, REXPMismatchException {
		if(datasetLoaded) {
			client.eval(trainset + " <- "+dataset+"[" + start +":"+end+", ]");
			LOGGER.info("created trainset");	
			trainsetLoaded = true;
		} else {
			LOGGER.warning("dataset not loaded, please load dataset before continuing!");
		}
	}
	
	private String hiddenLayersToString(Integer[] hidden) {
		String hiddenLyr = "c(";
		if(hidden.length > 0) {
			boolean first = true;
			for(Integer hid : hidden){
				if(first){
					hiddenLyr = hiddenLyr + hid;
					first = false;
				} else{
					hiddenLyr = hiddenLyr+","+hid;
				}
			}	
			hiddenLyr += ")";
		} else {
			hiddenLyr = "0";
		}

		
		return hiddenLyr;
	}
	
	private String creatInputAndOutputParam(Set<String> inpColumns, Set<String> outColumns){
		String params = "";
		boolean outfirst = true;
		for(String outputName : outColumns) {
			if(outfirst) {
				params = outputName;
				outfirst = false;
			} else {
				params += " + " + outputName;
			}
		}
		boolean infirst = true;
		for(String inputName : inpColumns) {
			if(infirst) {
				params += " ~ " + inputName;
				infirst = false;
			} else {
				params += " + " + inputName; 		
			}
		}
		return params;
	}
	
	public void createNeuralNet() throws REngineException, REXPMismatchException, IllegalArgumentException, IllegalAccessException, NeuralAnnotationException {
		if(trainsetLoaded) {
			LOGGER.info("creating neuralnet");
			Set<String> inpColumns = NeuralAnnotationReader.getNeuralInput(sampleObject).keySet();
			Set<String> outColumns = NeuralAnnotationReader.getNeuralOutput(sampleObject).keySet();
			String params = creatInputAndOutputParam(inpColumns, outColumns);
			String linear = linearOutput ? "TRUE" : "FALSE";
			String hidLayers = hiddenLayersToString(hiddenLayers);
			params = params + ", " + trainset + ", hidden = " + hidLayers + ", " + "lifesign = '"+lifeSign+"', linear.output = "+linear+", threshold = "+threshold.toString();
			String exec =  net + " <- neuralnet(" + params + ")";
			executeHandleRError(exec, "Created Neural network successfully");	
		} else{
			LOGGER.warning("Dataset or trainset not loaded, please make sure load dataset and trainset before proceeding.");
		}
	}
	
	private void executeHandleRError(String cmd, String successMessage) throws REXPMismatchException, REngineException {
		REXP rexecute = client.parseAndEval("try(" + cmd +", silent=TRUE)");
		if (!rexecute.inherits("try-error")) {
			if(!successMessage.isEmpty())
				LOGGER.info(successMessage);
		} else {
			LOGGER.severe("ERROR: " + rexecute.asString());
		}		
	}
	
	public double predict(RObject input) throws REXPMismatchException, REngineException, IllegalArgumentException, IllegalAccessException, NeuralAnnotationException {
		List<HashMap<String,Double>> objList = new ArrayList<HashMap<String,Double>>();
		objList.add(NeuralAnnotationReader.getNeuralInput(input));	
		client.createHashDataFrame(predictionVar, objList);
		String res = "result";
		String params = net + ", " + predictionVar;
		String exec = res + " <- compute(" + params + ")";
		executeHandleRError(exec, "");
		double answer = client.eval(res+"$net.result[1]").asDouble();		
		return answer;		
	}
	
	public void saveNetToPng(String path) throws REXPMismatchException, REngineException {
		LOGGER.info("plotting neural network");
		client.eval("png(file = '" + path + "')");
		executeHandleRError("plot("+net+", rep='best')", "saved picture success");
		client.eval("dev.off()");
	}
	

	public double getNeuralNetError() throws Exception {
		String exec = net+"$result.matrix[1]";
		double error = client.parseAndEval(exec).asDouble();
		return error;
	}
	
	public void close() throws Exception{
		LOGGER.info("closed Rserve");
		String exec = "rm(" + dataset + "," + trainset + "," + net + "," + predictionVar + ")";
		client.eval(exec);
		client.closeConnection();
	}
}
