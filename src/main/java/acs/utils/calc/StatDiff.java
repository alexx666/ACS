package main.java.acs.utils.calc;

import java.util.List;

/**
 * 
 * @author alexx666
 *
 */
public class StatDiff {

	public static double intListDiff(List<Integer> p, List<Integer> s) {
		int counter = 0;
		for (int e : s) {
			if(!p.contains(e)) {
				counter++;
			}
		}
		return counter / s.size();
	}
	
	public static double stringListDiff(List<String> p, List<String> s) {
		int counter = 0;
		for (String e : s) {
			if(!p.contains(e)) {
				counter++;
			}
		}
		return counter / s.size();
	}
	
	public static double diff(int p, int s) {
		double max = Math.max(p, s);
		double min = Math.min(p, s);
		if (max == 0 && min == 0) {
			return 0; //just in case
		}else {
			return (max - min) / max;
		}
	}
		
	public static double sum(List<Double> list) {
		double sum = 0;
		for (Double d : list) {
			sum += d;
		}
		return sum;
	}
}
