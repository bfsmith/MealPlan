package bs.howdy.MealPlanner.Entities;

public abstract class Dish {
	public Dish(int id, String name, String description) {
		setId(id);
		setName(name);
		setDescription(description);
	}
	
	protected int _id;
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		_id = id;
	}

	protected String _name;
	public String getName() {
		return _name;
	}
	public void setName(String name) {
		_name = name;
	}

	protected String _description;
	public String getDescription() {
		return _description;
	}
	public void setDescription(String description) {
		_description = description;
	}
	
	public abstract String xmlSerialize();
}
