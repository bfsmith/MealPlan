package bs.howdy.MealPlanner.Entities;

public class MainDish extends Dish {
	public MainDish(int id, String name, String description) {
		super(id, name, description);
	}

	public MainDish(String name, String description) {
		super(name, description);
	}	
	
	@Override
	public String xmlSerialize() {
		return "<MainDish id=\"" + getId() + "\"><Name>" + getName() + "</Name><Description>" + getDescription() + "</Description></MainDish>";
	}
	
}
