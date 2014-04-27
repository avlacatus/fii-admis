package ro.infoiasi.fiiadmis.util;

public class Pair<X, Y> {

    private X first;
    private Y second;

    public Pair(X first, Y second) {
        this.first = first;
        this.second = second;
    }

    public X getFirst() {
        return first;
    }

    public void setFirst(X first) {
        this.first = first;
    }

    public Y getSecond() {
        return second;
    }

    public void setSecond(Y second) {
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair pair = (Pair) o;

        if (!first.equals(pair.first)) return false;
        if (!second.equals(pair.second)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = first.hashCode();
        result = 31 * result + second.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Pair{");
        sb.append("first=").append(first);
        sb.append(", second=").append(second);
        sb.append('}');
        return sb.toString();
    }
}
