package bs.howdy.MealPlanner.UI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import bs.howdy.MealPlanner.Entities.Dish;

public class DishListRenderer implements ListCellRenderer<Dish> {

	@Override
	public Component getListCellRendererComponent(JList<? extends Dish> list,
			Dish dish, int index, boolean isSelected, boolean cellHasFocus) {
		JPanel panel = new JPanel();
		
		panel.setLayout(new BorderLayout(10, 5));
		
		if(isSelected) {
			panel.setBackground(list.getSelectionBackground());
			panel.setForeground(list.getSelectionForeground());
		}
		else {
			panel.setBackground(list.getBackground());
			panel.setForeground(list.getForeground());
		}
		
		JLabel name = new JLabel(dish.getName(), JLabel.LEFT);
		name.setFont(new Font("Arial", Font.PLAIN, 16));
		panel.add(name);
		
		return panel;
	}

}
