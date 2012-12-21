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
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
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
		tblCalendar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		//Set row/column count
		tblCalendar.setRowHeight(100);
		mtblCalendar.setColumnCount(7);
		mtblCalendar.setRowCount(6);
		
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
//		lblMonth.setBounds(160-lblMonth.getPreferredSize().width/2, 25, 180, 25); //Re-align label with calendar
		
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
//			mtblCalendar.setValueAt(i, row, column);
			MealDay md = manager.getMealDay(year, month+1, i);
			mtblCalendar.setValueAt(new mealDayContainer(year, month, i, md), row, column);
		}

		//Apply renderers
		tblCalendar.setDefaultRenderer(tblCalendar.getColumnClass(0), new tblCalendarRenderer());
	}
	
	class mealDayContainer {
		public mealDayContainer(int year, int month, int day, MealDay md) {
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

			mealDayContainer mdc = (mealDayContainer)value;
			JPanel p = new JPanel();
			if(mdc != null && mdc.day == realDay && mdc.month == realMonth && mdc.year == realYear)
				p.setBackground(new Color(220, 220, 255));
			else if (column == 0 || column == 6){ //Week-end
				p.setBackground(new Color(255, 220, 220));
			}
			else{ //Week
				p.setBackground(new Color(255, 255, 255));
			}
			p.setForeground(Color.black);
			drawCell(p, mdc);
//			p.setPreferredSize(new Dimension(100, 100));
			return p;  
		}
		
		private JPanel drawCell(JPanel p, mealDayContainer mdc) {
//			JPanel p = new JPanel();

			if(mdc == null)
				return p;
			
			p.setLayout(new BorderLayout());
			p.add(new JLabel(String.valueOf(mdc.day)), BorderLayout.NORTH);
			
			JPanel meals = new JPanel();
			meals.setBackground(p.getBackground());
			meals.setLayout(new GridLayout(0,1,2,2));
//			meals.setBorder(BorderFactory.createLineBorder(Color.GREEN));
			if(mdc.mealDay != null) {
				MainDish mainDish = mdc.mealDay.getMainDish();
				if(mainDish != null) {
					JLabel mdp = new JLabel(mainDish.getName());
					mdp.setBackground(Color.CYAN);
					mdp.setBorder(BorderFactory.createLineBorder(Color.BLUE));
					meals.add(mdp);
				}
				for(SideDish sideDish : mdc.mealDay.getSideDishes()) {
					JLabel sdp = new JLabel(sideDish.getName());
					sdp.setBackground(Color.PINK);
					sdp.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
					meals.add(sdp);
				}
			}
			else
			{
				for(int i = 0; i < 4; i++) {
					meals.add(new JLabel());
				}
			}
			p.add(meals, BorderLayout.CENTER);
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
