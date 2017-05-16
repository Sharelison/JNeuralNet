package jneuralnetwork.neuralnet;

public class NeuralAnnotationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private String message;
	
	public NeuralAnnotationException(String message){
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
