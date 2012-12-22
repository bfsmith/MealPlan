package bs.howdy.MealPlanner.Entities;

import java.util.ArrayList;
import java.util.List;

public class MealDay {
	public MealDay(int year, int month, int day)
	{
		_sideDishes = new ArrayList<SideDish>();
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

	private MainDish _mainDish;	
	public MainDish getMainDish() {
		return _mainDish;
	}
	public void setMainDish(MainDish mainDish) {
		_mainDish = mainDish;
	}
	
	private List<SideDish> _sideDishes;
	public List<SideDish> getSideDishes() {
		return _sideDishes;
	}
	public void addSideDish(SideDish sideDish) {
		_sideDishes.add(sideDish);
	}
	public void removeSideDish(SideDish sideDish) {
		int index = -1;
		for(int i = 0; i < _sideDishes.size(); i++) {
			if(_sideDishes.get(i).getId() == sideDish.getId())
			{
				index = i;
				break;
			}
		}
		if(index >= 0)
			_sideDishes.remove(index);
	}
}
