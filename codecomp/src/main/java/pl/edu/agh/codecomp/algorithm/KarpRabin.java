package pl.edu.agh.codecomp.algorithm;

import java.util.List;

public class KarpRabin implements IAlgorithm {

	private int m, n, i, j, h1, h2, rm;
	private String tekst;
	private String wzorzec;
	
	public KarpRabin(String text, String pattern) {
		this.tekst = text;
		this.wzorzec = pattern;

	}
	
	@Override
	public List<Integer> match() {
		// algorytm Hornera do obliczenia funkcji haszujacej h(tekst[1..m])
		for (i = 0; i < m; i++) {
			h2 = ((h2 * r) + tekst.charAt(i));
			h2 %= q;
		}
		// algorytm Hornera do obliczenia funkcji haszujacej h(wzorzec)
		for (i = 0; i < m; i++) {
			h1 = ((h1 * r) + wzorzec.charAt(i));
			h1 %= q;
		}

		// Algorytm KR (Karpa-Rabina)
		rm = power_modulo_fast(r, m - 1, q);
		i = 0;
		while (i < n - m) {
			j = 0;
			if (h1 == h2)
				while ((j < m) && (wzorzec.charAt(j) == tekst.charAt(i + j)))
					j++;
			if (j == m)
				System.out.println(i + 1);
			h2 = ((h2 - tekst.charAt(i) * rm) * r + tekst.charAt(i + m));
			h2 %= q;
			if (h2 < 0)
				h2 += q;
			i++;
		}
		j = 0;
		if (h1 == h2)
			while ((j < m) && (wzorzec.charAt(j) == tekst.charAt(i + j)))
				j++;
		if (j == m)
			System.out.println(i + 1);
		return null;
	}

	private static final int r = 256; // liczba symboli alfabetu (char 0-255)
	private static final int q = 9551; // możliwie duża liczba pierwsza

	public static int power_modulo_fast(int a, int b, int m) {
		int i;
		int result = 1;
		long x = a % m;

		for (i = 1; i <= b; i <<= 1) {
			x %= m;
			if ((b & i) != 0) {
				result *= x;
				result %= m;
			}
			x *= x;
		}

		return result % m;
	}

}
