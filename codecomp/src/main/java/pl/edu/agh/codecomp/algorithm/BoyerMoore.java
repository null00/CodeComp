package pl.edu.agh.codecomp.algorithm;

import java.util.LinkedList;
import java.util.List;

public class BoyerMoore {

	public static final int ALPHABET_SIZE = 256;

	private String text;
	private String pattern;

	private int[] last;
	private int[] match;
	private int[] suffix;

	public BoyerMoore(String text, String pattern) {
		this.text = text;
		this.pattern = pattern;
		last = new int[ALPHABET_SIZE];
		match = new int[pattern.length()];
		suffix = new int[pattern.length()];
	}

	public List<Integer> match() {
		List<Integer> matches = new LinkedList<Integer>();

		computeLast();
		computeMatch();

		int i = text.length() - 1;
		int j = pattern.length() - 1;
		while (i >= 0 && i < text.length()) {
			if (pattern.charAt(j) == text.charAt(i)) {
				if (j == 0) {
					matches.add(i);
					j = pattern.length() - 1;
					// return i;
				}
				j--;
				i--;
			} else {
				// i += pattern.length() - j - 1 + Math.max(j -
				// last[text.charAt(i)], match[j]);
				i -= Math.max(match[j], last[text.charAt(i)] - text.length() + 1 + i);
				j = pattern.length() - 1;
			}
		}
		return matches;
	}

	private void computeLast() {
		for (int k = 0; k < last.length; k++) {
			last[k] = -1;
		}
		for (int j = pattern.length() - 1; j >= 0; j--) {
			if (last[pattern.charAt(j)] < 0) {
				last[pattern.charAt(j)] = j;
			}
		}
	}

	private void computeMatch() {
		for (int j = 0; j < match.length; j++) {
			match[j] = match.length;
		}

		computeSuffix();

		for (int i = 0; i < match.length - 1; i++) {
			int j = suffix[i + 1] - 1; // suffix[i+1] <= suffix[i] + 1
			if (suffix[i] > j) { // therefore pattern[i] != pattern[j]
				match[j] = j - i;
			} else {// j == suffix[i]
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
					j++;
				}
			}
		}
	}

	private void computeSuffix() {
		suffix[suffix.length - 1] = suffix.length;
		int j = suffix.length - 1;
		for (int i = suffix.length - 2; i >= 0; i--) {
			while (j < suffix.length - 1 && pattern.charAt(j) != pattern.charAt(i)) {
				j = suffix[j + 1] - 1;
			}
			if (pattern.charAt(j) == pattern.charAt(i)) {
				j--;
			}
			suffix[i] = j + 1;
		}
	}

}