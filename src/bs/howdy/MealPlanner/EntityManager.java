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
		MealDays = new MealDayService();
		db = new Database(this);
		ColorPreferences = new ColorPreferences(db);
		_cache = new Cache();
	}
	
	public MainDishService MainDishes;
	public SideDishService SideDishes;
	public MealDayService MealDays;
	public ColorPreferences ColorPreferences;
	private Database db;
	private Cache _cache;

	public void delete(MainDish dish) {
		db.deleteMainDish(dish);
	}

	public void delete(SideDish dish) {
		db.deleteSideDish(dish);
	}
	
	public void populateTestData() {
		for(int i = 0; i < 5; i++) {
			MainDishes.add(new MainDish("Main Dish " + i, "Description " + i));
		}
		for(int i = 0; i < 5; i++) {
			SideDishes.add(new SideDish("Side Dish " + i, "Description " + i));
		}
		List<SideDish> sds = SideDishes.getSideDishes();
		
		for(int i = 0; i <= 5; i++) {
			MealDay md = new MealDay(2012, 12, i+1, this);
			md.setMainDish(MainDishes.get(((i) % MainDishes.getDishes().size())));
			md.addSideDish(sds.get(i % sds.size()));
			md.addSideDish(sds.get((i+1) % sds.size()));
			md.addSideDish(sds.get((i+2) % sds.size()));
			MealDays.addUpdateMealDay(md);
		}
	}
	
	/** BEGIN MAIN DISHES **/
	
	public MainDish get(int id) {
		MainDish md = getMainDishFromCache(id);
		if(md == null ) {
			md = db.getMainDish(id);
			putMainDishIntoCache(md);
		}
		return md;
	}
	public List<MainDish> getDishes()
	{
		List<MainDish> md = getMainDishListFromCache();
		if(md == null ) {
			md = db.getMainDishes();
			putMainDishListIntoCache(md);
		}
		return md;
	}
	public void add(MainDish dish) {
		db.addMainDish(dish);
		putMainDishIntoCache(dish);
		expireMainDishList();
	}
	public void update(MainDish dish) {
		db.updateMainDish(dish);
		putMainDishIntoCache(dish);
		expireMainDishList();
	}
	
	private MainDish getMainDishFromCache(int id) {
		return (MainDish)_cache.get(getMainDishCacheKey(id));
	}
	private void putMainDishIntoCache(MainDish md) {
		if(md != null)
			_cache.put(getMainDishCacheKey(md.getId()), md);
	}
	private String getMainDishCacheKey(int id) {
		return "MainDish:" + id;
	}
	private List<MainDish> getMainDishListFromCache() {
		return (List<MainDish>)_cache.get(getMainDishListCacheKey());
	}
	private void putMainDishListIntoCache(List<MainDish> md) {
		if(md != null)
			_cache.put(getMainDishListCacheKey(), md);
	}
	private void expireMainDishList() {
		_cache.remove(getMainDishListCacheKey());
	}
	private String getMainDishListCacheKey() {
		return "MainDish:list";
	}
	
	/** END MAIN DISHES **/
	
	/** BEGIN SIDE DISHES **/
	public SideDish getSideDish(int id) {
		SideDish sd = getSideDishFromCache(id);
		if(sd == null ) {
			sd = db.getSideDish(id);
			putSideDishIntoCache(sd);
		}
		return sd;
	}
	public List<SideDish> getSideDishes()
	{
		List<SideDish> sd = getSideDishListFromCache();
		if(sd == null ) {
			sd = db.getSideDishes();
			putSideDishListIntoCache(sd);
		}
		return sd;
	}
	public void add(SideDish dish) {
		db.addSideDish(dish);
		putSideDishIntoCache(dish);
		expireSideDishList();
	}
	public void update(SideDish dish) {
		db.updateSideDish(dish);
		putSideDishIntoCache(dish);
		expireSideDishList();
	}
	
	private SideDish getSideDishFromCache(int id) {
		return (SideDish)_cache.get(getSideDishCacheKey(id)));
	}
	private void putSideDishIntoCache(SideDish md) {
		if(md != null)
			_cache.put(getSideDishCacheKey(md.getId()), md);
	}
	private String getSideDishCacheKey(int id) {
		return "SideDish:" + id;
	}
	private List<SideDish> getSideDishListFromCache() {
		return (List<SideDish>)_cache.get(getSideDishListCacheKey());
	}
	private void putSideDishListIntoCache(List<SideDish> md) {
		if(md != null)
			_cache.put(getSideDishListCacheKey(), md);
	}
	private void expireSideDishList() {
		_cache.remove(getSideDishListCacheKey());
	}
	private String getSideDishListCacheKey() {
		return "SideDish:list";
	}
	
	/** END SIDE DISHES **/
	
	/** BEGIN MEAL DAYS **/
	
	public class MealDayService {
		private Map<String, MealDay> _mealDays;
		
		public MealDayService() {
			_mealDays = new HashMap<String, MealDay>();
		}
		
		public MealDay getMealDay(int year, int month, int day) {
			return db.getMealDay(year, month, day);
		}
		public void addUpdateMealDay(MealDay mealDay) {
			if(mealDay.getId() > 0)
				db.updateMealDay(mealDay);
			else
				db.addMealDay(mealDay);
		}
		
		private MealDay getMealDayFromCache(int year, int month, int day) {
			return (MealDay)_cache.get(getMealDayCacheKey(year, month, day));
		}
		private void putMealDayIntoCache(MealDay md) {
			if(md != null)
				_cache.put(getMealDayCacheKey(md.getYear(), md.getMonth(), md.getDay()), md);
		}
		private String getMealDayCacheKey(int year, int month, int day) {
			return "MealDay:" + year + ":" + month + ":" + day;
		}
	}

	/** END MEAL DAYS **/
	
	public class ColorPreferences {
		private Database _db;
		
		public ColorPreferences(Database db) {
			_db = db;
		}
		
		public Color getColor(String name) {
			return _db.getColor(name);
		}
		public void setColor(String name, Color color) {
			_db.setColorPreference(name, color);
		}
		public void restoreDefaultColors() {
			_db.restoreColorPreferences();
		}
	}
}
