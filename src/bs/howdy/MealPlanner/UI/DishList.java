package bs.howdy.MealPlanner.UI;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import bs.howdy.MealPlanner.Entities.Dish;

public class DishList<T extends Dish> extends JList<T> {
	public DishList(ListModel<T> dishModel, String title) {
		super(dishModel);
		setBorder(BorderFactory.createTitledBorder(null, title, TitledBorder.LEADING, TitledBorder.TOP, new Font("Arial", Font.BOLD, 16), null));
		setCellRenderer(new DishListRenderer());
		setDragEnabled(true);
		setTransferHandler(new DishTransferHandler());
	}
}
