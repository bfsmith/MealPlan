package bs.howdy.MealPlanner;

import java.awt.*;

import javax.swing.*;
import bs.howdy.MealPlanner.Entities.*;
import bs.howdy.MealPlanner.UI.*;
import java.awt.event.*;

public class MainWindow {

	private JFrame _frmMealPlanner;
	private EntityManager manager;
	private JList<MainDish> mainDishes;
	private JList<SideDish> sideDishes;
	private MealDayDetailsPanel mealDayDetailsPanel;
	private CalendarPanel2 calendarPanel;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window._frmMealPlanner.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		new DataService().Export();
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
//		new DnDTransferableTest();
		manager = EntityManager.Instance();
		
		_frmMealPlanner = new JFrame();
		BorderLayout borderLayout = (BorderLayout) _frmMealPlanner.getContentPane().getLayout();
		borderLayout.setVgap(2);
		borderLayout.setHgap(2);
		_frmMealPlanner.setTitle("Meal Planner");
		_frmMealPlanner.setBounds(100, 100, 1100, 688);
		_frmMealPlanner.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		calendarPanel = new CalendarPanel2();
		_frmMealPlanner.getContentPane().add(calendarPanel, BorderLayout.CENTER);
		
		DishListRenderer listRenderer = new DishListRenderer();
		
		JPanel leftSidePanel = new JPanel();
		leftSidePanel.setPreferredSize(new Dimension(200, 600));
		leftSidePanel.setLayout(new GridLayout(2, 1, 0, 0));
		_frmMealPlanner.getContentPane().add(leftSidePanel, BorderLayout.WEST);
		
		JPanel mainDishPanel = new JPanel();
		mainDishPanel.setLayout(new BorderLayout());
		leftSidePanel.add(mainDishPanel, BorderLayout.NORTH);
		
		final DefaultListModel<MainDish> mainDishModel = new DefaultListModel<MainDish>();
		populateMainDishes(mainDishModel);
		mainDishes = new DishList<MainDish>(mainDishModel, "Main Dish");
		mainDishPanel.add(mainDishes);
		
		JPanel mainDishButtonPanel = new JPanel();
		mainDishButtonPanel.setLayout(new GridLayout(1, 2, 2, 2));
		mainDishPanel.add(mainDishButtonPanel, BorderLayout.SOUTH);
		JButton addMainDishButton = new JButton("Add");
		addMainDishButton.addActionListener(new ActionListener() {
			@Override	 
			public void actionPerformed(ActionEvent arg0) {
				DishCreateEdit dce = new DishCreateEdit(_frmMealPlanner, "Create Main Dish", DishCreateEdit.MAIN_DISH);
				dce.setVisible(true);
				dce.addWindowListener(new WindowAdapter() 
				{
					public void windowClosed(WindowEvent e)
					{
						populateMainDishes(mainDishModel);
					}
				});
			}
		});
		mainDishButtonPanel.add(addMainDishButton);
		JButton deleteMainDishButton = new JButton("Delete");
		deleteMainDishButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MainDish dish = mainDishes.getSelectedValue();
				if(dish == null) return;
				int selection = JOptionPane.showConfirmDialog(_frmMealPlanner, "Are you sure you want to delete " + dish.getName() + "?", "Delete Main Dish", JOptionPane.YES_NO_OPTION);
				if(selection == JOptionPane.YES_OPTION) {
					manager.delete(dish);
					populateMainDishes(mainDishModel);
					calendarPanel.refreshCalendar();
				}
			}
		});
		mainDishButtonPanel.add(deleteMainDishButton);

		JPanel sideDishPanel = new JPanel();
		sideDishPanel.setLayout(new BorderLayout());
		leftSidePanel.add(sideDishPanel, BorderLayout.SOUTH);
		
		final DefaultListModel<SideDish> sideDishModel = new DefaultListModel<SideDish>();
		populateSideDishes(sideDishModel);
		sideDishes = new DishList<SideDish>(sideDishModel, "Side Dish");
		sideDishPanel.add(sideDishes);

		JPanel sideDishButtonPanel = new JPanel();
		sideDishButtonPanel.setLayout(new GridLayout(1, 2, 2, 2));
		sideDishPanel.add(sideDishButtonPanel, BorderLayout.SOUTH);
		JButton addSideDishButton = new JButton("Add");
		addSideDishButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DishCreateEdit dce = new DishCreateEdit(_frmMealPlanner, "Create Side Dish", DishCreateEdit.SIDE_DISH);
				dce.setVisible(true);
				dce.addWindowListener(new WindowAdapter() 
				{
					public void windowClosed(WindowEvent e)
					{
						populateSideDishes(sideDishModel);
					}
				});
			}
		});
		sideDishButtonPanel.add(addSideDishButton);
		JButton deleteSideDishButton = new JButton("Delete");
		deleteSideDishButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SideDish dish = sideDishes.getSelectedValue();
				if(dish == null) return;
				int selection = JOptionPane.showConfirmDialog(_frmMealPlanner, "Are you sure you want to delete " + dish.getName() + "?", "Delete Side Dish", JOptionPane.YES_NO_OPTION);
				if(selection == JOptionPane.YES_OPTION) {
					manager.delete(dish);
					populateSideDishes(sideDishModel);
					calendarPanel.refreshCalendar();
				}
			}
		});
		sideDishButtonPanel.add(deleteSideDishButton);
	}

	private void populateSideDishes(DefaultListModel<SideDish> sideDishModel) {
		sideDishModel.clear();
		for(SideDish dish : manager.SideDishes.getDishes()) {
			sideDishModel.addElement(dish);
		}
	}

	private void populateMainDishes(DefaultListModel<MainDish> mainDishModel) {
		mainDishModel.clear();
		for(MainDish dish : manager.MainDishes.getDishes()) {
			mainDishModel.addElement(dish);
		}
	}
}
