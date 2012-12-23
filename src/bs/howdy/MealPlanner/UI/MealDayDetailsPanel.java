package bs.howdy.MealPlanner.UI;

import java.awt.*;
import javax.swing.*;

import bs.howdy.MealPlanner.Entities.*;

public class MealDayDetailsPanel extends JPanel {
	private MealDay _mealDay;
	
	private JTextArea _description;
	
	public MealDayDetailsPanel() {
		setPreferredSize(new Dimension(200, 600));
		setLayout(new BorderLayout());
		_description = new JTextArea();
		add(new JScrollPane(_description));
		setVisible(false);
	}
	
	public void setMealDay(MealDay mealDay) {
		_mealDay = mealDay;
		
		if(mealDay == null) {
			_description.setText("");
			setVisible(false);
		}
		else {
			StringBuilder sb = new StringBuilder();
			MainDish md = mealDay.getMainDish();
			if(md != null) {
				sb.append(md.getName() + " (Main Dish):\n");
				sb.append(md.getDescription());
				sb.append("\n\n");
			}
			for(SideDish sd : mealDay.getSideDishes()) {
				sb.append(sd.getName() + " (SideDish):\n");
				sb.append(sd.getDescription());
				sb.append("\n\n");
			}
			if(sb.length() == 0) {
				_description.setText("");
				setVisible(false);
			}
			else {
				_description.setText(sb.toString());
				setVisible(true);
			}
		}
	}
}
