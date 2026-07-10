import java.util.Random;
import java.util.Scanner;

public class TP2_11_RandomArray {
    public static void main(String[] args) {
        int n, i, soma=0;

        System.out.print("Insira um número: ");
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        int[] lista = new int[n];

        for (i=0; i<n; i++) {
            Random random = new Random();
            int rand = random.nextInt(101);
            lista[i] = rand;
            System.out.print(lista[i]+" ");
            soma=soma+lista[i];
        }
        System.out.println("\nO somatório é: "+soma);
    }
}
