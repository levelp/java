/**
 * Демонстрация работы Garbage Collection
 */
public class Memory {

    public static void main(String[] args) {
        int mb = 1024 * 1024;

        boolean saveReferences = true;

        // Getting the runtime reference from system
        Runtime runtime = Runtime.getRuntime();

        // Заводим массив в динамической памяти
        int[][] allRefs = new int[100000][];
        for (int i = 0; i < 100000; ++i) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int[] intArray = new int[100000];
            for (int j = 0; j < intArray.length; ++j)
                intArray[j] = j;
            System.out.println("i = " + i);
            if (saveReferences) {
                allRefs[i] = intArray;
            }

            System.out.println("Used: " + (runtime.totalMemory() - runtime.freeMemory()) / mb
                    + " from " + runtime.totalMemory() / mb);
        }
    }
}
