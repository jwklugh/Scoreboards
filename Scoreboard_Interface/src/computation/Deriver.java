package computation;

import java.util.ArrayList;

public class Deriver {
	
	private ArrayList<Integer[]> config;
	
	public Deriver(ArrayList<Integer[]> c) {
		config = c;
	}
	
	public int deriveRace(int rank) {
		for(Integer[] item : config) {
			if(item[1] <= rank && rank <= item[2]) {
				return item[0].intValue();
			}
		}
		return 1;
	}
	
	public int deriveCap(int race) {
		for(Integer[] item : config) {
			if(item[0].intValue() == race) {
				return item[3].intValue();
			}
		}
		return race + race/2;
	}
	
	public int deriveTime(int race) {
		for(Integer[] item : config) {
			if(item[0].intValue() == race) {
				return item[4].intValue();
			}
		}
		return race * 15;
	}
}
