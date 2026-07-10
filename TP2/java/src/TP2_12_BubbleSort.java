import java.util.Random;
import java.util.Scanner;

public class TP2_12_BubbleSort {
    public static void main(String[] args) {
        int n, i, j = 0, recebe;
        boolean troca = true;

        System.out.print("Insira um número: ");
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        int[] lista = new int[n];

        for (i = 0; i < n; i++) {
            Random random = new Random();
            int rand = random.nextInt(100) + 1;
            lista[i] = rand;
        }

        while (troca == true) {
            troca=false;
            for (i = 0; i < n-1; i++) {
                if (lista[i] > lista[i + 1]) {
                    recebe = lista[i + 1];
                    lista[i + 1] = lista[i];
                    lista[i] = recebe;
                    troca = true;
                }
            }
        }
        for (i = 0; i < n; i++) {
            System.out.print(lista[i] + " ");
        }
    }
}
