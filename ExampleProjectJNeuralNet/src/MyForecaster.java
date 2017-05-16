import jneuralnetwork.data.RObject;
import jneuralnetwork.neuralnet.Forecaster;
import jneuralnetwork.neuralnet.NeuralNetwork;


public class MyForecaster implements Forecaster{

	//make prediction by calling net.predict and do wathever you want with it.
	@Override
	public void predict(NeuralNetwork net, RObject obj) throws Exception {
		System.out.println("prediction - MyForecast..\n " + Math.round(net.predict(obj))+ " for object with id : " + obj.getId());
	}

}
