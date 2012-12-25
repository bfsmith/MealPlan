package bs.howdy.MealPlanner.UI;

import java.awt.*;
import java.awt.event.*;
import java.util.GregorianCalendar;

import javax.swing.*;

import bs.howdy.MealPlanner.EntityManager;
import bs.howdy.MealPlanner.Entities.MealDay;

public class CalendarPanel2 extends JPanel {
	private EntityManager _manager;
	private JLabel _monthName;
	private JButton _prev;
	private JButton _next;
	private JPanel _calendarPanel;
	private int _year;
	private int _month;
	private String[] daysOfWeek = { "Sun", "Mon", "Tues", "Wed", "Thur", "Fri", "Sat" };
	private String[] monthsOfYear = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
	private int _selectedDay = -1;
	private MealDayPanel _selectedPanel = null;
	private int _currentDay;
	private int _currentMonth;
	private int _currentYear;
	private MealDayDetailsPanel _mealDayDetailsPanel;
	
	public CalendarPanel2() {
		super();
		_manager = EntityManager.Instance();
		GregorianCalendar cal = new GregorianCalendar();
		_currentDay = cal.get(GregorianCalendar.DAY_OF_MONTH);
		_currentMonth = cal.get(GregorianCalendar.MONTH);
		_currentYear = cal.get(GregorianCalendar.YEAR);
		
		setLayout(new BorderLayout());
		
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new BorderLayout());
		_monthName = new JLabel();
		_monthName.setHorizontalAlignment(JLabel.CENTER);
		_monthName.setHorizontalTextPosition(JLabel.CENTER);
		northPanel.add(_monthName);
		_prev = new JButton("<<");
		northPanel.add(_prev, BorderLayout.WEST);
		_prev.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(_month == 0)
					_year--;
				_month = (_month - 1);
				if(_month < 0)
					_month += 12;
				setMonth(_year, _month);
			}
		});
		_next = new JButton(">>");
		northPanel.add(_next, BorderLayout.EAST);
		_next.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(_month == 11)
					_year++;
				_month = (_month + 1);
				if(_month >= 12)
					_month -= 12;
				setMonth(_year, _month);
			}
		});
		add(northPanel, BorderLayout.NORTH);
		
		JPanel centralPanel = new JPanel();
		centralPanel.setLayout(new BorderLayout());
		JPanel dayHeaders = new JPanel();
		dayHeaders.setLayout(new GridLayout(1,7));
		for(String day : daysOfWeek) {
			JLabel dayLbl = new JLabel(day, JLabel.CENTER);
			dayLbl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			dayHeaders.add(dayLbl);
		}
		centralPanel.add(dayHeaders, BorderLayout.NORTH);
		_calendarPanel = new JPanel();
		_calendarPanel.setLayout(new GridLayout(0, 7, 0, 0));
		centralPanel.add(_calendarPanel);
		add(centralPanel);

		_mealDayDetailsPanel = new MealDayDetailsPanel();
		add(_mealDayDetailsPanel, BorderLayout.EAST);
		
		setMonth(_currentYear, _currentMonth);
	}
	
	public void refreshCalendar() {
		setMonth(_year, _month);
	}
	
	private void setMonth(int year, int month) {
		_calendarPanel.removeAll();
		_year = year;
		_month = month;
		GregorianCalendar cal = new GregorianCalendar(year, month, 1);
		cal.setFirstDayOfWeek(GregorianCalendar.SUNDAY);
		int numberOfDays = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		int startDay = cal.get(GregorianCalendar.DAY_OF_WEEK) - 1 % 7;
		int monthOfYear = cal.get(GregorianCalendar.MONTH);
		_monthName.setText(monthsOfYear[monthOfYear] + " " + year);
		
		for(int i = 0; i < startDay; i++) {
			_calendarPanel.add(emptyPanel());
		}
		for(int i = 1; i <= numberOfDays; i++) {
			_calendarPanel.add(dayPanel(i));
		}
		if((startDay + numberOfDays) % 7 > 0) {
			for(int i = (startDay + numberOfDays) % 7; i < 7; i++) {
				_calendarPanel.add(emptyPanel());
			}
		}
	}
	
	private JPanel emptyPanel() {
		JPanel panel = new JPanel();
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				setSelected(null);
			}
		});
		return panel;
	}
	
	private MealDayPanel dayPanel(final int day) {
		GregorianCalendar cal = new GregorianCalendar(_year, _month, day);
		
		Color background = new Color(255, 255, 255);
		if(day == _currentDay && _month == _currentMonth && _year == _currentYear)
			background = new Color(220, 220, 255);
		else if (cal.get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.SUNDAY || cal.get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.SATURDAY){
			background = new Color(255, 220, 220);
		}

		MealDay md = _manager.MealDays.getMealDay(_year, _month+1, day);
		if(md == null) {
			md = new MealDay(_year, _month, day);
			_manager.MealDays.addMealDay(md);
		}
		
		final MealDayPanel panel = new MealDayPanel(md, day, background);
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				setSelected(panel);
			}
		});
		return panel;
	};
	
	private void setSelected(MealDayPanel panel) {
		if(_selectedPanel != null) {
			_selectedPanel.deselect();
		}
		if(panel != null) {
			panel.select();
			_selectedPanel = panel;
            _mealDayDetailsPanel.setMealDay(panel.getMealDay());
		}
		else {
			_mealDayDetailsPanel.setMealDay(null);
		}
			
	}
}
