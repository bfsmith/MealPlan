package bs.howdy.MealPlanner;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import bs.howdy.MealPlanner.EntityManager.ColorPreferences;
import bs.howdy.MealPlanner.Entities.*;
import bs.howdy.MealPlanner.UI.*;
import java.awt.event.*;

public class MainWindow {
	private DataService _dataService;
	private JFrame _frmMealPlanner;
	private EntityManager manager;
	private JList<MainDish> mainDishes;
	private JList<SideDish> sideDishes;
	private MealDayDetailsPanel mealDayDetailsPanel;
	private CalendarPanel calendarPanel;
	private JColorChooser _colorChooser;

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
		_dataService = new DataService();
		manager = _dataService.Import();
		_colorChooser = new JColorChooser();
		_colorChooser.setPreviewPanel(new JPanel());
		
//		manager.populateTestData();
		
		_frmMealPlanner = new JFrame();
		BorderLayout borderLayout = (BorderLayout) _frmMealPlanner.getContentPane().getLayout();
		borderLayout.setVgap(2);
		borderLayout.setHgap(2);
		_frmMealPlanner.setTitle("Meal Planner");
		_frmMealPlanner.setBounds(100, 100, 1100, 688);
		_frmMealPlanner.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_frmMealPlanner.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				_dataService.Export(manager);
			}
		});

		calendarPanel = new CalendarPanel(manager);
		_frmMealPlanner.getContentPane().add(calendarPanel, BorderLayout.CENTER);
		
		JMenuBar mb = buildMenuBar();
        _frmMealPlanner.setJMenuBar(mb);
		
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
		mainDishes = new DishList<MainDish>(mainDishModel, "Main Dish", manager);
		mainDishPanel.add(mainDishes);
		
		JPanel mainDishButtonPanel = new JPanel();
		mainDishButtonPanel.setLayout(new GridLayout(1, 2, 2, 2));
		mainDishPanel.add(mainDishButtonPanel, BorderLayout.SOUTH);
		JButton addMainDishButton = new JButton("Add");
		addMainDishButton.addActionListener(new ActionListener() {
			@Override	 
			public void actionPerformed(ActionEvent arg0) {
				DishCreateEdit dce = new DishCreateEdit(_frmMealPlanner, "Create Main Dish", DishCreateEdit.MAIN_DISH, manager);
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
		sideDishes = new DishList<SideDish>(sideDishModel, "Side Dish", manager);
		sideDishPanel.add(sideDishes);

		JPanel sideDishButtonPanel = new JPanel();
		sideDishButtonPanel.setLayout(new GridLayout(1, 2, 2, 2));
		sideDishPanel.add(sideDishButtonPanel, BorderLayout.SOUTH);
		JButton addSideDishButton = new JButton("Add");
		addSideDishButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DishCreateEdit dce = new DishCreateEdit(_frmMealPlanner, "Create Side Dish", DishCreateEdit.SIDE_DISH, manager);
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

	private JMenuBar buildMenuBar() {
		JMenu menu = new JMenu("Color Preferences");
		
		JMenuItem todayBackground = new JMenuItem("Today Background");
		todayBackground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Color c = _colorChooser.showDialog(_frmMealPlanner, "Today's Date Background", manager.ColorPreferences.todayBackground);
				if(c != null)
					manager.ColorPreferences.todayBackground = c;
					calendarPanel.revalidate();
					calendarPanel.repaint();
					calendarPanel.refreshCalendar();
			}
		});
		menu.add(todayBackground);

		JMenuItem weekendBackground = new JMenuItem("Weekend Background");
		weekendBackground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Color c = _colorChooser.showDialog(_frmMealPlanner, "Weekend Background", manager.ColorPreferences.weekendBackground);
				if(c != null)
					manager.ColorPreferences.weekendBackground = c;
					calendarPanel.revalidate();
					calendarPanel.repaint();
					calendarPanel.refreshCalendar();
			}
		});
		menu.add(weekendBackground);

		JMenuItem mainDishBorder = new JMenuItem("Main Dish Border");
		mainDishBorder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Color c = _colorChooser.showDialog(_frmMealPlanner, "Main Dish Border", manager.ColorPreferences.MainDishBorder);
				if(c != null)
					manager.ColorPreferences.MainDishBorder = c;
					calendarPanel.revalidate();
					calendarPanel.repaint();
					calendarPanel.refreshCalendar();
			}
		});
		menu.add(mainDishBorder);

		JMenuItem mainDishBackground = new JMenuItem("Main Dish Background");
		mainDishBackground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Color c = _colorChooser.showDialog(_frmMealPlanner, "Main Dish Background", manager.ColorPreferences.MainDishBackground);
				if(c != null)
					manager.ColorPreferences.MainDishBackground = c;
					calendarPanel.revalidate();
					calendarPanel.repaint();
					calendarPanel.refreshCalendar();
			}
		});
		menu.add(mainDishBackground);

		JMenuItem sideDishBorder = new JMenuItem("Side Dish Border");
		sideDishBorder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Color c = _colorChooser.showDialog(_frmMealPlanner, "Side Dish Border", manager.ColorPreferences.SideDishBorder);
				if(c != null)
					manager.ColorPreferences.SideDishBorder = c;
					calendarPanel.revalidate();
					calendarPanel.repaint();
					calendarPanel.refreshCalendar();
			}
		});
		menu.add(sideDishBorder);

		JMenuItem sideDishBackground = new JMenuItem("Side Dish Background");
		sideDishBackground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Color c = _colorChooser.showDialog(_frmMealPlanner, "Side Dish Background", manager.ColorPreferences.SideDishBackground);
				if(c != null)
					manager.ColorPreferences.SideDishBackground = c;
					calendarPanel.revalidate();
					calendarPanel.repaint();
					calendarPanel.refreshCalendar();
			}
		});
		menu.add(sideDishBackground);

		JMenuItem defaultDayBackground = new JMenuItem("Default Day Background");
		defaultDayBackground.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Color c = _colorChooser.showDialog(_frmMealPlanner, "Default Day Background", manager.ColorPreferences.defaultDayBackground);
				if(c != null)
					manager.ColorPreferences.defaultDayBackground = c;
					calendarPanel.revalidate();
					calendarPanel.repaint();
					calendarPanel.refreshCalendar();
			}
		});
		menu.add(defaultDayBackground);

		JMenuItem selectedDayBorder = new JMenuItem("Selected Day Border");
		selectedDayBorder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Color c = _colorChooser.showDialog(_frmMealPlanner, "Selected Day Border", manager.ColorPreferences.selectedDayBorder);
				if(c != null)
					manager.ColorPreferences.selectedDayBorder = c;
					calendarPanel.revalidate();
					calendarPanel.repaint();
					calendarPanel.refreshCalendar();
			}
		});
		menu.add(selectedDayBorder);

		JMenuItem defaultDayBorder = new JMenuItem("Day Border");
		defaultDayBorder .addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Color c = _colorChooser.showDialog(_frmMealPlanner, "Day Border", manager.ColorPreferences.defaultDayBorder );
				if(c != null)
					manager.ColorPreferences.defaultDayBorder  = c;
					calendarPanel.revalidate();
					calendarPanel.repaint();
					calendarPanel.refreshCalendar();
			}
		});
		menu.add(defaultDayBorder );

		JMenuItem nonSelectedMonthBackground = new JMenuItem("Other Month Background");
		nonSelectedMonthBackground .addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Color c = _colorChooser.showDialog(_frmMealPlanner, "Other Month Background", manager.ColorPreferences.nonSelectedMonthBackground );
				if(c != null)
					manager.ColorPreferences.nonSelectedMonthBackground  = c;
					calendarPanel.revalidate();
					calendarPanel.repaint();
					calendarPanel.refreshCalendar();
			}
		});
		menu.add(nonSelectedMonthBackground );

		JMenuItem restoreDefaults = new JMenuItem("Restore Defaults");
		restoreDefaults.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				manager.ColorPreferences.restoreDefaults();
				calendarPanel.revalidate();
				calendarPanel.repaint();
				calendarPanel.refreshCalendar();
			}
		});
		menu.add(restoreDefaults);

		JMenuBar mb = new JMenuBar();
        mb.add(menu);
		return mb;
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
