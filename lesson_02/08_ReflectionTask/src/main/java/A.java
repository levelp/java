public class A {
    int X = 2;
    public int publicField = 3;
    private int privateField = 4;

    public A() {
        System.out.println("Constructor A");
    }

    public void draw() {
        System.out.println("A.draw");
    }

    public int newMethod() {
        System.out.println("A.newMethod");
        return 1;
    }

    public static int classMethod() {
        System.out.println("A.classMethod");
        return 101;
    }

    public static void newStaticMethod() {
        System.out.println("A.newStaticMethod");
        for (int i = 0; i < 10; i++)
            System.out.println("i^2 = " + (i * i));
    }

}
