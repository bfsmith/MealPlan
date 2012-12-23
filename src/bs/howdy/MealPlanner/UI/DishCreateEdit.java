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
	private EntityManager entityManager;
	public final static int MAIN_DISH = 1;
	public final static int SIDE_DISH = 2;
	
	public DishCreateEdit(int dishType) {
		_dishType = dishType;
		_dishId = -1;
		setupUI();
	}
	
	public DishCreateEdit(MainDish md) {
		_dishType = MAIN_DISH;
		setupUI();
		_dishId = md.getId();
		_name.setText(md.getName());
		_description.setText(md.getDescription());
	}

	public DishCreateEdit(SideDish sd) {
		_dishType = SIDE_DISH;
		setupUI();
		_dishId = sd.getId();
		_name.setText(sd.getName());
		_description.setText(sd.getDescription());
	}
	
	private void setupUI() {
		entityManager = EntityManager.Instance();
		setLayout(new BorderLayout(10, 10));
		
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new BorderLayout());
		namePanel.setBorder(new TitledBorder(null, "Name", TitledBorder.LEADING, TitledBorder.TOP, null, null)); //new Font("Arial", Font.BOLD, 16)
		_name = new JTextField();
		namePanel.add(_name);
		add(namePanel, BorderLayout.NORTH);
		
		JPanel descriptionPanel = new JPanel();
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
					((TitledBorder)_name.getBorder()).setTitleColor(Color.RED);
					return;
				}
				if(_description.getText().isEmpty()) {
					_description.setForeground(Color.RED);
					return;
				}
				if(_dishType == MAIN_DISH) {
					if(_dishId > 0) {
						MainDish dish = entityManager.MainDishes.get(_dishId);
						dish.setName(_name.getText());
						dish.setDescription(_description.getText());
					}
					else {
						entityManager.MainDishes.add(new MainDish(_name.getText(), _description.getText()));
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
