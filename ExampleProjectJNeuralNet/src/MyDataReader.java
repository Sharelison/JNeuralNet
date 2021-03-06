import java.util.ArrayList;
import java.util.List;

import jneuralnetwork.data.DataReader;
import jneuralnetwork.data.RObject;


public class MyDataReader implements DataReader{

	//data
	RObject r = new MyRObject("first", 0, 1, 0);
	RObject r2 = new MyRObject("second", 1, 0,1);
	RObject r3 = new MyRObject("third",  0,0, 0);
	RObject r4 = new MyRObject("fourth", 1, 1, 0);
	
	//This is my dataset it can be from whatever source you want
	List<RObject> list = new ArrayList<RObject>();
	
	public MyDataReader() 
	{}
		
	//returns an object by id, to keep it simple let's just return the object at that index.
	@Override
	public Object findById(String index) throws Exception {
		return list.get(Integer.parseInt(index));
	}

	//Just to keep it simple, returns same dataset to make predictions on. 
	@Override
	public List<RObject> readToPredictObjects() throws Exception {
		return list;
	}

	//Returns trainning dataset, add items to list. 
	@Override
	public List<RObject> readTrainObjects(int arg0) throws Exception {
		list.add(r);
		list.add(r2);
		list.add(r3);
		list.add(r4);
		return list;
	}
}
