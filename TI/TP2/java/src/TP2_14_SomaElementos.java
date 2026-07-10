import java.util.Scanner;

public class TP2_14_SomaElementos {
    public static void main(String[] args) {
        int[] l1 = {1, 1, 2, 3, 4, 5, 6, 6};

        System.out.print("Insira um número: ");
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        int i;
        for (i = 0; i < l1.length; i++) {
            for (int j = 1; j < l1.length - 1; j++)
                if (l1[i] + l1[j] == n && i<j) {
                        System.out.print("[" + l1[i] + "+" + l1[j] + "]");
                    }
                }
        }
    }
