package bs.howdy.MealPlanner;

import java.util.*;

public class Cache {
	private Map<String, Object> _cache;
	
	public Cache() {
		_cache = new HashMap<String, Object>();
	}
	
	public Object get(String key) {
		return _cache.get(key);
	}
	public void put(String key, Object o) {
		_cache.put(key, o);
	}
	public void remove(String key) {
		_cache.remove(key);
	}
}
