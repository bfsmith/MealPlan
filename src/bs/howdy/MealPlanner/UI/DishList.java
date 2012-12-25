package bs.howdy.MealPlanner.UI;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import bs.howdy.MealPlanner.Entities.*;

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
						if(dish instanceof MainDish) {
							DishCreateEdit dce = new DishCreateEdit(null, "Edit Main Dish", (MainDish)dish);
							dce.setVisible(true);
						}
						else if(dish instanceof SideDish){
							DishCreateEdit dce = new DishCreateEdit(null, "Edit Side Dish", (SideDish)dish);
							dce.setVisible(true);
						}
		          }
		        }
			}
		});
		setTransferHandler(new DishTransferHandler());
	}
}
