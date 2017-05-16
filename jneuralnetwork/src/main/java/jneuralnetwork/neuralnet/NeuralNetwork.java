package jneuralnetwork.neuralnet;

import java.util.List;

import jneuralnetwork.data.RObject;

public interface NeuralNetwork {

	/**
	 * Load a list of objects as a data frame.
	 */
	public void loadDataset(List<RObject> list) throws Exception;
	
	/**
	 * Specify a subset of the dataset as a training set.
	 */
	public void loadTrainset(Integer start, Integer end) throws Exception;
	
	/**
	 * Create a neural network with the given parameters.
	 */
	public void createNeuralNet() throws Exception;
	
	/**
	 * Get error value of the neural network
	 */
	public double getNeuralNetError() throws Exception;
	
	/**
	 * Make a prediction using the neural network
	 */
	public double predict(RObject input) throws Exception;
	
	/**
	 * Save the neural network to a png file.
	 */
	public void saveNetToPng(String path) throws Exception;
	
	/**
	 * Close the neural network
	 */
	public void close() throws Exception;
}
