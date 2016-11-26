package ru.kaonasi.polyhedra.lib;

import java.util.Arrays;

public class Polyhedron implements Uniform {
	
	public static double DBL_EPSILON = 2.2204460492503131e-16;
	public static double BIG_EPSILON = 3e-2;
	public static double M_PI = 3.14159265358979323846;
	
	private class SymParser {
		String sym;
		int index;
		public SymParser(String sym) {
			this.sym = sym;
			this.index = 0;
		}
		public double getNextFraction() throws PolyhedronException {
			char c;
			while (Character.isSpaceChar(sym.charAt(index))) {
				++index;
			}
			if (sym.charAt(index) == '|') {
				index++;
				return 0;
			} else {
				c = sym.charAt(index++);
				if (Character.isDigit(c)) {
					String number = "" + c;
					double a;
					try {
						while (Character.isDigit(c = sym.charAt(index))) {
							number += c;
							++index;
						}
					} catch (IndexOutOfBoundsException e) { return Double.parseDouble(number); }
					a = Double.parseDouble(number);
					if (sym.charAt(index) == '/') {
						c = sym.charAt(++index);
						if (Character.isDigit(c)) {
							number = "";
							try {
								while (Character.isDigit(c = sym.charAt(index))) {
									number += c;
									++index;
								}
							} catch (IndexOutOfBoundsException e) {  }
							return a / Double.parseDouble(number);
						} else {
							throw new PolyhedronException("No digit after \"/\": " + c);
						}
					} else {
						return a;
					}
				} else {
					throw new PolyhedronException("\"" + c + "\" is not a digit");
				}
			}
		}
		
	}
    /* NOTE: some of the int's can be replaced by short's, char's,
    	or even bit fields, at the expense of readability!!!*/
	int index; /* index to the standard list, the array uniform[] */
	int N; /* number of faces types (atmost 5)*/
	int M; /* vertex valency  (may be big for dihedral polyhedra) */
	int V; /* vertex count */
	int E; /* edge count */
	int F; /* face count */
	int D; /* density */
	int chi; /* Euler characteristic */
	int g; /* order of symmetry group */
	int K; /* symmetry type: D=2, T=3, O=4, I=5 */
	int hemi;/* flag hemi polyhedron */
	int onesided;/* flag onesided polyhedron */
	int even; /* removed face in pqr| */
	int[] Fi; /* face counts by type (array N)*/
	int[] rot; /* vertex configuration (array M of 0..N-1) */
	int[] snub; /* snub triangle configuration (array M of 0..1) */
	int[] firstrot; /* temporary for vertex generation (array V) */
	int[] anti; /* temporary for direction of ideal vertices (array E) */
	int[] ftype; /* face types (array F) */
	int[][] e; /* edges (matrix 2 x E of 0..V-1)*/
	int[][] dual_e; /* dual edges (matrix 2 x E of 0..F-1)*/
	int[][] incid; /* vertex-face incidence (matrix M x V of 0..F-1)*/
	int[][] adj; /* vertex-vertex adjacency (matrix M x V of 0..V-1)*/
	double[] p = new double[4]; /* p, q and r; |=0 */
	double minr; /* smallest nonzero inradius */
	double gon; /* basis type for dihedral polyhedra */
	double[] n; /* number of side of a face of each type (array N) */
	double[] m; /* number of faces at a vertex of each type (array N) */
	double[] gamma; /* fundamental angles in radians (array N) */
	String polyform; /* printable Wythoff symbol */
	String config; /* printable vertex configuration */
	String name; /* name, standard or manifuctured */
	String dual_name; /* dual name, standard or manifuctured */
	Vector[] v; /* vertex coordinates (array V) */
	Vector[] f; /* face coordinates (array F)*/
	
	
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getNFacesTypes() {
		return N;
	}

	public void setNFacesTypes(int n) {
		N = n;
	}

	public int getVertesValency() {
		return M;
	}

	public void setVertesValency(int m) {
		M = m;
	}

	public int getNVertex() {
		return V;
	}

	public void setNVertex(int v) {
		V = v;
	}

	public int getNEdges() {
		return E;
	}

	public void setNEdges(int e) {
		E = e;
	}

	public int getNFaces() {
		return F;
	}

	public void setNFaces(int f) {
		F = f;
	}

	public int getD() {
		return D;
	}

	public void setD(int d) {
		D = d;
	}

	public int getChi() {
		return chi;
	}

