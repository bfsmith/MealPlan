package bs.howdy.MealPlanner.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import bs.howdy.MealPlanner.EntityManager;
import bs.howdy.MealPlanner.Entities.MainDish;
import bs.howdy.MealPlanner.Entities.MealDay;
import bs.howdy.MealPlanner.Entities.SideDish;


public class CalendarPanel extends JPanel {
	
	private EntityManager manager;
	private JLabel lblMonth;
	private JButton btnPrev, btnNext;
	private JTable tblCalendar;
	private DefaultTableModel mtblCalendar; //Table model
	private JScrollPane stblCalendar; //The scrollpane
	private int realYear, realMonth, realDay, currentYear, currentMonth;
	private MealDayDetailsPanel _mealDayDetailsPanel;

	public CalendarPanel() {
		manager = EntityManager.Instance();
		
		setLayout(new BorderLayout(0, 0));
		//setLayout(null);
		//Create controls
		lblMonth = new JLabel ();
		lblMonth.setHorizontalAlignment(JLabel.CENTER);
		btnPrev = new JButton ("<<");
		btnNext = new JButton (">>");
		mtblCalendar = new DefaultTableModel(){public boolean isCellEditable(int rowIndex, int mColIndex){return false;}};
		tblCalendar = new JTable(mtblCalendar);
		stblCalendar = new JScrollPane(tblCalendar);
		
		//Register action listeners
		btnPrev.addActionListener(new btnPrev_Action());
		btnNext.addActionListener(new btnNext_Action());
		
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new BorderLayout(4, 0));
		northPanel.add(btnPrev, BorderLayout.WEST);
		northPanel.add(btnNext, BorderLayout.EAST);
		northPanel.add(lblMonth, BorderLayout.CENTER);
		add(northPanel, BorderLayout.NORTH);
		
		add(stblCalendar);

		_mealDayDetailsPanel = new MealDayDetailsPanel();
		add(_mealDayDetailsPanel, BorderLayout.EAST);
		
		//Get real month/year
		GregorianCalendar cal = new GregorianCalendar(); //Create calendar
		realDay = cal.get(GregorianCalendar.DAY_OF_MONTH); //Get day
		realMonth = cal.get(GregorianCalendar.MONTH); //Get month
		realYear = cal.get(GregorianCalendar.YEAR); //Get year
		currentMonth = realMonth; //Match month and year
		currentYear = realYear;
		
		//Add headers
		String[] headers = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"}; //All headers
		for (int i=0; i<7; i++){
			mtblCalendar.addColumn(headers[i]);
		}
		
		tblCalendar.getParent().setBackground(tblCalendar.getBackground()); //Set background

		//No resize/reorder
		tblCalendar.getTableHeader().setResizingAllowed(false);
		tblCalendar.getTableHeader().setReorderingAllowed(false);

		//Single cell selection
		tblCalendar.setColumnSelectionAllowed(true);
		tblCalendar.setRowSelectionAllowed(true);
		tblCalendar.setCellSelectionEnabled(true);
		tblCalendar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		//Set row/column count
		tblCalendar.setRowHeight(100);
		mtblCalendar.setColumnCount(7);
		mtblCalendar.setRowCount(6);
		
		tblCalendar.setDropMode(DropMode.ON);
		tblCalendar.setTransferHandler(new DishTransferHandler());
		
		//Refresh calendar
		refreshCalendar (realMonth, realYear); //Refresh calendar
	}
	
	public void refreshCalendar(int month, int year){
		//Variables
		String[] months =  {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		int nod, som; //Number Of Days, Start Of Month
			
		//Allow/disallow buttons
		btnPrev.setEnabled(true);
		btnNext.setEnabled(true);
		if (month == 0 && year <= realYear-10){btnPrev.setEnabled(false);} //Too early
		if (month == 11 && year >= realYear+100){btnNext.setEnabled(false);} //Too late
		lblMonth.setText(months[month] + " " + year); //Refresh the month label (at the top)
		
		//Clear table
		for (int i=0; i<6; i++){
			for (int j=0; j<7; j++){
				mtblCalendar.setValueAt(null, i, j);
			}
		}
		
		//Get first day of month and number of days
		GregorianCalendar cal = new GregorianCalendar(year, month, 1);
		nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		som = cal.get(GregorianCalendar.DAY_OF_WEEK);
		
		//Draw calendar
		for (int i=1; i<=nod; i++){
			int row = new Integer((i+som-2)/7);
			int column  =  (i+som-2)%7;
			MealDay md = manager.MealDays.getMealDay(year, month+1, i);
			if(md == null)
				md = new MealDay(year, month+1, i);
			mtblCalendar.setValueAt(new MealDayContainer(year, month, i, md), row, column);
		}

		//Apply renderer
		tblCalendar.setDefaultRenderer(tblCalendar.getColumnClass(0), new tblCalendarRenderer());
	}
	
	class MealDayContainer {
		public MealDayContainer(int year, int month, int day, MealDay md) {
			this.day = day;
			this.month = month;
			this.year = year;
			mealDay = md;
		}
		
		public int day;
		public int month;
		public int year;
		public MealDay mealDay;
	}

	class tblCalendarRenderer extends DefaultTableCellRenderer{
		
		public Component getTableCellRendererComponent (JTable table, Object value, boolean selected, boolean focused, int row, int column){
			super.getTableCellRendererComponent(table, value, selected, focused, row, column);

			final MealDayContainer mdc = (MealDayContainer)value;
			Color background = new Color(255, 255, 255);
			if(mdc != null && mdc.day == realDay && mdc.month == realMonth && mdc.year == realYear)
				background = new Color(220, 220, 255);
			else if (column == 0 || column == 6){ //Week-end
				background = new Color(255, 220, 220);
			}

			JPanel p;// = mdc != null ? new MealDayPanel(mdc.mealDay, mdc.day, background) : new JPanel();
			if(mdc != null) {
				MealDayPanel mdp = new MealDayPanel(mdc.mealDay, mdc.day, background);
				if(selected) {
					mdp.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
	                _mealDayDetailsPanel.setMealDay(mdc.mealDay);
				}
				else {
					mdp.setBorder(BorderFactory.createLineBorder(mdp.getBackground()));
				}
				p = mdp;
			}
			else {
				if(selected)
					_mealDayDetailsPanel.setMealDay(null);
				p = new JPanel();
			}
			
			p.setForeground(Color.black);
			p.setBackground(background);
			return p;  
		}
	}

	class btnPrev_Action implements ActionListener{
		public void actionPerformed (ActionEvent e){
			if (currentMonth == 0){ //Back one year
				currentMonth = 11;
				currentYear -= 1;
			}
			else{ //Back one month
				currentMonth -= 1;
			}
			refreshCalendar(currentMonth, currentYear);
		}
	}
	class btnNext_Action implements ActionListener{
		public void actionPerformed (ActionEvent e){
			if (currentMonth == 11){ //Foward one year
				currentMonth = 0;
				currentYear += 1;
			}
			else{ //Foward one month
				currentMonth += 1;
			}
			refreshCalendar(currentMonth, currentYear);
		}
	}
}
