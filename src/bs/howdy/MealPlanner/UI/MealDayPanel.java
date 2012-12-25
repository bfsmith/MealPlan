package bs.howdy.MealPlanner.UI;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import bs.howdy.MealPlanner.EntityManager;
import bs.howdy.MealPlanner.Entities.*;

public class MealDayPanel extends JPanel implements Droppable<Dish> {
	private MealDay _md;
	private EntityManager _manager;
	private Color _background;
	private int _dayOfMonth;
	
	public MealDayPanel(MealDay md, int dayOfMonth, Color background) {
		_manager = EntityManager.Instance();
		_md = md;
		_background = background;
		_dayOfMonth = dayOfMonth;
		setTransferHandler(new DishTransferHandler());
		setupPanel();
	}
	
	private void updateMealDay(Dish d) {
		if(d instanceof MainDish) {
			_md.setMainDish((MainDish)d);
		}
		else if(d instanceof SideDish) {
			_md.addSideDish((SideDish)d);
		}
		revalidate();
		setupPanel();
	}
	public void setMealDay(MealDay md) {
		_md = md;
		revalidate();
		setupPanel();
	}
	public MealDay getMealDay() {
		return _md;
	}
	
	public void select() {
		setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		repaint();
	}
	public void deselect() {
		setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		repaint();
	}
	
	private void setupPanel() {
		removeAll();
		setLayout(new BorderLayout());
		add(new JLabel(String.valueOf(_dayOfMonth)), BorderLayout.NORTH);
		setBackground(_background);
		
		final JPanel meals = new JPanel();
		meals.setBackground(getBackground());
		setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		meals.setLayout(new GridLayout(4,1,2,2));
		
		MainDish mainDish = _md.getMainDish();
		if(mainDish != null) {
			final JPanel jp = new JPanel();
			jp.setLayout(new BorderLayout());
			JLabel mdp = new JLabel(mainDish.getName());
			jp.setBackground(new Color(132, 199, 255));
			jp.setBorder(BorderFactory.createLineBorder(Color.BLUE));
			jp.add(mdp);
			
			JLabel removeBtn = new JLabel("X");
			jp.add(removeBtn, BorderLayout.EAST);
			removeBtn.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent mouseEvent) {
					_md.setMainDish(null);
					meals.remove(jp);
					revalidate();
					repaint();
				}
			});
			
			meals.add(jp);
		}
		for(final SideDish sideDish : _md.getSideDishes()) {
			final JPanel jp = new JPanel();
			jp.setLayout(new BorderLayout());
			JLabel sdp = new JLabel(sideDish.getName());
			jp.setBackground(new Color(230, 200, 255));
			jp.setBorder(BorderFactory.createLineBorder(new Color(150, 100, 255)));
			jp.add(sdp);
			
			JLabel removeBtn = new JLabel("X");
			jp.add(removeBtn, BorderLayout.EAST);
			removeBtn.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent mouseEvent) {
					_md.removeSideDish(sideDish);
					meals.remove(jp);
					revalidate();
					repaint();
				}
			});
			
			meals.add(jp);
		}
		
		add(meals, BorderLayout.CENTER);
	}

	@Override
	public void drop(Dish dish) {
		updateMealDay(dish);
	}
}
