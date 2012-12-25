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

	public void delete(MainDish dish) {
		if(dish == null) return;
		List<MainDish> dishes = MainDishes.getDishes();
		int index = -1;
		for(int i = 0; i < dishes.size(); i++) {
			if(dishes.get(i).getId() == dish.getId()) {
				index = i;
				break;
			}
		}
		if(index >= 0) {
			MealDays.removeAll(dish);
			dishes.remove(index);
		}
	}

	public void delete(SideDish dish) {
		if(dish == null) return;
		List<SideDish> dishes = SideDishes.getDishes();
		int index = -1;
		for(int i = 0; i < dishes.size(); i++) {
			if(dishes.get(i).getId() == dish.getId()) {
				index = i;
				break;
			}
		}
		if(index >= 0) {
			MealDays.removeAll(dish);
			dishes.remove(index);
		}
	}
	
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
			md.addSideDish(SideDishes.get(((i) % SideDishes.getDishes().size())));
			md.addSideDish(SideDishes.get(((i+1) % SideDishes.getDishes().size())));
			md.addSideDish(SideDishes.get(((i+2) % SideDishes.getDishes().size())));
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
	}
	
	public class SideDishService {
		private List<SideDish> _sideDishes;
		public SideDishService() {
			_sideDishes = new ArrayList<SideDish>();
		}
		
		public SideDish get(int id) {
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
		
		public void removeAll(MainDish dish) {
			for(String key : _mealDays.keySet()) {
				MealDay md = _mealDays.get(key);
				if(md != null) {
					MainDish mainDish = md.getMainDish();
					if(mainDish != null && mainDish.getId() == dish.getId()) {
						md.setMainDish(null);
					}
				}
			}
		}

		public void removeAll(SideDish dish) {
			for(String key : _mealDays.keySet()) {
				MealDay md = _mealDays.get(key);
				if(md != null) {
					List<SideDish> sideDishes = md.getSideDishes();
					if(sideDishes != null) {
						int index = -1;
						for(int i = 0; i < sideDishes.size(); i++) {
							SideDish sideDish = sideDishes.get(i);
							if(sideDish.getId() == dish.getId()) {
								index = i;
								break;
							}
						}
						if(index >= 0) {
							sideDishes.remove(index);
						}
					}
				}
			}
		}
		
	}
}
