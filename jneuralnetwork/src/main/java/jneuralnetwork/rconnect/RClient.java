package jneuralnetwork.rconnect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import jneuralnetwork.data.RObject;
import jneuralnetwork.neuralnet.NeuralAnnotationException;
import jneuralnetwork.neuralnet.NeuralAnnotationReader;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

/**
 * A R Client using R serve tha can be used for basic R functions 
 */
public class RClient {
	RConnection connection;
	
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public RClient() throws RserveException{
		connection = new RConnection();
		connection.eval("library(neuralnet)");
		LOGGER.info("started Rserve");
	}

	
	/**
	 * Can only create numeric dataframes
	 */
	public void createHashDataFrame(String frameName, List<HashMap<String, Double>> hashFrame) throws RserveException, IllegalArgumentException, IllegalAccessException, NeuralAnnotationException{
		String args = "";
		if(hashFrame.size()> 0) {
			Set<String> colNames = hashFrame.get(0).keySet();
			boolean first = true;
			for(String col : colNames){
				if(first){
					args = col + " = numeric()";
					first = false;
				} else {
					args += ", " + col + " = numeric()";
				}
			}
			connection.eval(frameName + " <- data.frame("+args+")");
			fillHashDataFrame(frameName, hashFrame);	
		}
	}
	
	private void fillHashDataFrame(String frameName, List<HashMap<String, Double>> hashFrame) throws RserveException, IllegalArgumentException, IllegalAccessException, NeuralAnnotationException {
		for(HashMap<String, Double> dfValues : hashFrame){
			String args2 = "";
			boolean first = true;
			for(Map.Entry<String, Double> entry : dfValues.entrySet()) {
				if(first) {
					args2 = entry.getKey() + " = " + entry.getValue();
					first = false;	
				}
				else{
					args2 += ", " + entry.getKey() + " = " + entry.getValue();					
				}
			}
			connection.eval(frameName + " <- rbind("+frameName+", data.frame("+args2+"))");
		}		
	}
	
	/**
	 * Can only create numeric dataframes
	 */
	public void createDataFrame(String frameName, List<RObject> rObjList) throws RserveException, IllegalArgumentException, IllegalAccessException, NeuralAnnotationException{
		String args = "";
		if(rObjList.size()> 0) {
			Set<String> colNames = NeuralAnnotationReader.getDataFrame(rObjList.get(0)).keySet();
			boolean first = true;
			for(String col : colNames){
				if(first){
					args = col + " = numeric()";
					first = false;
				} else {
					args += ", " + col + " = numeric()";
				}
			}
			connection.eval(frameName + " <- data.frame("+args+")");
			fillDataFrame(frameName, rObjList);	
		}
	}
	
	private void fillDataFrame(String frameName, List<RObject> rObjList) throws RserveException, IllegalArgumentException, IllegalAccessException, NeuralAnnotationException {
		for(RObject obj : rObjList){
			HashMap<String, Double> dfValues = NeuralAnnotationReader.getDataFrame(obj);
			String args2 = "";
			boolean first = true;
			for(Map.Entry<String, Double> entry : dfValues.entrySet()) {
				if(first) {
					args2 = entry.getKey() + " = " + entry.getValue();
					first = false;	
				}
				else{
					args2 += ", " + entry.getKey() + " = " + entry.getValue();					
				}
			}
			connection.eval(frameName + " <- rbind("+frameName+", data.frame("+args2+"))");
		}		
	}
	
	/**
	 * Gets a data frame
	 * @param frameName
	 * @return
	 * @throws REXPMismatchException
	 * @throws REngineException
	 */
	public List<HashMap<String, Double>> getDataFrame(String frameName) throws REXPMismatchException, REngineException {
		@SuppressWarnings("unchecked")
		HashMap<String, double[]> c = (HashMap<String, double[]>) connection.parseAndEval(frameName).asNativeJavaObject();
		List<HashMap<String, Double>> df = new ArrayList<HashMap<String, Double>>();
		int kvCount = getDataframeSize(c);
        for(int i = 0; i < kvCount; i++){
        	HashMap<String, Double> object = new HashMap<String, Double>();
        	for(String key : c.keySet()) {
        		object.put(key, c.get(key)[0]);
        	}
        	df.add(object);
        }
        return df;
	}
	
	public String[] getDataFrameColumnNames(String frameName) throws REXPMismatchException, REngineException {
		@SuppressWarnings("unchecked")
		HashMap<String, double[]> c = (HashMap<String, double[]>) connection.parseAndEval(frameName).asNativeJavaObject();
    	String[] keys = new String[c.size()];
    	int i = 0;
		for(String key : c.keySet()) {
			keys[i] = key;
			i++;
    	}
		return keys;
	}
	
	private int getDataframeSize(HashMap<String, double[]> dataframe) {
		int i = 0;
		for(@SuppressWarnings("unused") Map.Entry<String, double[]> entry : dataframe.entrySet()) {
			i++;
		}
		return i;
	}
	
	public REXP eval(String cmd) throws RserveException {
		return connection.eval(cmd); 
	}
	
	public REXP parseAndEval(String cmd) throws REngineException, REXPMismatchException {
		return connection.parseAndEval(cmd);
	}
	
	public void closeConnection() throws RserveException {
		connection.finalize();
		connection.close();
	}
}
