package bs.howdy.MealPlanner.UI;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import bs.howdy.MealPlanner.EntityManager;
import bs.howdy.MealPlanner.Entities.Dish;
import bs.howdy.MealPlanner.Entities.MainDish;
import bs.howdy.MealPlanner.Entities.SideDish;

public class DishCreateEdit extends JDialog {
	private int _dishId;
	private JTextField _name;
	private JTextArea _description;
	private int _dishType;
	private EntityManager _manager;
	public final static int MAIN_DISH = 1;
	public final static int SIDE_DISH = 2;
	
	public DishCreateEdit(Frame owner, String title, int dishType, EntityManager manager) {
		this(owner, title, dishType, null, manager);
	}
	
	public DishCreateEdit(Frame owner, String title, MainDish md, EntityManager manager) {
		this(owner, title, MAIN_DISH, md, manager);
	}

	public DishCreateEdit(Frame owner, String title, SideDish sd, EntityManager manager) {
		this(owner, title, SIDE_DISH, sd, manager);
	}
	
	public DishCreateEdit(Frame owner, String title, int dishType, Dish dish, EntityManager manager) {
		super(owner, title);
		_dishType = dishType;
		_manager = manager;
		setupUI();
		_dishId = -1;
		if(dish != null) {
			_dishId = dish.getId();
			_name.setText(dish.getName());
			_description.setText(dish.getDescription());
		}
	}
	
	private void setupUI() {
		setLayout(new BorderLayout(10, 10));
		
		final JPanel namePanel = new JPanel();
		namePanel.setLayout(new BorderLayout());
		namePanel.setBorder(new TitledBorder(null, "Name", TitledBorder.LEADING, TitledBorder.TOP, null, null)); //new Font("Arial", Font.BOLD, 16)
		_name = new JTextField();
		namePanel.add(_name);
		add(namePanel, BorderLayout.NORTH);
		
		final JPanel descriptionPanel = new JPanel();
		descriptionPanel.setBorder(new TitledBorder(null, "Description", TitledBorder.LEADING, TitledBorder.TOP, null, null)); //new Font("Arial", Font.BOLD, 16)
		_description = new JTextArea(10, 40);
		_description.setWrapStyleWord(true);
		descriptionPanel.add(_description);
		add(descriptionPanel);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		JButton saveBtn = new JButton("Save");
		saveBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				if(_name.getText().isEmpty()) {
					((TitledBorder)namePanel.getBorder()).setTitleColor(Color.RED);
					return;
				}
				if(_description.getText().isEmpty()) {
					((TitledBorder)descriptionPanel.getBorder()).setTitleColor(Color.RED);
					return;
				}
				if(_dishType == MAIN_DISH) {
					if(_dishId > 0) {
						MainDish dish = _manager.MainDishes.get(_dishId);
						dish.setName(_name.getText());
						dish.setDescription(_description.getText());
					}
					else {
						_manager.MainDishes.add(new MainDish(_name.getText(), _description.getText()));
					}
					closeFrame();
				}
				if(_dishType == SIDE_DISH) {
					if(_dishId > 0) {
						SideDish dish = _manager.SideDishes.get(_dishId);
						dish.setName(_name.getText());
						dish.setDescription(_description.getText());
					}
					else {
						_manager.SideDishes.add(new SideDish(_name.getText(), _description.getText()));
					}
					closeFrame();
				}
			}
		});
		buttonPanel.add(saveBtn);
		JButton cancelBtn = new JButton("Cancel");
		cancelBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				closeFrame();
			}
		});
		buttonPanel.add(cancelBtn);
		add(buttonPanel, BorderLayout.SOUTH);
		pack();
	}
	
	private void closeFrame() {
		setVisible(false);
		dispose();
	}
}
