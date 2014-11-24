import java.util.Random;

/**
 * Декартово дерево поиска
 */
public class Treap<T extends Comparable> implements SearchTree<T> {

    /**
     * Добавление нового значения в дерево поиска
     *
     * @param value добавляемое значение
     */
    @Override
    public void add(T value) {
        // TODO: join + split
    }

    /**
     * Поиск значения в дереве поиска
     *
     * @param value Значение
     * @return найдено ли значение?
     */
    @Override
    public boolean find(T value) {
        return false;
    }

    @Override
    public int deep() {
        return 0;
    }


    public int x;
    public int y;

    public Treap Left;
    public Treap Right;
    static private Random rand = new Random();

    private Treap(int x, int y) {
        this.x = x;
        this.y = y;
        this.Left = null;
        this.Right = null;
    }

    private Treap(int x, int y, Treap left, Treap right) {
        this.x = x;
        this.y = y;
        this.Left = left;
        this.Right = right;
    }

    // здесь будут операции...

    public Treap[] Split(int x) {
        Treap newTree = null;
        Treap L, R;
        if (this.x <= x) {
            if (Right == null)
                R = null;
            else {
                Treap[] gg = Right.Split(x);
                newTree = gg[0];
                R = gg[1];
            }
            L = new Treap(this.x, y, Left, newTree);
        } else {
            if (Left == null)
                L = null;
            else {
                Treap[] gg = Left.Split(x);
                L = gg[0];
                newTree = gg[1];

            }
            R = new Treap(this.x, y, newTree, Right);
        }
        return new Treap[]{L, R};
    }

    public Treap Add(int x) {
        Treap l, r;
        Treap[] hh = Split(x);
        l = hh[0];
        r = hh[1];
        Treap m = new Treap(x, rand.nextInt());
        return Merge(Merge(l, m), r);
    }

    /**
     * Слияение
     *
     * @param L
     * @param R
     * @return
     */
    public static Treap Merge(Treap L, Treap R) {
        if (L == null) return R;
        if (R == null) return L;

        if (L.y > R.y) {
            Treap newR = Merge(L.Right, R);
            return new Treap(L.x, L.y, L.Left, newR);
        } else {
            Treap newL = Merge(L, R.Left);
            return new Treap(R.x, R.y, newL, R.Right);
        }
    }

    public Treap Remove(int x) {
        Treap l, m, r;
        Treap[] gg = Split(x - 1);
        l = gg[0];
        r = gg[1];
        r.Split(x, out m, out r);
        return Merge(l, r);
    }
}
