package bs.howdy.MealPlanner;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JList;

import bs.howdy.MealPlanner.Entities.MainDish;
import bs.howdy.MealPlanner.Entities.SideDish;
import bs.howdy.MealPlanner.UI.CalendarPanel;
import bs.howdy.MealPlanner.UI.DishListRenderer;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.TitledBorder;
import javax.swing.border.BevelBorder;

public class MainWindow {

	private JFrame _frmMealPlanner;
	private EntityManager manager;

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
		manager = EntityManager.Instance();
		
		_frmMealPlanner = new JFrame();
		BorderLayout borderLayout = (BorderLayout) _frmMealPlanner.getContentPane().getLayout();
		borderLayout.setVgap(2);
		borderLayout.setHgap(2);
		_frmMealPlanner.setTitle("Meal Planner");
		_frmMealPlanner.setBounds(100, 100, 700, 688);
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
		
		DefaultListModel<MainDish> mainDishModel = new DefaultListModel<MainDish>();
		for(MainDish dish : manager.getMainDishes()) {
			mainDishModel.addElement(dish);
		}
		JList<MainDish> mainDishes = new JList<MainDish>(mainDishModel);
		mainDishes.setBorder(new TitledBorder(null, "Main Dish", TitledBorder.LEADING, TitledBorder.TOP, new Font("Arial", Font.BOLD, 16), null));
		mainDishes.setCellRenderer(listRenderer);
		mainDishes.setDragEnabled(true);
		mainDishPanel.add(mainDishes);
		
		JPanel mainDishButtonPanel = new JPanel();
		mainDishButtonPanel.setLayout(new GridLayout(1, 2, 2, 2));
		mainDishPanel.add(mainDishButtonPanel, BorderLayout.SOUTH);
		JButton addMainDishButton = new JButton("Add");
		addMainDishButton.addActionListener(new ActionListener() {
			@Override	 
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
			}
		});
		mainDishButtonPanel.add(addMainDishButton);
		JButton deleteMainDishButton = new JButton("Delete");
		deleteMainDishButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
			}
		});
		mainDishButtonPanel.add(deleteMainDishButton);
		

		JPanel sideDishPanel = new JPanel();
		sideDishPanel.setLayout(new BorderLayout());
		sidePanel.add(sideDishPanel, BorderLayout.SOUTH);
		
		DefaultListModel<SideDish> sideDishModel = new DefaultListModel<SideDish>();
		for(SideDish dish : manager.getSideDishes()) {
			sideDishModel.addElement(dish);
		}
		JList<SideDish> sideDishes = new JList<SideDish>(sideDishModel);
		sideDishes.setBorder(new TitledBorder(null, "Side Dish", TitledBorder.LEADING, TitledBorder.TOP, new Font("Arial", Font.BOLD, 16), null));
		sideDishes.setCellRenderer(listRenderer);
		sideDishes.setDragEnabled(true);
		sideDishPanel.add(sideDishes);
		

		JPanel sideDishButtonPanel = new JPanel();
		sideDishButtonPanel.setLayout(new GridLayout(1, 2, 2, 2));
		sideDishPanel.add(sideDishButtonPanel, BorderLayout.SOUTH);
		JButton addSideDishButton = new JButton("Add");
		addSideDishButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
			}
		});
		sideDishButtonPanel.add(addSideDishButton);
		JButton deleteSideDishButton = new JButton("Delete");
		deleteSideDishButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
			}
		});
		sideDishButtonPanel.add(deleteSideDishButton);
		
	}

	private void SetupData()
	{
		
	}
}
