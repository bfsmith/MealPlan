package bs.howdy.MealPlanner.Entities;

import java.util.ArrayList;
import java.util.List;

import bs.howdy.MealPlanner.EntityManager;

public class MealDay {
	private EntityManager _manager;
	
	public MealDay(int year, int month, int day, EntityManager manager)
	{
		_manager = manager;
		_mainDishId = -1;
		_sideDishIds = new ArrayList<Integer>();
		_year = year;
		_month = month;
		_day = day;
	}
	
	private int _year;
	private int _month;
	private int _day;
	
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
			dishes.add(_manager.SideDishes.get(id));
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
