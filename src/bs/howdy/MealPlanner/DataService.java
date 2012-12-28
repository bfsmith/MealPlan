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
		manager = new EntityManager();
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
	
	public void Test() {
		EntityManager manager = new EntityManager();
		MainDish md1 = new MainDish("Main1", "MainDesc1");
		manager.MainDishes.add(md1);
		SideDish sd1 = new SideDish("Side1", "SideDesc1");
		SideDish sd2 = new SideDish("Side2", "SideDesc2");
		manager.SideDishes.add(sd1);
		manager.SideDishes.add(sd2);
		MealDay md = new MealDay(2012, 12, 27, manager);
		md.setMainDish(md1);
		md.addSideDish(sd1);
		md.addSideDish(sd2);
		manager.MealDays.addMealDay(md);
		String xml = xstream.toXML(manager);
		EntityManager manager2 = (EntityManager)xstream.fromXML(xml);
		if(manager.MealDays.getMealDays().size() != manager2.MealDays.getMealDays().size())
			System.out.println("NOT EQUAL");
		else
			System.out.println("EQUAL");
	}
}
