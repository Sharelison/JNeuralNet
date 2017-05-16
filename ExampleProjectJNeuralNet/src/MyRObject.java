import jneuralnetwork.data.RObject;
import jneuralnetwork.neuralnet.annotations.Input;
import jneuralnetwork.neuralnet.annotations.Output;


public class MyRObject implements RObject{

	//@Input is your input values 
	//@Output is your output value 
		
	@Input
	private double myfield;
	@Input
	private double myfield2;
	@Output
	private double answer;

	private String id;
	
	public MyRObject(String id, double m, double m2, double ans) {
		myfield=m;
		myfield2=m2;
		answer=ans;
		this.id = id;
	}
		
	public double getMyfield() {
		return myfield;
	}

	public void setMyfield(double myfield) {
		this.myfield = myfield;
	}

	public double getMyfield2() {
		return myfield2;
	}

	public void setMyfield2(double myfield2) {
		this.myfield2 = myfield2;
	}

	public double getAnswer() {
		return answer;
	}

	public void setAnswer(double answer) {
		this.answer = answer;
	}

	@Override
	public String getId() {
		return id;
	}

}
