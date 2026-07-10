import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class TP2_15_SomaMatrizes {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        System.out.println("Insira um número inteiro que será o número de colunas e elementos de duas matrizes.");
        int n = scanner.nextInt();
        int[][] a1 = new int[n][n];
        int[][] a2 = new int[n][n];
        for (int i = 0; i < a1.length; i++) {
            for (int j=0; j< a1[i].length; j++) {
                a1[i][j]= random.nextInt(10);
            }
        }
        for (int i = 0; i < a2.length; i++) {
            for (int j=0; j< a2[i].length; j++) {
                a2[i][j]= random.nextInt(10);
            }
        }
        System.out.println("Matriz 1 = " + Arrays.deepToString(a1));
        System.out.println("Matriz 2 = " + Arrays.deepToString(a2));
        int[][] soma = new int [n][n];
        for (int i = 0; i < a1.length; i++) {
            for (int j=0; j< a1[i].length; j++) {
                soma[i][j]=a1[i][j]+a2[i][j];
            }
        }
        System.out.println("Soma das matrizes = " + Arrays.deepToString(soma));
    }
}
