package pl.edu.agh.codecomp.comparator;

import pl.edu.agh.codecomp.algorithm.BoyerMoore;
import pl.edu.agh.codecomp.algorithm.IAlgorithm;
import pl.edu.agh.codecomp.algorithm.KarpRabin;

public class CompareToken {

	private static IAlgorithm[] algoList = { new BoyerMoore(), new KarpRabin() };
	private static IAlgorithm textAlgorithm;
	static {
		textAlgorithm = algoList[0];
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
	
}
