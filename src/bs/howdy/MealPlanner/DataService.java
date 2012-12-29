package bs.howdy.MealPlanner;

import java.io.*;

import bs.howdy.MealPlanner.Entities.*;

import com.thoughtworks.xstream.*;
import com.thoughtworks.xstream.io.xml.*;

public class DataService {
	private String file = "data1.dat";
	XStream xstream;
	
	public DataService() {
		xstream = new XStream(new StaxDriver());
	}
	
	public EntityManager Import() {
		EntityManager manager = null;
		if(new File(file).exists()) {
			try{ 
				FileReader fstream = new FileReader(file);
				manager = (EntityManager)xstream.fromXML(fstream);
				fstream.close();
			} catch (Exception e){//Catch exception if any
				  System.err.println("Error: " + e.getMessage());
			}
		}
		if(manager == null) {
			manager = new EntityManager();
		}
		return manager;
	}
	
	public void Export(EntityManager manager) {
		manager.MealDays.cleanup();
		try{ 
			FileWriter fstream = new FileWriter(file);
			BufferedWriter out = new BufferedWriter(fstream);
			xstream.toXML(manager, out);
			out.close();
		} catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
}
