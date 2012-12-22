package bs.howdy.MealPlanner.UI;

import bs.howdy.MealPlanner.Entities.Dish;

public interface Droppable<T extends Dish> {
	void drop(T dish);
}
