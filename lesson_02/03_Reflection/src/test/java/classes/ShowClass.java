package classes;

/**
 *
 */
public class ShowClass {

    public static void main(String[] args) throws
            Exception {
        showType("Test");
        showType(1);
        showType(1.1);
        showType(1.1f);
        showType(1L);
        showType('A');
        showType(new ShowClass());
        showType(new Integer(2));
    }

    private static void showType(Object obj)
            throws Exception {
        // По объекту получаем класс
        Class c = obj.getClass();
        // Значение и имя класса
        System.out.println("value = " + obj +
                "  ->  " + c.getName());

        try {
            // Получаем класс по имени класса
            Class<?> aClass = Class.forName(c.getName());
            // Пытаемся создать экземпляр класса
            Object obj2 = aClass.newInstance();
            // Выводим экземпляр класса
            System.out.println("obj2 = " + obj2);
        } catch (InstantiationException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
