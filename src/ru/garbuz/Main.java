package ru.garbuz;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String result = "";
        Scanner scanner = new Scanner(System.in);
        for (;;) {
            System.out.print("Введите выражение: ");
            String stringExpression = scanner.nextLine();
            try {
                MyExpression expression = new MyExpression(stringExpression);
                switch (expression.getResultType()){
                    case ROMAN:
                        result = expression.getRomanResult();
                        break;
                    case ARABIC:
                        result = expression.getArabicResult();
                        break;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage() + ". Программа будет закрыта");
                scanner.close();
                System.exit(100);
            }
            System.out.println("Результат: " + result);
        }
    }
}
