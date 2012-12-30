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
		setLayout(new BorderLayout());
		
		final JPanel namePanel = new JPanel();
		namePanel.setLayout(new BorderLayout());
		namePanel.setBorder(BorderFactory.createTitledBorder(null, "Name", TitledBorder.LEADING, TitledBorder.TOP)); //new Font("Arial", Font.BOLD, 16)
		_name = new JTextField();
		namePanel.add(_name);
		add(namePanel, BorderLayout.NORTH);
		
		final JPanel descriptionPanel = new JPanel();
		descriptionPanel.setBorder(BorderFactory.createTitledBorder(null, "Description", TitledBorder.LEADING, TitledBorder.TOP)); //new Font("Arial", Font.BOLD, 16)
		_description = new JTextArea(10, 40);
		_description.setLineWrap(true);
		_description.setWrapStyleWord(true);
		descriptionPanel.add(new JScrollPane(_description, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
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
						MainDish dish = _manager.getMainDish(_dishId);
						dish.setName(_name.getText());
						dish.setDescription(_description.getText());
						_manager.update(dish);
					}
					else {
						_manager.add(new MainDish(_name.getText(), _description.getText()));
					}
					closeFrame();
				}
				if(_dishType == SIDE_DISH) {
					if(_dishId > 0) {
						SideDish dish = _manager.getSideDish(_dishId);
						dish.setName(_name.getText());
						dish.setDescription(_description.getText());
						_manager.update(dish);
					}
					else {
						_manager.add(new SideDish(_name.getText(), _description.getText()));
					}
					closeFrame();
				}
			}
		});
		buttonPanel.add(saveBtn);
		getRootPane().setDefaultButton(saveBtn);
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
