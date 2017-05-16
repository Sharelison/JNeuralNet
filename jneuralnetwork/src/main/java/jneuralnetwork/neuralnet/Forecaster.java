package jneuralnetwork.neuralnet;

import jneuralnetwork.data.RObject;


public interface Forecaster {

	/**
	 * Make a prediction and do something with it.
	 */
	public void predict(NeuralNetwork net, RObject object) throws Exception;

}
