import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;

public class Ferate {
	// declarare variabile necesare
	private int V;
	private int Time;
	int index_componente;
	private ArrayList<ArrayList<Integer>> lista_ad;
	private ArrayList<ArrayList<Integer>> componente_conexe;
	private int componenta_speciala;

	Ferate(int v) {
		// initializare date
		V = v;
		Time = 0;
		componenta_speciala = 0;
		lista_ad = new ArrayList<ArrayList<Integer>>(v + 1);
		for (int i = 0; i <= v; ++i) {
			lista_ad.add(new ArrayList<Integer>());
		}
		componente_conexe = new ArrayList<ArrayList<Integer>>(v + 1);
		for (int i = 0; i <= v; ++i) {
			componente_conexe.add(new ArrayList<Integer>());
		}
		index_componente = 1;

	}

	// functie pentru a adauga o muchie
	public void addEdge(int v, int w) {
		lista_ad.get(v).add(w);
	}

	// algoritmul lui tarjan
	void scc(int nr_componente) {
		int [] disc = new int[nr_componente + 1];
		int [] low = new int[nr_componente + 1];
		// initializare disc si low pentru fiecare nod
		for (int i = 1; i <= nr_componente; i++) {
			disc[i] = -1;
			low[i] = -1;
		}

		boolean [] membruStiva = new boolean[nr_componente + 1];
		Stack<Integer> st = new Stack<Integer>();

		// se ia fiecare nod in parte si se aplica dfs pe el
		for (int i = 1; i <= nr_componente; i++) {
			if (disc[i] == -1) {
				sccUtil(i, low, disc, membruStiva, st);
			}
		}
	}

