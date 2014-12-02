package minmax;

import java.util.Comparator;

/**
 * User: gkislin
 * Date: 30.06.2014
 */
public class MinMax<T> {

    private T[] array;

    public MinMax(T[] array) {
        this.array = array;
    }

    public class Pair {
        public T min;
        public T max;

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
            if (min != null ? !min.equals(pair.min) : pair.min != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = min != null ? min.hashCode() : 0;
            result = 31 * result + (max != null ? max.hashCode() : 0);
            return result;
        }
    }

    public Pair calculate() {
        return calculate(new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                return ((Comparable<T>) o1).compareTo(o2);
            }
        });
    }

    public Pair calculate(Comparator<T> cmp) {
        if (array.length == 0) {
            return new Pair(null, null);
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
        return new Pair(min, max);
    }
}
