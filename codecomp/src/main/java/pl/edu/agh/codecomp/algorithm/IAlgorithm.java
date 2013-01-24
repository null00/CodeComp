package pl.edu.agh.codecomp.algorithm;

import java.util.List;

import org.omg.CORBA.Object;

public interface IAlgorithm {

	public abstract List<Integer> match(String text, String pattern);
	public abstract String getName();
	
}
