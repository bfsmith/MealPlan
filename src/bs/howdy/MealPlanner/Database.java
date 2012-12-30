package bs.howdy.MealPlanner;

import java.awt.Color;
import java.sql.*;
import java.util.*;

import bs.howdy.MealPlanner.Entities.*;

public class Database {
	private final String DB_NAME = "data.db";
	private EntityManager _manager;
	
	public Database(EntityManager manager) {
		_manager = manager;
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		List<SchemaPatch> patches = getSchemaPatches();
		Connection db = getConnection();
		int latestPatch = getLatestSchemaPatch(db);
		for(SchemaPatch patch : patches) {
			if(patch.getPatchNumber() > latestPatch) {
				patch.execute(db);
				addSchemaPatchNumber(db, patch.getPatchNumber());
			}
		}
		ensureIsClosed(db);
	}

	/** BEGING MAIN DISHES **/
	public void addMainDish(MainDish dish) {
		Connection db = getConnection();
		PreparedStatement ps = null;
		try {
			ps = getStatement(db, "INSERT INTO mainDish (name, description) VALUES ( ? , ? )", new Object[] { dish.getName(), dish.getDescription() });
			ps.executeUpdate();
			
			ResultSet generatedKeys = ps.getGeneratedKeys();
	        if (generatedKeys.next()) {
	            dish.setId(generatedKeys.getInt(1));
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ensureIsClosed(ps);
			ensureIsClosed(db);
		}
	}
	public void updateMainDish(MainDish dish) {
		Connection db = getConnection();
		PreparedStatement ps = null;
		try {
			ps = getStatement(db, "UPDATE mainDish SET name = ?, description = ? WHERE id = ?", new Object[] { dish.getName(), dish.getDescription(), dish.getId() });
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ensureIsClosed(ps);
			ensureIsClosed(db);
		}
	}
	public void deleteMainDish(MainDish dish) {
		Connection db = getConnection();
		PreparedStatement ps = null;
		try {
			ps = getStatement(db, "DELETE FROM mealDayMainDish WHERE mainDishId = ?", new Object[] { dish.getId() });
			ps.executeUpdate();
			ps = getStatement(db, "DELETE FROM mainDish WHERE id = ?", new Object[] { dish.getId() });
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ensureIsClosed(ps);
			ensureIsClosed(db);
		}
	}
	public MainDish getMainDish(int id) {
		Connection db = getConnection();
		PreparedStatement ps = null;
		try {
			ps = getStatement(db, "select * FROM mainDish WHERE id = ?", new Object[] { String.valueOf(id) });
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return populateMainDish(rs);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ensureIsClosed(ps);
			ensureIsClosed(db);
		}
		return null;
	}
	public List<MainDish> getMainDishes() {
		Connection db = getConnection();
		PreparedStatement ps = null;
		ArrayList<MainDish> dishes = new ArrayList<MainDish>();
		try {
			ps = getStatement(db, "select * FROM mainDish");
			ResultSet rs = ps.executeQuery();
			while(rs.next())
				dishes.add(populateMainDish(rs));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ensureIsClosed(ps);
			ensureIsClosed(db);
		}
		return dishes;
	}
	private MainDish populateMainDish(ResultSet rs) {
		try {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			String desc = rs.getString("description");
			return new MainDish(id, name, desc);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	/** END MAIN DISHES **/

	/** BEGIN SIDE DISHES **/
	public void addSideDish(SideDish dish) {
		Connection db = getConnection();
		PreparedStatement ps = null;
		try {
			ps = getStatement(db, "INSERT INTO sideDish (name, description) VALUES ( ? , ? )", new Object[] { dish.getName(), dish.getDescription() });
			ps.executeUpdate();
			
			ResultSet generatedKeys = ps.getGeneratedKeys();
	        if (generatedKeys.next()) {
	            dish.setId(generatedKeys.getInt(1));
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ensureIsClosed(ps);
			ensureIsClosed(db);
		}
	}
	public void updateSideDish(SideDish dish) {
		Connection db = getConnection();
		PreparedStatement ps = null;
		try {
			ps = getStatement(db, "UPDATE sideDish SET name = ?, description = ? WHERE id = ?", new Object[] { dish.getName(), dish.getDescription(), dish.getId() });
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ensureIsClosed(ps);
			ensureIsClosed(db);
		}
	}
	public void deleteSideDish(SideDish dish) {
		Connection db = getConnection();
		PreparedStatement ps = null;
		try {
			ps = getStatement(db, "DELETE FROM mealDaySideDish WHERE sideDishId = ?", new Object[] { dish.getId() });
			ps.executeUpdate();
			ps = getStatement(db, "DELETE FROM sideDish WHERE id = ?", new Object[] { dish.getId() });
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ensureIsClosed(ps);
			ensureIsClosed(db);
		}
	}
	public SideDish getSideDish(int id) {
		Connection db = getConnection();
		PreparedStatement ps = null;
		try {
			ps = getStatement(db, "select * FROM sideDish WHERE id = ?", new Object[] { String.valueOf(id) });
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return populateSideDish(rs);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ensureIsClosed(ps);
			ensureIsClosed(db);
		}
		return null;
	}
	public List<SideDish> getSideDishes() {
		Connection db = getConnection();
		PreparedStatement ps = null;
		ArrayList<SideDish> dishes = new ArrayList<SideDish>();
		try {
			ps = getStatement(db, "select * FROM sideDish");
			ResultSet rs = ps.executeQuery();
			while(rs.next())
				dishes.add(populateSideDish(rs));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ensureIsClosed(ps);
			ensureIsClosed(db);
		}
		return dishes;
	}
	private SideDish populateSideDish(ResultSet rs) {
		try {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			String desc = rs.getString("description");
			return new SideDish(id, name, desc);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	/** END SIDE DISHES **/

	/** BEGIN MEAL DAYS **/
	public void addMealDay(MealDay day) {
		Connection db = getConnection();
		PreparedStatement ps = null;
		try {
			ps = getStatement(db, "INSERT INTO mealDay (year, month, day) VALUES ( ? , ?, ? );", new Object[] { day.getYear(), day.getMonth(), day.getDay() });
			ps.executeUpdate();
			ResultSet generatedKeys = ps.getGeneratedKeys();
	        if (generatedKeys.next()) {
	            day.setId(generatedKeys.getInt(1));
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ensureIsClosed(ps);
		}
        if(day.getMainDish() != null) {
			try {
	        	ps = getStatement(db, "INSERT INTO mealDayMainDish (mealDayId, mainDishId) VALUES ( ? , ? );", new Object[] { day.getId(), day.getMainDish().getId() });
				ps.executeUpdate();
				ensureIsClosed(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
				ensureIsClosed(ps);
			}
        }
        if(day.getSideDishes().size() > 0) {
        	try {
		        for(SideDish sd : day.getSideDishes()) {
		        	ps = getStatement(db, "INSERT INTO mealDaySideDish (mealDayId, sideDishId) VALUES ( ? , ? );", new Object[] { day.getId(), sd.getId() });
					ps.executeUpdate();
					ensureIsClosed(ps);
		        }
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
				ensureIsClosed(ps);
			}
        }
		ensureIsClosed(db);
	}
	public void updateMealDay(MealDay day) {
		Connection db = getConnection();
		PreparedStatement ps = null;
		// Delete set main dish
		try {
        	ps = getStatement(db, "DELETE FROM mealDayMainDish WHERE mealDayId = ?;", new Object[] { day.getId() });
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ensureIsClosed(ps);
		}
		// Set main dish
        if(day.getMainDish() != null) {
			try {
	        	ps = getStatement(db, "INSERT INTO mealDayMainDish (mealDayId, mainDishId) VALUES ( ? , ? );", new Object[] { day.getId(), day.getMainDish().getId() });
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
				ensureIsClosed(ps);
			}
        }
        // Delete set side dishes
 		try {
         	ps = getStatement(db, "DELETE FROM mealDaySideDish WHERE mealDayId = ?;", new Object[] { day.getId() });
 			ps.executeUpdate();
 		} catch (SQLException e) {
 			e.printStackTrace();
 		}
 		finally {
 			ensureIsClosed(ps);
 		}
 		// Set side dishes
        if(day.getSideDishes().size() > 0) {
        	try {
		        for(SideDish sd : day.getSideDishes()) {
		        	ps = getStatement(db, "INSERT INTO mealDaySideDish (mealDayId, sideDishId) VALUES ( ? , ? );", new Object[] { day.getId(), sd.getId() });
					ps.executeUpdate();
					ensureIsClosed(ps);
	        }
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
				ensureIsClosed(ps);
			}
        }
		ensureIsClosed(db);
	}
	public MealDay getMealDay(int year, int month, int day) {
		Connection db = getConnection();
		PreparedStatement ps = null;
		try {
			ps = getStatement(db, "SELECT * FROM mealDay WHERE year = ? AND month = ? AND day = ?;", new Object[] { year, month, day });
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				return populateMealDay(rs);
			}
			MealDay md = new MealDay(year, month, day, _manager);
			addMealDay(md);
			return md;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ensureIsClosed(ps);
			ensureIsClosed(db);
		}
		return null;
	}
//	public MealDay getMealDays() {
//		Connection db = getConnection();
//		PreparedStatement ps = null;
//		try {
//			ps = getStatement(db, "SELECT * FROM mealDay WHERE year = ? AND month = ? AND day = ?);", new Object[] { year, month, day });
//			ResultSet rs = ps.executeQuery();
//			if(rs.next()) {
//				return populateMealDay(rs);
//			}
//			MealDay md = new MealDay(year, month, day, _manager);
//			addMealDay(md);
//			return md;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		finally {
//			ensureIsClosed(ps);
//			ensureIsClosed(db);
//		}
//		return null;
//	}
	private MealDay populateMealDay(ResultSet rs) {
		MealDay md = null;
		try {
			int id = rs.getInt("id");
			int year = rs.getInt("year");
			int month = rs.getInt("month");
			int day = rs.getInt("day");
			md = new MealDay(id, year, month, day, _manager);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(md == null)
			return null;
		Connection db = getConnection();
		PreparedStatement ps = null;
		// Get main dish
		try {
			ps = getStatement(db, "SELECT md2.* FROM mealDayMainDish md1 INNER JOIN mainDish md2 ON md1.mainDishId = md2.id WHERE mealDayId = ?;", new Object[] { md.getId() });
			ResultSet rs2 = ps.executeQuery();
			if(rs2.next()) {
				md.setMainDish(populateMainDish(rs2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ensureIsClosed(ps);
		}
		// Get side dishes
		try {
			ps = getStatement(db, "SELECT md2.* FROM mealDaySideDish md1 INNER JOIN sideDish md2 ON md1.sideDishId = md2.id WHERE mealDayId = ?;", new Object[] { md.getId() });
			ResultSet rs2 = ps.executeQuery();
			while(rs2.next()) {
				md.addSideDish(populateSideDish(rs2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ensureIsClosed(ps);
			ensureIsClosed(db);
		}
		return md;
	}
	/** END MEAL DAYS **/
	
	/** BEGIN COLOR PREFERENCES **/
	private Map<String, Color> _colorPreferences;
	public void setColorPreference(String name, Color color) {
		Connection db = getConnection();
		PreparedStatement ps = null;
		try {
			ps = getStatement(db, "INSERT OR REPLACE INTO colorPreference (name, R, G, B) VALUES  (?, ?, ?, ?);", new Object[] { name, color.getRed(), color.getGreen(), color.getBlue() });
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ensureIsClosed(ps);
			ensureIsClosed(db);
		}
		ensureColorPreferencesAreLoaded();
		_colorPreferences.put(name, color);
	}
	public Color getColor(String name) {
		ensureColorPreferencesAreLoaded();
		return _colorPreferences.get(name);
	}
	private void ensureColorPreferencesAreLoaded() {
		if(_colorPreferences == null)
			loadColorPreferences();
	}
	private void loadColorPreferences() {
		_colorPreferences = new HashMap<String, Color>();
		Connection db = getConnection();
		PreparedStatement ps = null;
		try {
			ps = getStatement(db, "select * FROM colorPreference");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				_colorPreferences.put(rs.getString("name"),
					new Color(rs.getInt("R"), rs.getInt("G"), rs.getInt("B")));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ensureIsClosed(ps);
			ensureIsClosed(db);
		}
	}
	public void restoreColorPreferences() {
		setColorPreference("nonSelectedMonthBackground", new Color(220, 220, 220));
		setColorPreference("MainDishBorder", Color.BLUE);
		setColorPreference("MainDishBackground", new Color(132, 199, 255));
		setColorPreference("SideDishBorder", new Color(150, 100, 255));
		setColorPreference("SideDishBackground", new Color(230, 200, 255));
		setColorPreference("weekendBackground", new Color(255, 220, 220));
		setColorPreference("todayBackground", new Color(150, 150, 255));
		setColorPreference("defaultDayBackground", new Color(255, 255, 255));
		setColorPreference("defaultDayBorder", Color.LIGHT_GRAY);
		setColorPreference("selectedDayBorder", Color.DARK_GRAY);
	}
	/** END COLOR PREFERENCES **/
	
	private Connection getConnection() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + DB_NAME);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	private PreparedStatement getStatement(Connection db, String sql) {
		return getStatement(db, sql, null);
	}
	
	private PreparedStatement getStatement(Connection db, String sql, Object[] params) {
		try {
			PreparedStatement ps = db.prepareStatement(sql);
			if(params != null) {
				for(int i = 0; i < params.length; i++) {
					ps.setString(i+1, params[i].toString());
				}
			}
			return ps;
		} catch (SQLException e) {
			System.err.println(e.toString());
		}
		return null;
	}

	private void ensureIsClosed(Connection c) {
		try { 
			if(c != null && !c.isClosed())
				c.close(); 
		} catch (Throwable e) { }
	}
	
	private void ensureIsClosed(PreparedStatement ps) {
		try { 
			if(ps != null && !ps.isClosed())
				ps.close(); 
		} catch (Throwable e) { }
	}

	private void ensureIsClosed(Statement ps) {
		try { 
			if(ps != null && !ps.isClosed())
				ps.close(); 
		} catch (Throwable e) { }
	}
	
	private void executeSql(Connection db, String sql) {
		Statement s = null;
		try {
			s = db.createStatement();
			s.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ensureIsClosed(s);
		}
	}
	
	private int getLatestSchemaPatch(Connection db) {
		executeSql(db, "CREATE TABLE IF NOT EXISTS schemaPatch ( patchNumber INTEGER NOT NULL PRIMARY KEY ASC );");
		
		PreparedStatement ps2 = getStatement(db, "SELECT patchNumber FROM schemaPatch ORDER BY patchNumber DESC LIMIT 1;", null);
	    try {
	    	ResultSet rs1 = ps2.executeQuery();
			if(rs1.next()) {
				int count = rs1.getInt(1);
				return count;
			}
	    } catch (SQLException e) {
	    	return 0;
		} finally {
			ensureIsClosed(ps2);
		}
		return 0;
	}
	
	private List<SchemaPatch> getSchemaPatches() {
		ArrayList<SchemaPatch> patches = new ArrayList<SchemaPatch>();
		patches.add(new SchemaPatch() {
			@Override
			public int getPatchNumber() {
				return 1;
			}
			@Override
			public void execute(Connection db) {
				executeSql(db, "CREATE TABLE IF NOT EXISTS mainDish ( id INTEGER NOT NULL PRIMARY KEY ASC, name TEXT NOT NULL, description TEXT );");
				executeSql(db, "CREATE TABLE IF NOT EXISTS sideDish ( id INTEGER NOT NULL PRIMARY KEY ASC, name TEXT NOT NULL, description TEXT );");
			}
		});
		patches.add(new SchemaPatch() {
			@Override
			public int getPatchNumber() {
				return 2;
			}
			@Override
			public void execute(Connection db) {
				executeSql(db, "CREATE TABLE IF NOT EXISTS mealDay ( id INTEGER NOT NULL PRIMARY KEY ASC, year INTEGER NOT NULL, month INTEGER NOT NULL, day INTEGER NOT NULL);");
				executeSql(db, "CREATE TABLE IF NOT EXISTS mealDayMainDish ( mealDayId INTEGER NOT NULL, mainDishId INTEGER NOT NULL);");
				executeSql(db, "CREATE TABLE IF NOT EXISTS mealDaySideDish ( mealDayId INTEGER NOT NULL, sideDishId INTEGER NOT NULL);");
			}
		});
		patches.add(new SchemaPatch() {
			@Override
			public int getPatchNumber() {
				return 3;
			}
			@Override
			public void execute(Connection db) {
				executeSql(db, "CREATE TABLE IF NOT EXISTS colorPreference ( name TEXT NOT NULL PRIMARY KEY ASC, R INTEGER NOT NULL, G INTEGER NOT NULL, B INTEGER NOT NULL);");
				restoreColorPreferences();
			}
		});
		return patches;
	}
	
	private void addSchemaPatchNumber(Connection db, int patchNumber) {
		PreparedStatement ps = getStatement(db, "INSERT INTO schemaPatch ( patchNumber ) VALUES ( ? );", new Object[] { patchNumber });
	    try {
	    	ps.execute();
	    } catch (SQLException e) {
	    	e.printStackTrace();
		} finally {
			try { ps.close(); } catch (Throwable ignore) { }
		}
	}
	
	private interface SchemaPatch {
		public int getPatchNumber();
		public void execute(Connection db);
	}
}
