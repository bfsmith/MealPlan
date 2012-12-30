package bs.howdy.MealPlanner;

import java.awt.*;

import javax.swing.*;

import org.sqlite.SQLiteJDBCLoader;

import bs.howdy.MealPlanner.Entities.*;
import bs.howdy.MealPlanner.UI.*;

import java.awt.event.*;

public class MainWindow extends JFrame {
	private JFrame _frmMealPlanner;
	private EntityManager manager;
	private JList<MainDish> mainDishes;
	private JList<SideDish> sideDishes;
	private CalendarPanel calendarPanel;
	private JColorChooser _colorChooser;

	public static void main(String[] args) {
		MainWindow window = new MainWindow();
		window.setVisible(true);
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
		manager = new EntityManager();
		_colorChooser = new JColorChooser();
		_colorChooser.setPreviewPanel(new JPanel());
		
		_frmMealPlanner = new JFrame();
		BorderLayout borderLayout = (BorderLayout) getContentPane().getLayout();
		borderLayout.setVgap(2);
		borderLayout.setHgap(2);
		setTitle("Meal Planner");
		setBounds(100, 100, 1100, 688);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		calendarPanel = new CalendarPanel(manager);
		getContentPane().add(calendarPanel, BorderLayout.CENTER);
		
		JMenuBar mb = buildMenuBar();
        setJMenuBar(mb);
		
		JPanel leftSidePanel = new JPanel();
		leftSidePanel.setPreferredSize(new Dimension(200, 600));
		leftSidePanel.setLayout(new GridLayout(2, 1, 0, 0));
		getContentPane().add(leftSidePanel, BorderLayout.WEST);
		
		JPanel mainDishPanel = new JPanel();
		mainDishPanel.setLayout(new BorderLayout());
		leftSidePanel.add(mainDishPanel, BorderLayout.NORTH);
		
		final DefaultListModel<MainDish> mainDishModel = new DefaultListModel<MainDish>();
		populateMainDishes(mainDishModel);
		mainDishes = new DishList<MainDish>(mainDishModel, "Main Dish");
		mainDishPanel.add(mainDishes);
		mainDishes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() == 2) {
					int index = mainDishes.locationToIndex(mouseEvent.getPoint());
					if (index >= 0) {
						Dish dish = mainDishes.getModel().getElementAt(index);
						DishCreateEdit dce = new DishCreateEdit(null, "Edit Main Dish", (MainDish)dish, manager);
						dce.addWindowListener(new WindowAdapter() {
							@Override
							public void windowClosed(WindowEvent arg0) {
								populateMainDishes(mainDishModel);
							}
						});
						dce.setVisible(true);
					}
		        }
			}
		});
		
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
		sideDishes = new DishList<SideDish>(sideDishModel, "Side Dish");
		sideDishPanel.add(sideDishes);
		sideDishes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() == 2) {
					int index = sideDishes.locationToIndex(mouseEvent.getPoint());
					if (index >= 0) {
						Dish dish = sideDishes.getModel().getElementAt(index);
						DishCreateEdit dce = new DishCreateEdit(null, "Edit Side Dish", (SideDish)dish, manager);
						dce.addWindowListener(new WindowAdapter() {
							@Override
							public void windowClosed(WindowEvent arg0) {
								populateSideDishes(sideDishModel);
							}
						});
						dce.setVisible(true);
					}
		        }
			}
		});

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
				Color c = _colorChooser.showDialog(_frmMealPlanner, "Today's Date Background", manager.getColor("todayBackground"));
				if(c != null)
					manager.setColor("todayBackground", c);
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
				Color c = _colorChooser.showDialog(_frmMealPlanner, "Weekend Background", manager.getColor("weekendBackground"));
				if(c != null)
					manager.setColor("weekendBackground", c);
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
				Color c = _colorChooser.showDialog(_frmMealPlanner, "Main Dish Border", manager.getColor("MainDishBorder"));
				if(c != null)
					manager.setColor("MainDishBorder", c);
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
				Color c = _colorChooser.showDialog(_frmMealPlanner, "Main Dish Background", manager.getColor("MainDishBackground"));
				if(c != null)
					manager.setColor("MainDishBackground", c);
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
				Color c = _colorChooser.showDialog(_frmMealPlanner, "Side Dish Border", manager.getColor("SideDishBorder"));
				if(c != null)
					manager.setColor("SideDishBorder", c);
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
				Color c = _colorChooser.showDialog(_frmMealPlanner, "Side Dish Background", manager.getColor("SideDishBackground"));
				if(c != null)
					manager.setColor("SideDishBackground", c);
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
				Color c = _colorChooser.showDialog(_frmMealPlanner, "Default Day Background", manager.getColor("defaultDayBackground"));
				if(c != null)
					manager.setColor("defaultDayBackground", c);
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
				Color c = _colorChooser.showDialog(_frmMealPlanner, "Selected Day Border", manager.getColor("selectedDayBorder"));
				if(c != null)
					manager.setColor("selectedDayBorder", c);
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
				Color c = _colorChooser.showDialog(_frmMealPlanner, "Day Border", manager.getColor("defaultDayBorder"));
				if(c != null)
					manager.setColor("defaultDayBorder", c);
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
				Color c = _colorChooser.showDialog(_frmMealPlanner, "Other Month Background", manager.getColor("nonSelectedMonthBackground"));
				if(c != null)
					manager.setColor("nonSelectedMonthBackground", c);
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
				manager.restoreDefaultColors();
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
		for(SideDish dish : manager.getSideDishes()) {
			sideDishModel.addElement(dish);
		}
	}

	private void populateMainDishes(DefaultListModel<MainDish> mainDishModel) {
		mainDishModel.clear();
		for(MainDish dish : manager.getMainDishes()) {
			mainDishModel.addElement(dish);
		}
	}
}
