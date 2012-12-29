package bs.howdy.MealPlanner;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bs.howdy.MealPlanner.Entities.MainDish;
import bs.howdy.MealPlanner.Entities.MealDay;
import bs.howdy.MealPlanner.Entities.SideDish;

public class EntityManager {
	
	public EntityManager() {
		MainDishes = new MainDishService();
		SideDishes = new SideDishService();
		MealDays = new MealDayService();
		ColorPreferences = new ColorPreferences();
		db = new Database(this);
	}
	
	public MainDishService MainDishes;
	public SideDishService SideDishes;
	public MealDayService MealDays;
	public ColorPreferences ColorPreferences;
	private Database db;

	public void delete(MainDish dish) {
		MealDays.removeAll(dish);
		db.deleteMainDish(dish);
//		if(dish == null) return;
//		List<MainDish> dishes = MainDishes.getDishes();
//		int index = -1;
//		for(int i = 0; i < dishes.size(); i++) {
//			if(dishes.get(i).getId() == dish.getId()) {
//				index = i;
//				break;
//			}
//		}
//		if(index >= 0) {
//			MealDays.removeAll(dish);
//			dishes.remove(index);
//		}
	}

	public void delete(SideDish dish) {
		MealDays.removeAll(dish);
		db.deleteSideDish(dish);
//		if(dish == null) return;
//		List<SideDish> dishes = SideDishes.getDishes();
//		int index = -1;
//		for(int i = 0; i < dishes.size(); i++) {
//			if(dishes.get(i).getId() == dish.getId()) {
//				index = i;
//				break;
//			}
//		}
//		if(index >= 0) {
//			MealDays.removeAll(dish);
//			dishes.remove(index);
//		}
	}
	
	public void populateTestData() {
		for(int i = 0; i < 5; i++) {
			MainDishes.add(new MainDish("Main Dish " + i, "Description " + i));
		}
		for(int i = 0; i < 5; i++) {
			SideDishes.add(new SideDish("Side Dish " + i, "Description " + i));
		}
		List<SideDish> sds = SideDishes.getDishes();
		
		for(int i = 0; i <= 5; i++) {
			MealDay md = new MealDay(2012, 12, i+1, this);
			md.setMainDish(MainDishes.get(((i) % MainDishes.getDishes().size())));
			md.addSideDish(sds.get(i % sds.size()));
			md.addSideDish(sds.get((i+1) % sds.size()));
			md.addSideDish(sds.get((i+2) % sds.size()));
			MealDays.addMealDay(md);
		}
	}
	
	public class MainDishService {
		private int _maxId = 1;
		private List<MainDish> _mainDishes;
		
		public MainDishService() {
			_mainDishes = new ArrayList<MainDish>();
		}
		
		public MainDish get(int id) {
			return db.getMainDish(id);
//			for(MainDish dish : _mainDishes) {
//				if(dish.getId() == id)
//					return dish;
//			}
//			return null;
		}
		public List<MainDish> getDishes()
		{
			return db.getMainDishes();
//			return _mainDishes;
		}
		public void add(MainDish dish) {
			db.addMainDish(dish);
//			if(dish.getId() < 0)
//				dish.setId(_maxId++);
//			_mainDishes.add(dish);
		}
	}
	
	public class SideDishService {
		private int _maxId = 1;
		private List<SideDish> _sideDishes;
		
		public SideDishService() {
			_sideDishes = new ArrayList<SideDish>();
		}
		
		public SideDish get(int id) {
			return db.getSideDish(id);
//			for(SideDish dish : _sideDishes) {
//				if(dish.getId() == id)
//					return dish;
//			}
//			return null;
		}
		public List<SideDish> getDishes()
		{
			return db.getSideDishes();
//			return _sideDishes;
		}
		public void add(SideDish dish) {
			db.addSideDish(dish);
//			if(dish.getId() < 0)
//				dish.setId(_maxId++);
//			_sideDishes.add(dish);
		}
	}
	
	public class MealDayService {
		private Map<String, MealDay> _mealDays;
		
		public MealDayService() {
			_mealDays = new HashMap<String, MealDay>();
		}
		
//		public List<MealDay> getMealDays() {
//			return new ArrayList<MealDay>(_mealDays.values());
//		}
		
		public MealDay getMealDay(int year, int month, int day) {
			return db.getMealDay(year, month, day);
//			return _mealDays.get(createKey(year, month, day));
		}
		public void addMealDay(MealDay mealDay) {
			db.addMealDay(mealDay);
//			_mealDays.put(createKey(mealDay.getYear(), mealDay.getMonth(), mealDay.getDay()), mealDay);
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
		
		public void cleanup() {
			ArrayList<String> keysToRemove = new ArrayList<String>();
			for(String key : _mealDays.keySet()) {
				MealDay md = _mealDays.get(key);
				if(md == null) {
					keysToRemove.add(key);
					continue;
				}
				if(md.getMainDish() != null)
					continue;
				if(md.getSideDishes().size() > 0)
					continue;
				keysToRemove.add(key);
			}
			for(String key : keysToRemove)
				_mealDays.remove(key);
		}
	}
	
	public class ColorPreferences {
		public Color nonSelectedMonthBackground;
		public Color MainDishBorder;
		public Color MainDishBackground;
		public Color SideDishBorder;
		public Color SideDishBackground;
		public Color weekendBackground; 
		public Color todayBackground;
		public Color defaultDayBackground;
		public Color defaultDayBorder;
		public Color selectedDayBorder;
		
		public ColorPreferences() {
			restoreDefaults();
		}
		
		public void restoreDefaults() {
			nonSelectedMonthBackground = new Color(220, 220, 220);
			MainDishBorder = Color.BLUE;
			MainDishBackground = new Color(132, 199, 255);
			SideDishBorder = new Color(150, 100, 255);
			SideDishBackground = new Color(230, 200, 255);
			weekendBackground = new Color(255, 220, 220);
			todayBackground = new Color(150, 150, 255);
			defaultDayBackground = new Color(255, 255, 255);
			defaultDayBorder = Color.LIGHT_GRAY;
			selectedDayBorder = Color.DARK_GRAY;
		}
	}
}
