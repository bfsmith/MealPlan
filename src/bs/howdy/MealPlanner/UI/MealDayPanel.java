package bs.howdy.MealPlanner.UI;

import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import bs.howdy.MealPlanner.Entities.Dish;
import bs.howdy.MealPlanner.Entities.MainDish;
import bs.howdy.MealPlanner.Entities.MealDay;
import bs.howdy.MealPlanner.Entities.SideDish;

public class MealDayPanel extends JPanel implements Droppable<Dish> {
	private MealDay _md;
	
	public MealDayPanel(MealDay md, int dayOfMonth, Color background) {
		setLayout(new BorderLayout());
		add(new JLabel(String.valueOf(dayOfMonth)), BorderLayout.NORTH);
		setBackground(background);
		_md = md;
		setupPanel();
		setBorder(BorderFactory.createLineBorder(Color.GREEN));
		
		setTransferHandler(new DishTransferHandler());
	}
	
	private void updateMealDay(Dish d) {
		if(d instanceof MainDish) {
			_md.setMainDish((MainDish)d);
		}
		else if(d instanceof SideDish) {
			_md.addSideDish((SideDish)d);
		}
	}
	public void setMealDay(MealDay md) {
		_md = md;
		setupPanel();
	}
	public MealDay getMealDay() {
		return _md;
	}
	
	private void setupPanel() {
		JPanel meals = new JPanel();
		meals.setBackground(getBackground());
		meals.setLayout(new GridLayout(0,1,2,2));
		
		MainDish mainDish = _md.getMainDish();
		if(mainDish != null) {
			JLabel mdp = new JLabel(mainDish.getName());
			mdp.setBackground(Color.BLUE);
			mdp.setBorder(BorderFactory.createLineBorder(Color.BLUE));
			meals.add(mdp);
		}
		for(SideDish sideDish : _md.getSideDishes()) {
			JLabel sdp = new JLabel(sideDish.getName());
			sdp.setBackground(Color.PINK);
			sdp.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
			meals.add(sdp);
		}
		
		add(meals, BorderLayout.CENTER);
	}

	@Override
	public void drop(Dish dish) {
		updateMealDay(dish);
	}
}
