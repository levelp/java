import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 *
 */
public class ArrayListSizeDemo {
    private static ArrayList<Integer> arrayList;

    public static void main(String[] args) throws Exception {
        arrayList = new ArrayList<Integer>();
        arrayList.ensureCapacity(100);
        for (int i = 1; i < 40; i++)
            add(i);
    }

    private static void add(int i) throws NoSuchFieldException, IllegalAccessException {
        arrayList.add(i);
        System.out.println("size() = " + arrayList.size());
        Field field = arrayList.getClass().getDeclaredField("elementData");
        field.setAccessible(true);
        Object[] ints = (Object[]) field.get(arrayList);
        System.out.println("capacity = " + ints.length);
    }
}
