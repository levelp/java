package com.company;

public class Main {

    public static void main(String[] args) {
        // Перебор (тупая проверка)
        int count = 0;
        for (int i = 0; i < (1 << 10); i++) {
            boolean[] bits = new boolean[10];
            for (int b = 0; b < 10; b++) {
                bits[b] = ((i >> b) & 1) == 1;
            }
            // Проверка условий
            boolean f = false;
            for (int j = 0; j < 8; j++) {
                if (!(bits[j] || bits[j + 1] && bits[j + 2])) {
                    f = true;
                    break;
                }
            }
            if (f) continue;
            // Вывод решения
            String s = "";
            for (boolean bit : bits) {
                s += bit ? "1" : "0";
            }
            count++;
            System.out.println(count + ". " + s);
        }

        // Динамика по количеству переменных
        int[][] cnt = new int[11][4];
        // 3 переменных
        cnt[3][0b00] = 1;
        cnt[3][0b01] = 1;
        cnt[3][0b10] = 1;
        cnt[3][0b11] = 2;

        for (int i = 4; i <= 10; i++) {
            cnt[i][0b00] = cnt[i - 1][0b10];
            cnt[i][0b01] = cnt[i - 1][0b10];
            cnt[i][0b10] = cnt[i - 1][0b11];
            cnt[i][0b11] = cnt[i - 1][0b01] + cnt[i - 1][0b11];

            System.out.println(i + ". " + (cnt[i][0x00] + cnt[i][0b01] + cnt[i][0b10] + cnt[i][0b11]));
        }
    }
}
