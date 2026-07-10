import java.util.Random;
import java.util.Scanner;

public class TP2_05_Dados{
    public static void main(String[] args) {
        int x, num;

        Random random = new Random();
        x = random.nextInt(6)+1;
        System.out.print("Insira um número entre 1 e 6: ");
        Scanner scanner = new Scanner(System.in);
        num = scanner.nextInt();

        if (x==num){
            System.out.print("Acertou, parabéns!!!");
        }else{
            while (x!=num){
                System.out.print("Insira outro número entre 1 e 6: ");
                num = scanner.nextInt();
            }
            System.out.print("Acertou, parabéns!!!");
        }
    }
}
