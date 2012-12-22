package bs.howdy.MealPlanner.UI;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import bs.howdy.MealPlanner.Entities.Dish;

public class DishList<T extends Dish> extends JList<T> {
	public DishList(ListModel<T> dishModel, String title) {
		super(dishModel);
		setBorder(new TitledBorder(null, title, TitledBorder.LEADING, TitledBorder.TOP, new Font("Arial", Font.BOLD, 16), null));
		setCellRenderer(new DishListRenderer());
		setDragEnabled(true);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() == 2) {
					int index = locationToIndex(mouseEvent.getPoint());
					if (index >= 0) {
						Dish dish = getModel().getElementAt(index);
						// Edit dish
						JOptionPane.showMessageDialog(null, "You clicked on " + dish.getName());
		          }
		        }
			}
		});
		setTransferHandler(new DishTransferHandler());
	}
}
