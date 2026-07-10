import java.util.Scanner;

public class TP2_07_ListaNumeros {
    public static void main(String[] args) {
        int n1, n2;
        String escolha;
        String par = "par";
        String impar = "impar";

        System.out.print("Insira um número: ");
        Scanner scanner = new Scanner(System.in);
        n1 = scanner.nextInt();
        System.out.print("Insira outro número maior: ");
        n2 = scanner.nextInt();
        System.out.println("Escolha 'par' ou 'impar': ");
        escolha = scanner.next();

        if (escolha.equals(par)){
            for(int i=n1; i<=n2; i++)
                if (i%2==0){
                    System.out.print(i+ " ");
                }
        }else {
            for(int i=n1; i<=n2; i++)
                if (i%2!=0){
                    System.out.print(i+ " ");
                }
        }
    }
}
