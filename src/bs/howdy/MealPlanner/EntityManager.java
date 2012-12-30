package bs.howdy.MealPlanner;

import java.awt.Color;
import java.util.*;

import bs.howdy.MealPlanner.Entities.*;

public class EntityManager {
	
	public EntityManager() {
		db = new Database(this);
		_cache = new Cache();
	}
	
	private Database db;
	private Cache _cache;

	public void populateTestData() {
		for(int i = 0; i < 5; i++) {
			add(new MainDish("Main Dish " + i, "Description " + i));
		}
		for(int i = 0; i < 5; i++) {
			add(new SideDish("Side Dish " + i, "Description " + i));
		}
		List<SideDish> sds = getSideDishes();
		
		for(int i = 0; i <= 5; i++) {
			MealDay md = new MealDay(2012, 12, i+1, this);
			md.setMainDish(getMainDish(((i) % getMainDishes().size())));
			md.addSideDish(sds.get(i % sds.size()));
			md.addSideDish(sds.get((i+1) % sds.size()));
			md.addSideDish(sds.get((i+2) % sds.size()));
			addUpdateMealDay(md);
		}
	}
	
	/** BEGIN MAIN DISHES **/
	
	public MainDish getMainDish(int id) {
		MainDish md = getMainDishFromCache(id);
		if(md == null ) {
			md = db.getMainDish(id);
			putMainDishIntoCache(md);
		}
		return md;
	}
	public List<MainDish> getMainDishes()
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
		expireMealDays();
	}
	public void delete(MainDish dish) {
		db.deleteMainDish(dish);
		_cache.remove(getMainDishCacheKey(dish.getId()));
		expireMainDishList();
		expireMealDays();
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
		expireMealDays();
	}
	public void delete(SideDish dish) {
		db.deleteSideDish(dish);
		_cache.remove(getSideDishCacheKey(dish.getId()));
		expireSideDishList();
		expireMealDays();
	}
	
	private SideDish getSideDishFromCache(int id) {
		return (SideDish)_cache.get(getSideDishCacheKey(id));
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
	public MealDay getMealDay(int year, int month, int day) {
		MealDay md = getMealDayFromCache(year, month, day);
		if(md == null ) {
			md = db.getMealDay(year, month, day);
			putMealDayIntoCache(md);
		}
		return md;
	}
	public void addUpdateMealDay(MealDay mealDay) {
		if(mealDay.getId() > 0)
			db.updateMealDay(mealDay);
		else
			db.addMealDay(mealDay);
		putMealDayIntoCache(mealDay);
	}
	
	private MealDay getMealDayFromCache(int year, int month, int day) {
		return (MealDay)_cache.get(getMealDayCacheKey(year, month, day));
	}
	private void putMealDayIntoCache(MealDay md) {
		if(md != null)
			_cache.put(getMealDayCacheKey(md.getYear(), md.getMonth(), md.getDay()), md);
	}
	private void expireMealDays() {
		_cache.removeAll("MealDay:");
	}
	private String getMealDayCacheKey(int year, int month, int day) {
		return "MealDay:" + year + ":" + month + ":" + day;
	}
	/** END MEAL DAYS **/
	
	/** BEGIN COLOR PREFERENCES **/
	public Color getColor(String name) {
		return db.getColor(name);
	}
	public void setColor(String name, Color color) {
		db.setColorPreference(name, color);
	}
	public void restoreDefaultColors() {
		db.restoreColorPreferences();
	}
	/** END COLOR PREFERENCES **/
}
