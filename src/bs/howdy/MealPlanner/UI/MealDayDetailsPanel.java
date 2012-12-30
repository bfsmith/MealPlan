package bs.howdy.MealPlanner.UI;

import java.awt.*;
import javax.swing.*;

import bs.howdy.MealPlanner.Entities.*;

public class MealDayDetailsPanel extends JPanel {
	private JTextArea _description;
	
	public MealDayDetailsPanel() {
		setPreferredSize(new Dimension(200, 600));
		setLayout(new BorderLayout());
		_description = new JTextArea("Select a day to see the details here...");
		_description.setWrapStyleWord(true);
		_description.setLineWrap(true);
		add(new JScrollPane(_description, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
	}
	
	public void setMealDay(MealDay mealDay) {
		if(mealDay == null) {
			_description.setText("Select a day to see the details here...");
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
				_description.setText("No meals for this day...");
			}
			else {
				_description.setText(sb.toString());
			}
		}
	}
}