	public void setChi(int chi) {
		this.chi = chi;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getK() {
		return K;
	}

	public void setK(int k) {
		K = k;
	}

	public int getHemi() {
		return hemi;
	}

	public void setHemi(int hemi) {
		this.hemi = hemi;
	}

	public int getOnesided() {
		return onesided;
	}

	public void setOnesided(int onesided) {
		this.onesided = onesided;
	}

	public int getEven() {
		return even;
	}

	public void setEven(int even) {
		this.even = even;
	}

	public int[] getFi() {
		return Fi;
	}

	public void setFi(int[] fi) {
		Fi = fi;
	}

	public int[] getRot() {
		return rot;
	}

	public void setRot(int[] rot) {
		this.rot = rot;
	}

	public int[] getSnub() {
		return snub;
	}

	public void setSnub(int[] snub) {
		this.snub = snub;
	}

	public int[] getFirstrot() {
		return firstrot;
	}

	public void setFirstrot(int[] firstrot) {
		this.firstrot = firstrot;
	}

	public int[] getAnti() {
		return anti;
	}

	public void setAnti(int[] anti) {
		this.anti = anti;
	}

	public int[] getFtype() {
		return ftype;
	}

	public void setFtype(int[] ftype) {
		this.ftype = ftype;
	}

	public int[][] getE() {
		return e;
	}

	public void setE(int[][] e) {
		this.e = e;
	}

	public int[][] getDual_e() {
		return dual_e;
	}

	public void setDual_e(int[][] dual_e) {
		this.dual_e = dual_e;
	}

	public int[][] getIncid() {
		return incid;
	}

	public void setIncid(int[][] incid) {
		this.incid = incid;
	}

	public int[][] getAdj() {
		return adj;
	}

	public void setAdj(int[][] adj) {
		this.adj = adj;
	}

	public double[] getP() {
		return p;
	}

	public void setP(double[] p) {
		this.p = p;
	}

	public double getMinr() {
		return minr;
	}

	public void setMinr(double minr) {
		this.minr = minr;
	}

	public double getGon() {
		return gon;
	}

	public void setGon(double gon) {
		this.gon = gon;
	}

	public double[] getN() {
		return n;
	}

	public void setN(double[] n) {
		this.n = n;
	}

	public double[] getM() {
		return m;
	}

	public void setM(double[] m) {
		this.m = m;
	}

	public double[] getGamma() {
		return gamma;
	}

	public void setGamma(double[] gamma) {
		this.gamma = gamma;
	}

	public String getPolyform() {
		return polyform;
	}

	public void setPolyform(String polyform) {
		this.polyform = polyform;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDual_name() {
		return dual_name;
	}

	public void setDual_name(String dual_name) {
		this.dual_name = dual_name;
	}

	public Vector[] getV() {
		return v;
	}

	public void setV(Vector[] v) {
		this.v = v;
	}

	public Vector[] getF() {
		return f;
	}

	public void setF(Vector[] f) {
		this.f = f;
	}

	public Polyhedron() {
		index = -1;
	    even = -1;
	    K = 2;
	}

	/*
	 * Unpack input symbol: Wythoff symbol or an index to uniform[]. The symbol is
	 * a # followed by a number, or a three fractions and a bar in some order. We
	 * allow no bars only if it result from the input symbol #80.
	 */
	private void unpacksym(String sym) throws PolyhedronException {
	    sym = sym.trim();
	    if (sym != null && !sym.isEmpty()) {
	    	if (sym.startsWith("#")) { // take the number of polyhedron
	    		try {
	    			index = Integer.parseInt(sym.substring(1));
	    		} catch (NumberFormatException e) {
	    			throw new PolyhedronException("Illegal number: " + e.getMessage());
	    		}
	    		sym = Uniform.uniform[index].getWythoff();
	    	}
	    }
	    SymParser sp = new SymParser(sym);
	    p[0] = sp.getNextFraction();
	    p[1] = sp.getNextFraction();
	    p[2] = sp.getNextFraction();
	    p[3] = sp.getNextFraction();
	}

	private String sprintfrac(double x) {
	    String s = "";
	    Fraction frax = new Fraction().frac (x);
	    if (frax.getD() == 0) {
			s += "infinity";
	    } else if (frax.getD() == 1) {
	    	s+= frax.getN();
	    } else {
	    	s+= frax.getN() + "/" + frax.getD();
	    }
	    return s;
	}

	/*
	 * Using Wythoff symbol (p|qr, pq|r, pqr| or |pqr), find the Moebius triangle
	 * (2 3 K) (or (2 2 n)) of the Schwarz triangle (pqr), the order g of its
	 * symmetry group, its Euler characteristic chi, and its covering density D.
	 * g is the number of copies of (2 3 K) covering the sphere, i.e.,
	 *
	 *		g * pi * (1/2 + 1/3 + 1/K - 1) = 4 * pi
	 *
	 * D is the number of times g copies of (pqr) cover the sphere, i.e.
	 *
	 *		D * 4 * pi = g * pi * (1/p + 1/q + 1/r - 1)
	 *
	 * chi is V - E + F, where F = g is the number of triangles, E = 3*g/2 is the
	 * number of triangle edges, and V = Vp+ Vq+ Vr, with Vp = g/(2*np) being the
	 * number of vertices with angle pi/p (np is the numerator of p).
	 */
	private void moebius() throws PolyhedronException {
	    int twos = 0, j;
	/*
	 * Arrange Wythoff symbol in a presentable form. In the same time check the
	 * restrictions on the three fractions: They all have to be greater then one,
	 * and the numerators 4 or 5 cannot occur together.  We count the ocurrences of
	 * 2 in `two', and save the largest numerator in `P->K', since they reflect on
	 * the symmetry group.
	 */
	    K = 2;
	    if (index == 80) {
	    	polyform = "|";
	    } else {
	    	polyform = "";
	    }
	    for (j = 0; j < 4; j++) {
			if (p[j] > 0) {
			    String s = sprintfrac(p[j]);
			    if (j > 0 && p[j-1] > 0) {
			    	polyform += " " + s;
			    } else {
			    	polyform += s;
			    }
			    if (p[j] != 2) {
					int k;
					if ((k = (int) Fraction.numerator(p[j])) > K) {
					    if (K == 4) {
					    	break;
					    }
					    K = k;
					} else if (k < K && k == 4) {
					    break;
					}
			    } else {
			    	twos++;
			    }
			} else  {
			    polyform += "|";
			}
		}
	/*
	 * Find the symmetry group P->K (where 2, 3, 4, 5 represent the dihedral,
	 * tetrahedral, octahedral and icosahedral groups, respectively), and its order
	 * P->g.
	 */
	    if (twos >= 2) {/* dihedral */
			g = 4 * K;
			K = 2;
	    } else {
			if (K > 5) {
			    throw new PolyhedronException("numerator too large");
			}
			g = 24 * K / (6 - K);
	    }
	/*
	 * Compute the nominal density P->D and Euler characteristic P->chi.
	 * In few exceptional cases, these values will be modified later.
	 */
	    if (index != 80) {
			int i;
			D = chi = -g;
			for (j = 0; j < 4; j++) {
				if (p[j] > 0) {
				    chi += i = (int) (g / Fraction.numerator(p[j]));
				    D += i * Fraction.denominator(p[j]);
				}
			}
			chi /= 2;
			D /= 4;
			if (D <= 0)
			    throw new PolyhedronException("nonpositive density");
	    }
	}

	/*
	 * Decompose Schwarz triangle into N right triangles and compute the vertex
	 * count V and the vertex valency M.  V is computed from the number g of
	 * Schwarz triangles in the cover, divided by the number of triangles which
	 * share a vertex. It is halved for one-sided polyhedra, because the
	 * kaleidoscopic construction really produces a double orientable covering of
	 * such polyhedra. All q' q|r are of the "hemi" type, i.e. have equatorial {2r}
	 * faces, and therefore are (except 3/2 3|3 and the dihedra 2 2|r) one-sided. A
	 * well known example is 3/2 3|4, the "one-sided heptahedron". Also, all p q r|
	 * with one even denominator have a crossed parallelogram as a vertex figure,
	 * and thus are one-sided as well.
	 */
	private void decompose() throws PolyhedronException {
	    int j, J, s, t;
	    if (p[1] == 0) { /* p|q r */
			N = 2;
			M = 2 * (int)Fraction.numerator(p[0]);
			V = g / M;
			n = new double[N];
			m = new double[N];
			rot = new int[M];
			s = 0;
			for (j = 0; j < 2; j++) {
			    n[j] = p[j+2];
			    m[j] = p[0];
			}
			for (j = M / 2; j > 0; j--) {
			    rot[s++] = 0;
			    rot[s++] = 1;
			}
	    } else if (p[2] == 0) { /* p q|r */
			N = 3;
			M = 4;
			V = g / 2;
			n = new double[N];
			m = new double[N];
			rot = new int[M];
			s = 0; //rot;
			n[0] = 2 * p[3];
			m[0] = 2;
			for (j = 1; j < 3; j++) {
			    n[j] = p[j-1];
			    m[j] = 1;
			    rot[s++] = 0;
			    rot[s++] = j;
			}
			if (Math.abs(p[0] - Fraction.compl(p[1])) < DBL_EPSILON) {/* p = q' */
				    /* P->p[0]==compl(P->p[1]) should work.  However, MSDOS
				     * yeilds a 7e-17 difference! Reported by Jim Buddenhagen
				     * <jb1556@daditz.sbc.com> */
			    hemi = 1;
			    D = 0;
			    if (p[0] != 2 && !(p[3] == 3 && (p[0] == 3 || p[1] == 3))) {
					onesided = 1;
					V /= 2;
					chi /= 2;
			    }
			}
	    } else if (p[3] == 0) { /* p q r| */
			M = N = 3;
			V = g;
			n = new double[N];
			m = new double[N];
			rot = new int[M];
			s = 0; //rot;
			for (j = 0; j < 3; j++) {
			    if ((Fraction.denominator(p[j]) % 2) == 0) {
					/* what happens if there is more then one even denominator? */
					if (p[(j+1)%3] != p[(j+2)%3]) { /* needs postprocessing */
					    even = j;/* memorize the removed face */
					    chi -= g / Fraction.numerator(p[j]) / 2;
					    onesided = 1;
					    D = 0;
					} else {/* for p = q we get a double 2 2r|p */
							/* noted by Roman Maeder <maeder@inf.ethz.ch> for 4 4 3/2| */
							/* Euler characteristic is still wrong */
					    D /= 2;
					}
					V /= 2;
			    }
			    n[j] = 2 * p[j];
			    m[j] = 1;
			    rot[s++] = j;
			}
	    } else { /* |p q r - snub polyhedron */
			N = 4;
			M = 6;
			V = g / 2;/* Only "white" triangles carry a vertex */
			n = new double[N];
			m = new double[N];
			rot = new int[M];
			snub = new int[M];
			s = 0; //rot;
			t = 0; //snub;
			m[0] = n[0] = 3;
			for (j = 1; j < 4; j++) {
			    n[j] = p[j];
			    m[j] = 1;
			    rot[s++] = 0;
			    rot[s++] = j;
			    snub[t++] = 1;
			    snub[t++] = 0;
			}
	    }
	/*
	 * Sort the fundamental triangles (using bubble sort) according to decreasing
	 * n[i], while pushing the trivial triangles (n[i] = 2) to the end.
	 */
	    J = N - 1;
	    while (J != 0) {
			int last;
			last = J;
			J = 0;
			for (j = 0; j < last; j++) {
			    if ((n[j] < n[j+1] || n[j] == 2) && n[j+1] != 2) {
					int i;
					double temp;
					temp = n[j];
					n[j] = n[j+1];
					n[j+1] = temp;
					temp = m[j];
					m[j] = m[j+1];
					m[j+1] = temp;
					for (i = 0; i < M; i++) {
					    if (rot[i] == j) {
					    	rot[i] = j+1;
					    }
					    else if (rot[i] == j+1) {
					    	rot[i] = j;
					    }
					}
					if (even != -1) {
					    if (even == j) {
					    	even = j+1;
					    }
					    else if (even == j+1) {
					    	even = j;
					    }
					}
					J = j;
			    }
			}
	    }
	/*
	 *  Get rid of repeated triangles.
	 */
	    for (J = 0; J < N && n[J] != 2;J++) {
			int k, i;
			for (j = J+1; j < N && n[j] == n[J]; j++) {
			    m[J] += m[j];
			}
			k = j - J - 1;
			if (k != 0) {
			    for (i = j; i < N; i++) {
					n[i - k] = n[i];
					m[i - k] = m[i];
			    }
			    N -= k;
			    for (i = 0; i < M; i++) {
					if (rot[i] >= j)
					    rot[i] -= k;
					else if (rot[i] > J) {
					    rot[i] = J;
					}
			    }
			    if (even >= j) {
			    	even -= k;
			    }
			}
	    }
	/*
	 * Get rid of trivial triangles.
	 */
	    if (J == 0) {
	    	J = 1; /* hosohedron */
	    }
	    if (J < N) {
			int i;
			N = J;
			for (i = 0; i < M; i++) {
			    if (rot[i] >= N) {
					for (j = i + 1; j < M; j++) {
					    rot[j-1] = rot[j];
					    if (snub != null) {
					    	snub[j-1] = snub[j];
					    }
					}
					M--;
			    }
			}
	    }
	/*
	 * Truncate arrays
	 */
	    n = Arrays.copyOf(n, N);
	    m = Arrays.copyOf(m, N);
	    rot = Arrays.copyOf(rot, M);
	    if (snub != null) {
	    	snub = Arrays.copyOf(snub, M);
	    }
	}

	private void dihedral(String name, String dual_name)
	{
	    String s = sprintfrac(gon < 2 ? Fraction.compl(gon) : gon);
	    name = s + "-gonal " + name;
	    dual_name = s + "-gonal " + dual_name;
	}

	/*
	 * Get the polyhedron name, using standard list or guesswork. Ideally, we
	 * should try to locate the Wythoff symbol in the standard list (unless, of
	 * course, it is dihedral), after doing few normalizations, such as sorting
	 * angles and splitting isoceles triangles.
	 */
	private void guessname() throws PolyhedronException {
	    if (index != -1) {/* tabulated */
			name = uniform[index].getName();
			dual_name = uniform[index].getDual();
	    } else if (K == 2) {/* dihedral nontabulated */
			if (p[0] == 0) {
			    if (N == 1) {
			    	name = "octahedron";
					dual_name = "cube";
			    } else {
				    gon = n[0] == 3 ? n[1] : n[0];
				    if (gon >= 2) {
				    	dihedral("antiprism", "deltohedron");
				    }
				    else {
				    	dihedral("crossed antiprism", "concave deltohedron");
				    }
			    }
			} else if (p[3] == 0 || p[2] == 0 && p[3] == 2) {
			    if (N == 1) {
					name = "cube";
					dual_name = "octahedron";
			    }
			    gon = n[0] == 4 ? n[1] : n[0];
			    dihedral("prism", "dipyramid");
			} else if (p[1] == 0 && p[0] != 2) {
			    gon = m[0];
			    dihedral("hosohedron", "dihedron");
			} else {
			    gon = n[0];
			    dihedral("dihedron", "hosohedron");
			}
	    } else {/* other nontabulated */
			String pre[] = {"tetr", "oct", "icos"};
			name = pre[K - 3] + "ahedral ";
			if (onesided != 0) {
			    name += "one-sided ";
			}
			else if (D == 1) {
			    name += "convex ";
			}
			else {
			    name += "nonconvex ";
			}
			dual_name = name;
			name += "isogonal polyhedron";
			dual_name += "isohedral polyhedron";
	    }
	}

	/*
	 * Solve the fundamental right spherical triangles.
	 * If need_approx is set, print iterations on standard error.
	 */
	private void newton(boolean need_approx) throws PolyhedronException {
	/*
	 * First, we find initial approximations.
	 */
	    int j;
	    double cosa;
	    gamma = new double[N];
	    if (N == 1) {
	    	gamma[0] = M_PI / m[0];
	    }
	    for (j = 0; j < N; j++)
		gamma[j] = M_PI / 2 - M_PI / n[j];
	/*
	 * Next, iteratively find closer approximations for gamma[0] and compute
	 * other gamma[j]'s from Napier's equations.
	 */
	    if (need_approx) {
	    	System.out.println("Solving " + polyform);
	    }
	    for (;;) {
			double delta = M_PI, sigma = 0;
			for (j = 0; j < N; j++) {
			    if (need_approx) {
			    	System.out.print(gamma[j]);
			    }
			    delta -= m[j] * gamma[j];
			}
			if (need_approx) {
				System.out.println(delta);
			}
			if (Math.abs(delta) < 11 * DBL_EPSILON) {
			    return;
			}
			/* On a RS/6000, fabs(delta)/DBL_EPSILON may occilate between 8 and
			 * 10. Reported by David W. Sanderson <dws@ssec.wisc.edu> */
			for (j = 0; j < N; j++)
			    sigma += m[j] * Math.tan(gamma[j]);
			gamma[0] += delta * Math.tan(gamma[0]) / sigma;
			if (gamma[0] < 0 || gamma[0] > M_PI) {
			    throw new PolyhedronException("gamma out of bounds");
			}
			cosa = Math.cos(M_PI / n[0]) / Math.sin(gamma[0]);
			for (j = 1; j < N; j++)
				gamma[j] = Math.asin(Math.cos(M_PI / n[j]) / cosa);
	    }
	}

	/*
	 * Postprocess pqr| where r has an even denominator (cf. Coxeter &al. Sec.9).
	 * Remove the {2r} and add a retrograde {2p} and retrograde {2q}.
	 */
	private void exceptions() throws PolyhedronException {
	    int j;
	    if (even != -1) {
			M = N = 4;
			n = Arrays.copyOf(n, N);
			m = Arrays.copyOf(m, N);
			gamma = Arrays.copyOf(gamma, N);
			rot = Arrays.copyOf(rot, M);
			for (j = even + 1; j < 3; j++) {
			    n[j-1] = n[j];
			    gamma[j-1] = gamma[j];
			}
			n[2] = Fraction.compl(n[1]);
			gamma[2] = - gamma[1];
			n[3] = Fraction.compl(n[0]);
			m[3] = 1;
			gamma[3] = - gamma[0];
			rot[0] = 0;
			rot[1] = 1;
			rot[2] = 3;
			rot[3] = 2;
	    }

	/*
	 * Postprocess the last polyhedron |3/2 5/3 3 5/2 by taking a |5/3 3 5/2,
	 * replacing the three snub triangles by four equatorial squares and adding
	 * the missing {3/2} (retrograde triangle, cf. Coxeter &al. Sec. 11).
	 */
	    if (index == 80) {
			N = 5;
			M = 8;
			n = Arrays.copyOf(n, N);
			m = Arrays.copyOf(m, N);
			gamma = Arrays.copyOf(gamma, N);
			rot = Arrays.copyOf(rot, M);
			snub = Arrays.copyOf(snub, M);
			hemi = 1;
			D = 0;
			for (j = 3; j != 0; j--) {
			    m[j] = 1;
			    n[j] = n[j-1];
			    gamma[j] = gamma[j-1];
			}
			m[0] = n[0] = 4;
			gamma[0] = M_PI / 2;
			m[4] = 1;
			n[4] = Fraction.compl(n[1]);
			gamma[4] = - gamma[1];
			for (j = 1; j < 6; j += 2) {
				rot[j]++;
			}
			rot[6] = 0;
			rot[7] = 4;
			snub[6] = 1;
			snub[7] = 0;
	    }
	}

	/*
	 * Compute edge and face counts, and update D and chi.  Update D in the few
	 * cases the density of the polyhedron is meaningful but different than the
	 * density of the corresponding Schwarz triangle (cf. Coxeter &al., p. 418 and
	 * p. 425).
	 * In these cases, spherical faces of one type are concave (bigger than a
	 * hemisphere), and the actual density is the number of these faces less the
	 * computed density.  Note that if j != 0, the assignment gamma[j] = asin(...)
	 * implies gamma[j] cannot be obtuse.  Also, compute chi for the only
	 * non-Wythoffian polyhedron.
	 */
	private void count() throws PolyhedronException {
	    int j, temp;
	    Fi = new int[N];
	    for (j = 0; j < N; j++) {
			E += temp = V * (int)Fraction.numerator(m[j]);
			F += Fi[j] = (int)(temp / Fraction.numerator(n[j]));
	    }
	    E /= 2;
	    if (D != 0 && gamma[0] > M_PI / 2) {
	    	D = Fi[0] - D;
	    }
	    if (index == 80) {
	    	chi = V - E + F;
	    }
	}
	
	/*
	 * Generate a printable vertex configuration symbol.
	 */
	private void configuration() throws PolyhedronException {
	    int j;
	    for (j = 0; j < M; j++) {
			if (j == 0) {
			    config = "(";
			} else {
			    config += ".";
			}
			config += sprintfrac(n[rot[j]]);
	    }
	    config += ")";
	    if ((j = (int)Fraction.denominator(m[0])) != 1) {
			config += "/" + j;
	    }
	}

	/*
	 * Compute polyhedron vertices and vertex adjecency lists.
	 * The vertices adjacent to v[i] are v[adj[0][i], v[adj[1][i], ...
	 * v[adj[M-1][i], ordered counterclockwise.  The algorith is a BFS on the
	 * vertices, in such a way that the vetices adjacent to a givem vertex are
	 * obtained from its BFS parent by a cyclic sequence of rotations. firstrot[i]
	 * points to the first  rotaion in the sequence when applied to v[i]. Note that
	 * for non-snub polyhedra, the rotations at a child are opposite in sense when
	 * compared to the rotations at the parent. Thus, we fill adj[*][i] from the
	 * end to signify clockwise rotations. The firstrot[] array is not needed for
	 * display thus it is freed after being used for face computations below.
	 */
	private void vertices() throws PolyhedronException {
	    int i, newV = 2;
	    double cosa;
	    v = new Vector[V];
	    adj = new int[M][V];
	    firstrot = new int[V]; /* temporary , put in Polyhedron
			    structure so that may be freed on error */
	    cosa = Math.cos(M_PI / n[0]) / Math.sin(gamma[0]);
	    v[0] = new Vector(0, 0, 1);
	    firstrot[0] = 0;
	    adj[0][0] = 1;
	    v[1] = new Vector(2 * cosa * Math.sqrt(1 - cosa * cosa), 0, 2 * cosa * cosa - 1);
	    if (snub == null) {
			firstrot[1] = 0;
			adj[0][1] = -1;/* start the other side */
			adj[M-1][1] = 0;
	    } else {
			firstrot[1] = snub[M-1] != 0 ? 0 : M-1 ;
			adj[0][1] = 0;
	    }
	    for (i = 0; i < newV; i++) {
			int j, k;
			int last, one, start, limit;
			if (adj[0][i] == -1) {
			    one = -1; start = M-2; limit = -1;
			} else {
			    one = 1; start = 1; limit = M;
			}
			k = firstrot[i];
			for (j = start; j != limit; j += one) {
			    Vector temp;
			    int J;
			    temp = Vector.rotate(v[adj[j-one][i]], v[i], one * 2 * gamma[rot[k]]);
			    for (J=0; J<newV && !temp.same(v[J], BIG_EPSILON); J++) {
			    	/* noop */
			    }
			    adj[j][i] = J;
			    last = k;
			    if (++k == M) {
			    	k = 0;
			    }
			    if (J == newV) { /* new vertex */
					if (newV == V) {
						throw new PolyhedronException("too many vertices");
					}
					v[newV++] = temp;
					if (snub == null) {
					    firstrot[J] = k;
					    if (one > 0) {
							adj[0][J] = -1;
							adj[M-1][J] = i;
					    } else {
					    	adj[0][J] = i;
					    }
					} else {
					    firstrot[J] = snub[last] == 0 ? last :
						snub[k] == 0 ? (k+1)%M : k ;
					    adj[0][J] = i;
					}
			    }
			}
	    }
	}

	/*
	 * Compute the polar reciprocal of the plane containing a, b and c:
	 *
	 * If this plane does not contain the origin, return p such that
	 *	dot(p,a) = dot(p,b) = dot(p,b) = r.
	 *
	 * Otherwise, return p such that
	 *	dot(p,a) = dot(p,b) = dot(p,c) = 0
	 * and
	 *	dot(p,p) = 1.
	 */
	private Vector pole(double r, Vector a, Vector b, Vector c)	{
	    Vector p;
	    double k;
	    p = b.diff(a).cross(c.diff(a));
	    k = p.dot(a);
	    if (Math.abs(k) < 1e-6) {
	    	return p.scale(1 / Math.sqrt(p.dot(p)));
	    } else {
	    	return p.scale(r/ k);
	    }
	}

	/*
	 * compute the mathematical modulus function.
	 */
	private int mod (int i, int j) 	{
	    return (i%=j)>=0?i:j<0?i-j:i+j;
	}

	/*
	 * Compute polyhedron faces (dual vertices) and incidence matrices.
	 * For orientable polyhedra, we can distinguish between the two faces meeting
	 * at a given directed edge and identify the face on the left and the face on
	 * the right, as seen from the outside.  For one-sided polyhedra, the vertex
	 * figure is a papillon (in Coxeter &al.  terminology, a crossed parallelogram)
	 * and the two faces meeting at an edge can be identified as the side face
	 * (n[1] or n[2]) and the diagonal face (n[0] or n[3]).
	 */
	private void faces() throws PolyhedronException {
	    int i, newF = 0;
	    f = new Vector[F];
	    ftype = new int[F];
	    incid = new int[M][V];
	    minr = 1 / Math.abs (Math.tan (M_PI / n[hemi]) * Math.tan (gamma[hemi]));
	    for (i = M; --i>=0;) {
			for (int j = V; --j>=0;) {
			    incid[i][j] = -1;
			}
	    }
	    for (i = 0; i < V; i++) {
			for (int j = 0; j < M; j++) {
			    int i0, J;
			    int pap = 0;/* papillon edge type */
			    if (incid[j][i] != -1)
				continue;
			    incid[j][i] = newF;
			    if (newF == F) { 
			    	throw new PolyhedronException("too many faces");
			    }
			    f[newF] = pole(minr, v[i], v[adj[j][i]], v[adj[mod(j + 1, M)][i]]);
			    ftype[newF] = rot[mod(firstrot[i] + (adj[0][i] < adj[M - 1][i] ?  j : -j - 2), M)];
			    if (onesided != 0) { 
			    	pap = (firstrot[i] + j) % 2;
			    }
			    i0 = i;
			    J = j;
			    for (;;) {
					int k;
					k = i0;
					if ((i0 = adj[J][k]) == i) {
						break;
					}
					for (J = 0; J < M && adj[J][i0] != k; J++) {
					    /* noop */
					}
					if (J == M) {
					    throw new PolyhedronException("too many faces");
					}
					if (onesided != 0 && (J + firstrot[i0]) % 2 == pap) {
					    incid [J][i0] = newF;
					    if (++J >= M) {
					    	J = 0;
					    }
					} else {
					    if (--J < 0) {
					    	J = M - 1;
					    }
					    incid [J][i0] = newF;
					}
			    }
			    newF++;
			}
	    }
	}

	/*
	 * Compute edge list and graph polyhedron and dual.
	 * If the polyhedron is of the "hemi" type, each edge has one finite vertex and
	 * one ideal vertex. We make sure the latter is always the out-vertex, so that
	 * the edge becomes a ray (half-line).  Each ideal vertex is represented by a
	 * unit Vector, and the direction of the ray is either parallel or
	 * anti-parallel this Vector. We flag this in the array P->anti[E].
	 */
	private void edgelist() throws PolyhedronException {
	    int i, j, s, t, u;
	    e = new int[2][E];
	    dual_e = new int[2][E];
	    s = 0;//e[0];
	    t = 0;//e[1];
	    for (i = 0; i < V; i++) {
			for (j = 0; j < M; j++) {
			    if (i < adj[j][i]) {
					e[0][s++] = i;
					e[1][t++] = adj[j][i];
			    }
			}
	    }
	    s = 0;//dual_e[0];
	    t = 0;//dual_e[1];
	    if (hemi != 0) {
	    	anti = null;
	    } else { 
	    	anti = new int[E];
	    }
	    u = 0;//anti;
	    for (i = 0; i < V; i++) {
			for (j = 0; j < M; j++) {
			    if (i < adj[j][i]) {
					if (anti == null) {
						dual_e[0][s++] = incid[mod(j-1,M)][i];
						dual_e[1][t++] = incid[j][i];
					} else {
					    if (ftype[incid[j][i]] != 0) {
					    	dual_e[0][s] = incid[j][i];
					    	dual_e[1][t] = incid[mod(j-1,M)][i];
					    } else {
					    	dual_e[0][s] = incid[mod(j-1,M)][i];
					    	dual_e[1][t] = incid[j][i];
					    }
					    anti[u++] = f[dual_e[0][s++]].dot(f[dual_e[1][t++]]) > 0 ? 1 : 0;
					}
			    }
			}
	    }
	}

	public void kaleido(String sym, boolean need_coordinates, boolean need_edgelist, boolean need_approx, boolean just_list) throws PolyhedronException {
		/*
		 * Unpack input symbol into P.
		 */
		try {
			unpacksym(sym);
		} catch (PolyhedronException e) {
			throw new PolyhedronException("Unpacking symbol error: " + e.getMessage());
		}
		/*
		 * Find Mebius triangle, its density and Euler characteristic.
		 */
		try {
			moebius();
		} catch (PolyhedronException e) {
			throw new PolyhedronException("Moebius triangle error: " + e.getMessage());
		}
		/*
		 * Decompose Schwarz triangle.
		 */
		try {
			decompose();
		} catch (PolyhedronException e) {
			throw new PolyhedronException("Decompose error: " + e.getMessage());
		}
		/*
		 * Find the names of the polyhedron and its dual.
		 */
		try {
			guessname();
		} catch (PolyhedronException e) {
			throw new PolyhedronException("Finding names error: " + e.getMessage());
		}
	    if (just_list) {
	    	return;
	    }
		/*
		 * Solve Fundamental triangles, optionally printing approximations.
		 */
		try {
			newton(need_approx);
		} catch (PolyhedronException e) {
			throw new PolyhedronException("Fundamental triangles solving error: " + e.getMessage());
		}
		/*
		 * Deal with exceptional polyhedra.
		 */
		try {
			exceptions();
		} catch (PolyhedronException e) {
			throw new PolyhedronException("Exception polyhedra error: " + e.getMessage());
		}
		/*
		 * Count edges and faces, update density and characteristic if needed.
		 */
		try {
			count();
		} catch (PolyhedronException e) {
			throw new PolyhedronException("Count edges and faces error: " + e.getMessage());
		}
		/*
		 * Generate printable vertex configuration.
		 */
		try {
			configuration();
		} catch (PolyhedronException e) {
			throw new PolyhedronException("Generate vertex configuration error: " + e.getMessage());
		}
		/*
		 * Compute coordinates.
		 */
		if (!need_coordinates && !need_edgelist) {
			return;
		}
		try {
			vertices();
		} catch (PolyhedronException e) {
			throw new PolyhedronException("Compute vertices error: " + e.getMessage());
		}
		try {
			faces ();
		} catch (PolyhedronException e) {
			throw new PolyhedronException("Compute faces error: " + e.getMessage());
		}
		/*
		 * Compute edgelist.
		 */
		if (!need_edgelist) {
			return;
		}
		try {
			edgelist();
		} catch (PolyhedronException e) {
			throw new PolyhedronException("Compute edges error: " + e.getMessage());
		}
	}
}
