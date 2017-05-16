package jneuralnetwork.data;

import java.util.List;

public interface DataReader {
	
	/**
	 * Read training data.
	 */
	public List<RObject> readTrainObjects(int count) throws Exception; 
	
	/**
	 * Read objects to make predictions on.
	 */
	public List<RObject> readToPredictObjects() throws Exception; 
	
	/**
	 * Find an object by id.
	 */
	public Object findById(String id) throws Exception;
}
