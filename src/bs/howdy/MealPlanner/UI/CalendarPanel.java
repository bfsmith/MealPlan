package bs.howdy.MealPlanner.UI;

import java.awt.*;
import java.awt.event.*;
import java.util.GregorianCalendar;

import javax.swing.*;

import bs.howdy.MealPlanner.EntityManager;
import bs.howdy.MealPlanner.Entities.MealDay;

public class CalendarPanel extends JPanel {
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
	
	public CalendarPanel(EntityManager manager) {
		super();
		_manager = manager;
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
		
		if(startDay > 0) {
			GregorianCalendar previousMonth = getPreviousMonth(year, month);
			int maxDays = previousMonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
			
			for(int i = maxDays - startDay + 1; i <= maxDays; i++) {
				JPanel jp = dayPanel(previousMonth.get(GregorianCalendar.YEAR), previousMonth.get(GregorianCalendar.MONTH), i);
				_calendarPanel.add(jp);
			}
		}
		for(int i = 1; i <= numberOfDays; i++) {
			_calendarPanel.add(dayPanel(year, month, i));
		}
		if((startDay + numberOfDays) % 7 > 0) {
			GregorianCalendar previousMonth = getPreviousMonth(year, month);
			int maxDays = previousMonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
			
			for(int i = 1; i <= 7-(startDay + numberOfDays) % 7; i++) {
				JPanel jp = dayPanel(previousMonth.get(GregorianCalendar.YEAR), previousMonth.get(GregorianCalendar.MONTH), i);
				_calendarPanel.add(jp);
			}
		}
	}
	
	private GregorianCalendar getPreviousMonth(int year, int month) {
		if(month == 0) {
			year--;
			month = 11;
		} else { month--; }
		return new GregorianCalendar(year, month, 1);
	}

	private GregorianCalendar getNextMonth(int year, int month) {
		if(month == 11) {
			year++;
			month = 0;
		} else { month++; }
		return new GregorianCalendar(year, month, 1);
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
	
	private MealDayPanel dayPanel(final int year, final int month, final int day) {
		GregorianCalendar cal = new GregorianCalendar(year, month, day);
		
		Color background = _manager.ColorPreferences.defaultDayBackground;
		if(day == _currentDay && month == _currentMonth && year == _currentYear)
			background = _manager.ColorPreferences.todayBackground;
		else if(cal.get(GregorianCalendar.MONTH) != _month) {
			background = _manager.ColorPreferences.nonSelectedMonthBackground;
		}
		else if (cal.get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.SUNDAY || cal.get(GregorianCalendar.DAY_OF_WEEK) == GregorianCalendar.SATURDAY){
			background = _manager.ColorPreferences.weekendBackground;
		}

		MealDay md = _manager.MealDays.getMealDay(year, month+1, day);
		if(md == null) {
			md = new MealDay(year, month+1, day, _manager);
			_manager.MealDays.addMealDay(md);
		}
		
		final MealDayPanel panel = new MealDayPanel(md, day, background, _manager);
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
