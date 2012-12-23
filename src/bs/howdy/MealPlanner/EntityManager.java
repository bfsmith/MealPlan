package bs.howdy.MealPlanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bs.howdy.MealPlanner.Entities.MainDish;
import bs.howdy.MealPlanner.Entities.MealDay;
import bs.howdy.MealPlanner.Entities.SideDish;

public class EntityManager {
	
	private EntityManager() {
		MainDishes = new MainDishService();
		SideDishes = new SideDishService();
		MealDays = new MealDayService();
		
		populateTestData();
	}
	
	private static EntityManager _instance;
	public static EntityManager Instance()
	{
		if(_instance == null)
			_instance = new EntityManager();
		return _instance;
	}
	
	public MainDishService MainDishes;
	public SideDishService SideDishes;
	public MealDayService MealDays;
	
	private void populateTestData() {
		for(int i = 0; i < 5; i++) {
			MainDishes.add(new MainDish(i, "Main Dish " + i, "Description " + i));
		}
		for(int i = 0; i < 5; i++) {
			SideDishes.add(new SideDish(i, "Side Dish " + i, "Description " + i));
		}
		
		for(int i = 0; i < 5; i++) {
			MealDay md = new MealDay(2012, 12, i+1);
			md.setMainDish(MainDishes.get(((i) % MainDishes.getDishes().size())));
			md.addSideDish(SideDishes.getSideDish(((i) % SideDishes.getDishes().size())));
			md.addSideDish(SideDishes.getSideDish(((i+1) % SideDishes.getDishes().size())));
			md.addSideDish(SideDishes.getSideDish(((i+2) % SideDishes.getDishes().size())));
			MealDays.addMealDay(md);
		}
	}
	
	public class MainDishService {
		private List<MainDish> _mainDishes;
		public MainDishService() {
			_mainDishes = new ArrayList<MainDish>();
		}
		
		public MainDish get(int id) {
			for(MainDish dish : _mainDishes) {
				if(dish.getId() == id)
					return dish;
			}
			return null;
		}
		public List<MainDish> getDishes()
		{
			return _mainDishes;
		}
		public void add(MainDish dish) {
			_mainDishes.add(dish);
		}
		public void delete(MainDish dish) {
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
	}
	
	public class SideDishService {
		private List<SideDish> _sideDishes;
		public SideDishService() {
			_sideDishes = new ArrayList<SideDish>();
		}
		
		public SideDish getSideDish(int id) {
			for(SideDish dish : _sideDishes) {
				if(dish.getId() == id)
					return dish;
			}
			return null;
		}
		public List<SideDish> getDishes()
		{
			return _sideDishes;
		}
		public void add(SideDish dish) {
			_sideDishes.add(dish);
		}
		public void delete(SideDish dish) {
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
	}
	
	public class MealDayService {
		private Map<String, MealDay> _mealDays;
		public MealDayService() {
			_mealDays = new HashMap<String, MealDay>();
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
	}
}
