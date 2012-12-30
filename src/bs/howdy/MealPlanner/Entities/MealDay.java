package bs.howdy.MealPlanner.Entities;

import java.util.ArrayList;
import java.util.List;

import bs.howdy.MealPlanner.EntityManager;

public class MealDay {
	private EntityManager _manager;
	
	public MealDay(int year, int month, int day, EntityManager manager) {
		this(-1, year, month, day, manager);
	}
	public MealDay(int id, int year, int month, int day, EntityManager manager)
	{
		_manager = manager;
		_mainDishId = -1;
		_sideDishIds = new ArrayList<Integer>();
		_id = id;
		_year = year;
		_month = month;
		_day = day;
	}
	
	private int _id;
	private int _year;
	private int _month;
	private int _day;

	public int getId() {
		return _id;
	}
	public void setId(int id) {
		_id = id;
	}
	public int getYear() {
		return _year;
	}
	public int getMonth() {
		return _month;
	}
	public int getDay() {
		return _day;
	}

	private int _mainDishId;	
	public MainDish getMainDish() {
		return _manager.MainDishes.get(_mainDishId);
	}
	public void setMainDish(MainDish mainDish) {
		if(mainDish != null)
			_mainDishId = mainDish.getId();
		else
			_mainDishId = -1;
	}
	
	private List<Integer> _sideDishIds;
	public List<SideDish> getSideDishes() {
		ArrayList<SideDish> dishes = new ArrayList<SideDish>();
		for(Integer id : _sideDishIds) {
			dishes.add(_manager.SideDishes.getSideDish(id));
		}
		return dishes;
	}
	public void addSideDish(SideDish sideDish) {
		for(Integer sd : _sideDishIds) {
			if(sd == sideDish.getId())
				return;
		}
		_sideDishIds.add(sideDish.getId());
	}
	public void removeSideDish(SideDish sideDish) {
		int index = -1;
		for(int i = 0; i < _sideDishIds.size(); i++) {
			if(_sideDishIds.get(i) == sideDish.getId())
			{
				index = i;
				break;
			}
		}
		if(index >= 0)
			_sideDishIds.remove(index);
	}
}
