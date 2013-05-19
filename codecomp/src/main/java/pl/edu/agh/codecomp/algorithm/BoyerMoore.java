package pl.edu.agh.codecomp.algorithm;

import java.util.LinkedList;
import java.util.List;

public class BoyerMoore implements IAlgorithm {

	public static final String NAME = "BoyerMoore";
	private static final int ALPHABET_SIZE = 129;

	private String pattern;

	private int[] last;
	private int[] match;
	private int[] suffix;

	public BoyerMoore() {
		super();
	}

	// XXX: Seems OK - check the shifting
	public List<Integer> match(String text, String pattern) {
		this.pattern = pattern;
		last = new int[ALPHABET_SIZE];
		match = new int[pattern.length()];
		suffix = new int[pattern.length()];
		List<Integer> matches = new LinkedList<Integer>();

		computeLast();
		computeMatch();

		int i = text.length() - 1;
		int j = pattern.length() - 1;
		while (i >= 0 && i < text.length()) {
			try {
				if (pattern.charAt(j) == text.charAt(i)) {
					if (j == 0) {
						matches.add(i);
						j = pattern.length() - 1;
						--i;
						continue;
					}
					--i;
					--j;
				} else { // shifting
//					i -= Math.max(match[j], last[text.charAt(i)] - text.length() + i);
				    i--;
					j = pattern.length() - 1;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return matches;
	}

	private void computeLast() throws StringIndexOutOfBoundsException {
		for (int k = 0; k < last.length; ++k) {
			last[k] = pattern.length();
		}
		for (int j = 0; j < pattern.length() - 1; ++j) {
			last[pattern.charAt(j)] = pattern.length() - j - 1;
		}
	}

	private void computeMatch() throws StringIndexOutOfBoundsException, ArrayIndexOutOfBoundsException {
		for (int j = 0; j < match.length; j++) {
			match[j] = match.length - 1;
		}

		computeSuffix();

		for (int i = 0; i < match.length - 1; i++) {
			int j = suffix[i + 1] - 1;
			if (suffix[i] > j) {
				match[j] = j - i;
			} else {
				match[j] = Math.min(j - i + match[i], match[j]);
			}
		}

		if (suffix[0] < pattern.length()) {
			for (int j = suffix[0] - 1; j >= 0; j--) {
				if (suffix[0] < match[j]) {
					match[j] = suffix[0];
				}
			}
			int j = suffix[0];
			for (int k = suffix[j]; k < pattern.length(); k = suffix[k]) {
				while (j < k) {
					if (match[j] > k)
						match[j] = k;
					++j;
				}
			}
		}

	}

	private void computeSuffix() {
		suffix[suffix.length - 1] = suffix.length;
		int j = suffix.length - 1;
		for (int i = suffix.length - 2; i >= 0; --i) {
			while (j < suffix.length - 1 && pattern.charAt(j) != pattern.charAt(i)) {
				j = suffix[j + 1] - 1;
			}
			if (pattern.charAt(j) == pattern.charAt(i)) {
				--j;
			}
			suffix[i] = j + 1;
		}
	}

	public String getName() {
		return BoyerMoore.NAME;
	}

	@Override
	public String toString() {
		return NAME;
	}
}