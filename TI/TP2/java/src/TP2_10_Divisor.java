import java.util.Scanner;

public class TP2_10_Divisor {
    public static void main(String[] args) {
        int n1, n2, cont=0;
        System.out.print("Insira o dividendo: ");
        Scanner scanner = new Scanner(System.in);
        n1 = scanner.nextInt();
        System.out.print("Insira o divisor: ");
        n2 = scanner.nextInt();

        while (n1>=n2){
            n1=n1-n2;
            cont=cont+1;
        }

        System.out.println("O quociente da divisão é: "+ cont + " com resto: "+ n1);
    }
}

