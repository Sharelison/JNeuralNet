package jneuralnetwork.neuralnet;

import java.lang.reflect.Field;
import java.util.HashMap;

import jneuralnetwork.data.RObject;
import jneuralnetwork.neuralnet.annotations.Input;
import jneuralnetwork.neuralnet.annotations.Output;

public class NeuralAnnotationReader {

	public static HashMap<String, Double> getNeuralInput(RObject object) throws IllegalArgumentException, IllegalAccessException, NeuralAnnotationException {
		HashMap<String, Double> input = new HashMap<String, Double>();
		Field[] fields = object.getClass().getDeclaredFields();
		for(Field field : fields){
			if(field.isAnnotationPresent(Input.class)) {
				field.setAccessible(true);
				input.put(field.getName(), new Double(field.getDouble(object)));
			}
		}
		if(input.size() == 0) 
			throw new NeuralAnnotationException("Neuralnet input fields missing, plese use the annotation @Input for the input fields in class " + object.getClass().getName());
		return input;
	}
	
	public static HashMap<String, Double> getNeuralOutput(RObject object) throws IllegalArgumentException, IllegalAccessException, NeuralAnnotationException {
		HashMap<String, Double> output = new HashMap<String, Double>();
		Field[] fields = object.getClass().getDeclaredFields();
		for(Field field : fields){
			if(field.isAnnotationPresent(Output.class)) {
				field.setAccessible(true);
				output.put(field.getName(), new Double(field.getDouble(object)));
			}
		}
		if(output.size() == 0) 
			throw new NeuralAnnotationException("Neuralnet output field missing, plese use the annotation @Output for the output field in class " + object.getClass().getName());
		return output;
	}
	
	public static HashMap<String, Double> getDataFrame(RObject object) throws IllegalArgumentException, IllegalAccessException, NeuralAnnotationException {
		HashMap<String, Double> output = new HashMap<String, Double>();
		Field[] fields = object.getClass().getDeclaredFields();
		for(Field field : fields){
			if(field.isAnnotationPresent(Output.class) || field.isAnnotationPresent(Input.class)) {
				field.setAccessible(true);
				output.put(field.getName(), new Double(field.getDouble(object)));
			}
		}
		if(output.size() == 0) 
			throw new NeuralAnnotationException("Neuralnet input fields or output field is missing, please use the annotations @Input/@Output for the input fields & output field in class " + object.getClass().getName());
		return output;
	}
}
