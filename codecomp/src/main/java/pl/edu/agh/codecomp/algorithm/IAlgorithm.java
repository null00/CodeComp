package pl.edu.agh.codecomp.algorithm;

import java.util.List;

public interface IAlgorithm {

	public abstract List<Integer> match(String text, String pattern);
	public abstract String getName();
	
}
