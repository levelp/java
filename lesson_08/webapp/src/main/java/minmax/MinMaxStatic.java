package minmax;

import java.util.Comparator;

/**
 * User: gkislin
 * Date: 01.07.2014
 */
public class MinMaxStatic {
    private static final Pair EMPTY = new Pair<>(null, null);

    public static class Pair<T> {

        public final T min;
        public final T max;

        public Pair(T min, T max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public String toString() {
            return "Pair{" +
                    "min=" + min +
                    ", max=" + max +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Pair pair = (Pair) o;

            if (max != null ? !max.equals(pair.max) : pair.max != null) return false;
            return !(min != null ? !min.equals(pair.min) : pair.min != null);

        }

        @Override
        public int hashCode() {
            int result = min != null ? min.hashCode() : 0;
            result = 31 * result + (max != null ? max.hashCode() : 0);
            return result;
        }
    }

    public static <T> Pair<T> calculate(T[] array) {
        return calculate(new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                return ((Comparable<T>) o1).compareTo(o2);
            }
        }, array);
    }

    public static <T> Pair<T> calculate(Comparator<T> cmp, T[] array) {
        if (array.length == 0) {
            return (Pair<T>) EMPTY;
        }
        T min = array[0], max = array[0];
        for (T e : array) {
            if (cmp.compare(e, max) > 0) {
                max = e;
            }
            if (cmp.compare(e, min) < 0) {
                min = e;
            }
        }
        return new Pair<>(min, max);
    }
}