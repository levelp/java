package com.demo;

public class Main {

    public static void main(String[] args) {
        chain();
        chainWithBrackets();
        chainAsExpected();
    }

    private static void chain() {
        int a = 11, b = 3;
        a ^= b ^= a ^= b;
        System.out.println("a = " + a + " b = " + b); // Неверный ответ: a = 0 b = 11
    }

    private static void chainWithBrackets() {
        int a = 11, b = 3;
        a ^= (b ^= (a ^= b));
        System.out.println("a = " + a + " b = " + b); // Неверный ответ: a = 0 b = 11
    }

    private static void chainAsExpected() {
        int a = 11, b = 3;

        a ^= b;
        b ^= a;
        a ^= b;

        System.out.println("a = " + a + " b = " + b);
    }

}
