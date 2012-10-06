package pl.edu.agh.codecomp.algorithm;

import java.util.List;

public interface IAlgorithm {

	public List<Integer> match(String text, String pattern);
	public String getName();
	
}
