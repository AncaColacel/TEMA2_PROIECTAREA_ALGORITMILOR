import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Supercomputer {
	// numar de noduri
	private int V;
	// lista de adiacenta (este o lista de liste)
	private ArrayList<ArrayList<Integer>> lista_ad;

	Supercomputer(int v) {
		// initializare numar de noduri si matrice de adiacenta
		V = v;
		lista_ad = new ArrayList<ArrayList<Integer>>(v + 1);
		for (int i = 0; i <= v; ++i) {
			lista_ad.add(new ArrayList<Integer>());
		}
	}

	// functie pentru a adauga o muchie
	void addEdge(int v, int w) {
		lista_ad.get(v).add(w);
	}


	// functie de sortare topologica
	// aceasta functie foloseste sortarea topologica (algoritmul lui Khan)
	// si are niste particularitati
	public int topologicalSort(int nr_noduri, ArrayList<Integer> tip_date_taskuri) {

		int [] grad_nod = new int[nr_noduri + 1];
		int [] dp = new int[nr_noduri + 1];
		int cost = 0;

		// initializez vectorul dp cu 0
		for (int contor = 0; contor < nr_noduri + 1; contor++) {
			dp[contor] = 0;
		}

		// imi formez vectorul de grade
		for (int i = 1; i <= nr_noduri; i++) {
			ArrayList<Integer> temp = (ArrayList<Integer>) lista_ad.get(i);
			for (int nod : temp) {
				grad_nod[nod]++;
			}
		}

		// folosesc o coada in care initial pun nodurile
		// cu grad de intrare 0
		Queue<Integer> coada = new LinkedList<Integer>();
		for (int i = 1; i <= nr_noduri; i++) {
			if (grad_nod[i] == 0) {
				coada.add(i);
			}
		}


		while (!coada.isEmpty()) {
			// iau elementul din coada
			int u = coada.poll();

			// pentru fiecare nod copil cu nodul scos din coada
			for (int nod : lista_ad.get(u)) {
				// verific daca tipul de date pentru parinte este la fel ca la copil
				// in functie de asta salvez un cost
				if (tip_date_taskuri.get(nod).equals(tip_date_taskuri.get(u))) {
					cost = 0;
				} else {
					cost = 1;
				}
				// dp-ul pentru acel nod este valoarea maxima intre
				// dp-ul nodului si dp-ul parintelui + costul
				dp[nod] = Math.max(dp[u] + cost, dp[nod]);
				if (--grad_nod[nod] == 0) {
					coada.add(nod);
				}

			}
		}
		// numarul maxim de context switch-uri este valoarea maxima din dp
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < dp.length; i++) {
			if (dp[i] > max) {
				max = dp[i];
			}
		}
		return max;
	}


	// parte de schelet preluata din tema1
	public static void main(String[] args) {
		try {
			MyScanner sc = new MyScanner(new FileReader("supercomputer.in"));
			int nr_noduri = sc.nextInt();
			int nr_muchii = sc.nextInt();
			Supercomputer obiect = new Supercomputer(nr_noduri);
			ArrayList<Integer> tip_date_taskuri = new ArrayList<>();

			// prim element ceva fictiv
			tip_date_taskuri.add(0);
			for (int i = 1; i < nr_noduri + 1; i++) {
				tip_date_taskuri.add(sc.nextInt());

			}
			int [][] dependente = new int[nr_muchii][2];
			for (int i = 0; i < nr_muchii; i++) {
				for (int j = 0; j < 2; j++) {
					dependente[i][j] = sc.nextInt();
				}
			}
			// creez lista de adiacenta pe baza matricei de dependente
			for (int[] dependenta : dependente) {
				int nod_start = dependenta[0];
				int nod_finish = dependenta[1];
				obiect.addEdge(nod_start, nod_finish);
			}

			int ans = 0;
			ans = obiect.topologicalSort(nr_noduri, tip_date_taskuri);
			try {
				FileWriter fw = new FileWriter("supercomputer.out");
				fw.write(Long.toString(ans));
				fw.close();

			} catch (IOException e) {
				System.out.println(e.getMessage());
			}

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	private static class MyScanner {
		private BufferedReader br;
		private StringTokenizer st;

		public MyScanner(Reader reader) {
			br = new BufferedReader(reader);
		}

		public String next() {
			while (st == null || !st.hasMoreElements()) {
				try {
					st = new StringTokenizer(br.readLine());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return st.nextToken();
		}

		public int nextInt() {
			return Integer.parseInt(next());
		}

		public long nextLong() {
			return Long.parseLong(next());
		}

		public double nextDouble() {
			return Double.parseDouble(next());
		}

		public String nextLine() {
			String str = "";
			try {
				str = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return str;
		}
	}
}