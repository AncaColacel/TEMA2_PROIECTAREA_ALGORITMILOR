import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;


public class Magazin {
	// declarare variabile necesare
	private int V; 
	private ArrayList<ArrayList<Integer>> lista_ad; // Lista de adiacență a grafului
	int contor = -1;

	public Magazin(int V) {
		// initializare variabile
		this.V = V;
		lista_ad =  new ArrayList<ArrayList<Integer>>(V + 1);
		for (int i = 0; i <= V; i++) {
			lista_ad.add(i, new ArrayList<>());
		}
	}

	public void addEdge(int u, int v) {
		lista_ad.get(u).add(v);
		lista_ad.get(v).add(u);
	}
	// functie de dfs realizata iterativ
	public int dfs(int s, int E) {
		Vector<Boolean> visited = new Vector<Boolean>(V);
		for (int i = 0; i <= V; i++) {
			visited.add(false);
		}
		Stack<Integer> stack = new Stack<>();
		// se adauga in stiva nodul dat ca parametru
		stack.push(s);
		// cata vreme stiva nu e goala
		while (stack.empty() == false) {
			s = stack.peek();
			stack.pop();
			// se scot noduri din ea si creste contorul
			contor++;
			// daca contorul este egal cu nr de muchii care
			// pot fi strabatute se returneaza nodul la care 
			// s-a ajuns
			if (contor == E) {
				return s;
			}
			// daca nodul nu e vizitat se marcheaza ca si vizitat
			if (visited.get(s) == false) {
				visited.set(s, true);
			}
			Iterator<Integer> itr = lista_ad.get(s).iterator();
			// se parcurg vecinii nodului
			while (itr.hasNext()) {
				int v = itr.next();
				// daca vecinul nu este vizitat se baga in stiva
				if (!visited.get(v)) {
					stack.push(v);
				}
			}
		}
		// se intoarce -1 daca am terminat parcurgerea si contorul este 
		// mai mic decat E
		return -1;
	}


	public static void main(String[] args) {
		try {
			MyScanner sc = new MyScanner(new FileReader("magazin.in"));
			int nr_noduri = sc.nextInt();
			int nr_intrebari = sc.nextInt();
			Magazin obiect = new Magazin(nr_noduri);
			ArrayList<Integer> vector_muchii = new ArrayList<>();
			vector_muchii.add(0);
			for (int i = 1; i < nr_noduri; i++) {
				vector_muchii.add(sc.nextInt());
			}
			int [][] intrebari = new int[nr_intrebari + 1][3];
			for (int i = 1; i <= nr_intrebari; i++) {
				for (int j = 1; j <= 2; j++) {
					intrebari[i][j] = sc.nextInt();
				}
			}
			for (int i = nr_noduri - 1; i >= 1; i--) {
				int u = i + 1;
				obiect.lista_ad.get(vector_muchii.get(i)).add(u);
			}
			int ans = 0;
			try {
				FileWriter fw = new FileWriter("magazin.out");
				// se iau pe rand toate nodurile si muchiile ce alcatuiesc
				// intrebarile si se aplica functia pe ele
				for (int i = 1; i <= nr_intrebari; i++) {
					obiect.contor = -1;
					ans = obiect.dfs(intrebari[i][1],intrebari[i][2]);
					fw.write(Long.toString(ans));
					fw.write("\n");

				}

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