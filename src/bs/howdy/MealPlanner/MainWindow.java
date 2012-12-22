package bs.howdy.MealPlanner;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JList;

import bs.howdy.MealPlanner.Entities.Dish;
import bs.howdy.MealPlanner.Entities.MainDish;
import bs.howdy.MealPlanner.Entities.SideDish;
import bs.howdy.MealPlanner.UI.CalendarPanel;
import bs.howdy.MealPlanner.UI.DishList;
import bs.howdy.MealPlanner.UI.DishListRenderer;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.border.TitledBorder;
import javax.swing.border.BevelBorder;

public class MainWindow {

	private JFrame _frmMealPlanner;
	private EntityManager manager;
	private JList<MainDish> mainDishes;
	private JList<SideDish> sideDishes;

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
//		new DnDTransferableTest();
		manager = EntityManager.Instance();
		
		_frmMealPlanner = new JFrame();
		BorderLayout borderLayout = (BorderLayout) _frmMealPlanner.getContentPane().getLayout();
		borderLayout.setVgap(2);
		borderLayout.setHgap(2);
		_frmMealPlanner.setTitle("Meal Planner");
		_frmMealPlanner.setBounds(100, 100, 900, 688);
		_frmMealPlanner.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		CalendarPanel calendarPanel = new CalendarPanel();
		_frmMealPlanner.getContentPane().add(calendarPanel, BorderLayout.CENTER);
		
		DishListRenderer listRenderer = new DishListRenderer();
		
		JPanel sidePanel = new JPanel();
		sidePanel.setPreferredSize(new Dimension(200, 600));
		sidePanel.setLayout(new GridLayout(2, 1, 0, 0));
		_frmMealPlanner.getContentPane().add(sidePanel, BorderLayout.WEST);
		
		JPanel mainDishPanel = new JPanel();
		mainDishPanel.setLayout(new BorderLayout());
		sidePanel.add(mainDishPanel, BorderLayout.NORTH);
		
		final DefaultListModel<MainDish> mainDishModel = new DefaultListModel<MainDish>();
		populateMainDishes(mainDishModel);
		mainDishes = new DishList<MainDish>(mainDishModel, "Main Dish");
//		new DragSource().createDefaultDragGestureRecognizer(mainDishes,
//				DnDConstants.ACTION_COPY, new DragGestureListImp());
		mainDishPanel.add(mainDishes);
		
		
		JPanel mainDishButtonPanel = new JPanel();
		mainDishButtonPanel.setLayout(new GridLayout(1, 2, 2, 2));
		mainDishPanel.add(mainDishButtonPanel, BorderLayout.SOUTH);
		JButton addMainDishButton = new JButton("Add");
		addMainDishButton.addActionListener(new ActionListener() {
			@Override	 
			public void actionPerformed(ActionEvent arg0) {
				int num = manager.getMainDishes().size();
				MainDish dish = new MainDish(num, "Main Dish " + num, "Description " + num);
				manager.addMainDish(dish);
				populateMainDishes(mainDishModel);
			}
		});
		mainDishButtonPanel.add(addMainDishButton);
		JButton deleteMainDishButton = new JButton("Delete");
		deleteMainDishButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MainDish dish = mainDishes.getSelectedValue();
				if(dish == null) return;
				manager.deleteMainDish(dish);
				populateMainDishes(mainDishModel);
			}
		});
		mainDishButtonPanel.add(deleteMainDishButton);
		

		JPanel sideDishPanel = new JPanel();
		sideDishPanel.setLayout(new BorderLayout());
		sidePanel.add(sideDishPanel, BorderLayout.SOUTH);
		
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
				int num = manager.getSideDishes().size();
				SideDish dish = new SideDish(num, "Side Dish " + num, "Description " + num);
				manager.addSideDish(dish);
				populateSideDishes(sideDishModel);
			}
		});
		sideDishButtonPanel.add(addSideDishButton);
		JButton deleteSideDishButton = new JButton("Delete");
		deleteSideDishButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SideDish dish = sideDishes.getSelectedValue();
				if(dish == null) return;
				manager.deleteSideDish(dish);
				populateSideDishes(sideDishModel);
			}
		});
		sideDishButtonPanel.add(deleteSideDishButton);
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