	// functie ajutatoare pentru tarjan
	public void sccUtil(int u, int [] low, int [] disc, boolean [] membruStiva, Stack<Integer> st) {
		// se ofera valori pentru disc si low ca fiind momentul
		// in care s-a vizitat acel nod
		disc[u] = Time;
		low[u] = Time;
		// timpul este apoi incrementat
		Time = Time + 1;
		// se marcheaza nodul ca vizitat
		membruStiva[u] = true;
		// se pune nodul in stiva
		st.push(u);

		int n;

		Iterator<Integer> i = lista_ad.get(u).iterator();
		// se parcurg copiii nodului
		while (i.hasNext()) {
			n = i.next();
			// daca sunt nevizitati se aplica functia pe ei
			if (disc[n] == -1) {
				sccUtil(n, low, disc, membruStiva, st);
				// la intoarcerea din recursivitate se stabileste noua
				// valoare pentru low-ul parintelui in functie de low-ul 
				// copilului
				low[u] = Math.min(low[u], low[n]);
				// daca nodul este deja vizitat se stabileste noua valoare
				// pentru low-ul parintelui in functie de discul copilului
			} else if (membruStiva[n] == true) {
				low[u] = Math.min(low[u], disc[n]);
			}
		}
		int w = -1;
		// daca low si disc pentru un nod sunt egale
		// => acel nod este componenta conexa
		if (low[u] == disc[u]) {
			while (w != u) {
				// se extrag toate nodurile pana la ajungerea
				// la acel nod si aceea este componenta conexa
				// nodul u va fi radacina pentru componenta
				w = (int)st.pop();
				componente_conexe.get(index_componente).add(w);
				membruStiva[w] = false;
			}
			index_componente++;
		}
	}
	// functie pentru a creea graful condensat
	public ArrayList<ArrayList<Integer>> graf_condensat(
		ArrayList<ArrayList<Integer>> componente_conexe, 
		int nr_noduri, int nod_special) {
		ArrayList<ArrayList<Integer>> graf_condensat = new ArrayList<>(index_componente);
		Map<Integer, Integer> map = new HashMap<>();

		// alocare memorie pentru graful condensat
		for (int i = 0; i < index_componente; ++i) {
			graf_condensat.add(new ArrayList<Integer>());
		}
		// creare dictionar in care pentru fiecare nod retin componenta
		// conexa din care face parte acest nod
		for (int i = 1; i < index_componente; i++) {
			for (int nod : componente_conexe.get(i)) {
				if (nod == nod_special) {
					componenta_speciala = i;
				}
				map.put(nod, i);
			} 
		} 
		// pentru fiecare componenta conexa
		for (int i = 1; i < index_componente; i++) {
			// pentru fiecare nod din componenta
			for (Integer nod : componente_conexe.get(i)) {
				// pentru fiecare vecin din componenta
				for (Integer vecin : lista_ad.get(nod)) {
					// daca vecinul si nodul curent fac parte din componente diferite
					if (i != map.get(vecin)) {
						// si daca nu exista deja muchie spre acel vecin dintr-un alt
						// nod care face parte din aceeasi componenta ca si nodul curent
						if (!graf_condensat.get(i).contains(map.get(vecin))) {
							// se adauga muchie in noul graf
							graf_condensat.get(i).add(map.get(vecin));
						}
					}
				}
			}
		}
		return graf_condensat;
	}
	// parte de schelet preluata din tema1
	public static void main(String[] args) {
		try {
			MyScanner sc = new MyScanner(new FileReader("ferate.in"));
			int nr_noduri = sc.nextInt();
			int nr_muchii = sc.nextInt();
			int nod_start = sc.nextInt();
			Ferate obiect = new Ferate(nr_noduri);
			int [] grad_nod = new int[nr_noduri + 1];
			int nr_minim_muchii = 0;
			ArrayList<ArrayList<Integer>> graf_condensat;
			graf_condensat = new ArrayList<ArrayList<Integer>>(obiect.index_componente);
			for (int i = 0; i < nr_noduri; ++i) {
				graf_condensat.add(new ArrayList<Integer>());
			}


			int [][] legaturi = new int[nr_muchii][2];
			for (int i = 0; i < nr_muchii; i++) {
				for (int j = 0; j < 2; j++) {
					legaturi[i][j] = sc.nextInt();
				}
			}
			// creez lista de adiacenta pe baza matricei de legaturi
			for (int[] legatura : legaturi) {
				int nod_inceput = legatura[0];
				int nod_finish = legatura[1];
				obiect.addEdge(nod_inceput, nod_finish);
			}

			obiect.scc(nr_noduri);
			// daca numarul de componente conexe este egal cu 
			// numarul de noduri => se lucreaza pa graful initial
			if (obiect.index_componente - 1 == nr_noduri) {
				// imi formez vectorul de grade interne
				for (int i = 1; i <= nr_noduri; i++) {
					ArrayList<Integer> temp = (ArrayList<Integer>) obiect.lista_ad.get(i);
					for (int nod : temp) {
						grad_nod[nod]++;
					}
				}
				// numarul de muchii care trebuie adaugate este egal cu numarul
				// de noduri care au grad intern 0
				for (int i = 1; i <= nr_noduri; i++) {
					if (grad_nod[i] == 0) {
						nr_minim_muchii++;
					}
				}
			} else {
				// acelasi procedeu doar ca pe graful condensat
				graf_condensat = obiect.graf_condensat(obiect.componente_conexe,
				nr_noduri,nod_start);
				// imi formez vectorul de grade
				for (int i = 1; i < obiect.index_componente; i++) {
					ArrayList<Integer> temp = (ArrayList<Integer>) graf_condensat.get(i);
					for (int nod : temp) {
						grad_nod[nod]++;
					}
				}
				for (int i = 1; i < obiect.index_componente; i++) {
					// se exclude nodul care reprezinta componenta conexa
					// in care se afla nodul special
					if (grad_nod[i] == 0 && i != obiect.componenta_speciala) {
						nr_minim_muchii++;
					}
				}

			}
			int ans = 0;
			ans = nr_minim_muchii;
			try {
				FileWriter fw = new FileWriter("ferate.out");
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