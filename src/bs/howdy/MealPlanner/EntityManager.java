package bs.howdy.MealPlanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bs.howdy.MealPlanner.Entities.MainDish;
import bs.howdy.MealPlanner.Entities.MealDay;
import bs.howdy.MealPlanner.Entities.SideDish;

public class EntityManager {
	private Map<String, MealDay> _mealDays;
	private List<MainDish> _mainDishes;
	private List<SideDish> _sideDishes;
	
	private EntityManager() {
		_mealDays = new HashMap<String, MealDay>();
		_mainDishes = new ArrayList<MainDish>();
		_sideDishes = new ArrayList<SideDish>();
		
		populateTestData();
	}
	
	private static EntityManager _instance;
	public static EntityManager Instance()
	{
		if(_instance == null)
			_instance = new EntityManager();
		return _instance;
	}
	
	public List<MainDish> getMainDishes()
	{
		return _mainDishes;
	}
	public void addMainDish(MainDish dish) {
		_mainDishes.add(dish);
	}
	public void deleteMainDish(MainDish dish) {
		int index = -1;
		for(int i = 0; i < _mainDishes.size(); i++) {
			if(_mainDishes.get(i).getId() == dish.getId()) {
				index = i;
				break;
			}
		}
		if(index >= 0)
			_mainDishes.remove(index);
	}
	
	public List<SideDish> getSideDishes()
	{
		return _sideDishes;
	}
	public void addSideDish(SideDish dish) {
		_sideDishes.add(dish);
	}
	public void deleteSideDish(SideDish dish) {
		int index = -1;
		for(int i = 0; i < _sideDishes.size(); i++) {
			if(_sideDishes.get(i).getId() == dish.getId()) {
				index = i;
				break;
			}
		}
		if(index >= 0)
			_sideDishes.remove(index);
	}
	
	public MealDay getMealDay(int year, int month, int day) {
		return _mealDays.get(createKey(year, month, day));
	}
	public void addMealDay(MealDay mealDay) {
		_mealDays.put(createKey(mealDay.getYear(), mealDay.getMonth(), mealDay.getDay()), mealDay);
	}
	
	private String createKey(int year, int month, int day) {
		return "" + year + month + day;
	}
	
	private void populateTestData() {
		for(int i = 1; i <= 5; i++) {
			_mainDishes.add(new MainDish(i, "Main Dish " + i, "Description " + i));
		}
		for(int i = 1; i <= 5; i++) {
			_sideDishes.add(new SideDish(i, "Side Dish " + i, "Description " + i));
		}
		
		for(int i = 1; i < 5; i++) {
			MealDay md = new MealDay(2012, 12, i);
			md.setMainDish(_mainDishes.get((i-1) % _mainDishes.size()));
			md.addSideDish(_sideDishes.get((i) % _sideDishes.size()));
			md.addSideDish(_sideDishes.get((i+1) % _sideDishes.size()));
			md.addSideDish(_sideDishes.get((i+2) % _sideDishes.size()));
			addMealDay(md);
		}
	}
}
