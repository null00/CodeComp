package pl.edu.agh.codecomp.comparator;

import pl.edu.agh.codecomp.algorithm.BoyerMoore;
import pl.edu.agh.codecomp.algorithm.IAlgorithm;
import pl.edu.agh.codecomp.algorithm.KarpRabin;

public class CompareToken {

	private static Boolean filter = true;
	private static Boolean lowerCase = true;
	
	private static IAlgorithm[] algoList = { new BoyerMoore(), new KarpRabin() };
	private static IAlgorithm textAlgorithm;
	private static String[] compList = { "Simple", "Full" };
	private static String sourceComparator;
	static {
		textAlgorithm = algoList[0];
		setSourceComparator(getCompList()[0]);
	}

	public static IAlgorithm getTextAlgorithm() {
		return textAlgorithm;
	}

	public static void setTextAlgorithm(IAlgorithm textAlgorithm) {
		CompareToken.textAlgorithm = textAlgorithm;
	}

	public static IAlgorithm[] getAlgoList() {
		return algoList;
	}

	public static void setAlgoList(IAlgorithm[] algoList) {
		CompareToken.algoList = algoList;
	}
	
	public static Boolean getFilter() {
		return filter;
	}

	public static void setFilter(Boolean filter) {
		CompareToken.filter = filter;
	}

	public static Boolean getToLowerCase() {
		return lowerCase;
	}

	public static void setToLowerCase(Boolean lowerCase) {
		CompareToken.lowerCase = lowerCase;
	}

	public static String[] getCompList() {
		return compList;
	}

	public static void setCompList(String[] compList) {
		CompareToken.compList = compList;
	}

	public static String getSourceComparator() {
		return sourceComparator;
	}

	public static void setSourceComparator(String sourceComparator) {
		CompareToken.sourceComparator = sourceComparator;
	}
	
}
