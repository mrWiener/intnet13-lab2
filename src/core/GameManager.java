package core;

import java.util.Iterator;
import java.util.LinkedList;

public class GameManager extends LinkedList<Game>{
	private static final long serialVersionUID = 1L;

	public boolean exists(int id) {
		Iterator<Game> it = this.iterator();
		
		while(it.hasNext()) {
			Game g = it.next();
			
			if(g.getId() == id) {
				return true;
			}
		}
		
		return false;
	}
}
