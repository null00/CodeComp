package pl.edu.agh.codecomp.comparator;

public interface IComparator {

	public String compare(String text, String pattern);
	public void showDiff(String text, String pattern);
	
}
