package bs.howdy.MealPlanner.Entities;

public class SideDish extends Dish {
	public SideDish(int id, String name, String description) {
		super(id, name, description);
	}

	@Override
	public String xmlSerialize() {
		return "<SideDish id=\"" + getId() + "\"><Name>" + getName() + "</Name><Description>" + getDescription() + "</Description></SideDish>";
	}

}
