package bs.howdy.MealPlanner;

import com.thoughtworks.xstream.*;
import com.thoughtworks.xstream.io.xml.*;

import bs.howdy.MealPlanner.Entities.*;

public class DataService {
	private EntityManager _manager;
	private String file = "data.dat";
	XStream xstream;
	
	public DataService() {
		_manager = EntityManager.Instance();
		xstream = new XStream(new StaxDriver());
	}
	
	public void Import() {
		
	}
	
	public void Export() {
		
		xstream.toXML(_manager.MainDishes.getDishes(), System.out);
	}
	
	
}
