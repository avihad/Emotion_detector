package idc.cv.emotiondetector;

public class Pair<A, B> {
	public final A	fst;
	public final B	snd;

	private Pair(A paramA, B paramB) {
		this.fst = paramA;
		this.snd = paramB;
	}

	public static <A, B> Pair<A, B> of(A paramA, B paramB) {
		return new Pair(paramA, paramB);
	}

	public String toString() {
		return "Pair[" + this.fst + "," + this.snd + "]";
	}

	private static boolean equals(Object paramObject1, Object paramObject2) {
		return (((paramObject1 == null) && (paramObject2 == null)) || ((paramObject1 != null) && (paramObject1.equals(paramObject2))));
	}

	public boolean equals(Object paramObject) {
		return ((paramObject instanceof Pair) && (equals(this.fst, ((Pair) paramObject).fst)) && (equals(this.snd, ((Pair) paramObject).snd)));
	}

	public int hashCode() {
		if (this.fst == null)
			return ((this.snd == null) ? 0 : this.snd.hashCode() + 1);
		if (this.snd == null)
			return (this.fst.hashCode() + 2);
		return (this.fst.hashCode() * 17 + this.snd.hashCode());
	}

}
