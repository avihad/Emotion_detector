package idc.cv.emotiondetector;

public class Pair<A, B>
{
    public final A first;
    public final B second;

    private Pair(A paramA, B paramB)
    {
        this.first = paramA;
        this.second = paramB;
    }

    public static <A, B> Pair<A, B> of(A paramA, B paramB)
    {
        return new Pair<A, B>(paramA, paramB);
    }

    public String toString()
    {
        return "Pair[" + this.first + "," + this.second + "]";
    }

    private static boolean equals(Object paramObject1, Object paramObject2)
    {
        return (((paramObject1 == null) && (paramObject2 == null)) || ((paramObject1 != null) && (paramObject1.equals(paramObject2))));
    }

    public boolean equals(Object paramObject)
    {
        return ((paramObject instanceof Pair) && (equals(this.first, ((Pair) paramObject).first)) && (equals(this.second, ((Pair) paramObject).second)));
    }

    public int hashCode()
    {
        if (this.first == null)
            return ((this.second == null) ? 0 : this.second.hashCode() + 1);
        if (this.second == null)
            return (this.first.hashCode() + 2);
        return (this.first.hashCode() * 17 + this.second.hashCode());
    }

}
