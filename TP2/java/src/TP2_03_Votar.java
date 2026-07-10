//TP2 - TP2-03-Votar
import java.util.Scanner;

public class TP2_03_Votar{
    public static void main(String[] args) {
        String nome;
        int anoatual, anonascimento, dif;
        System.out.print("Insira o seu nome: ");
        Scanner scanner = new Scanner(System.in);
        nome = scanner.next();
        System.out.print("Insira o seu ano de nascimento: ");
        anonascimento = scanner.nextInt();
        System.out.print("Insira o ano atual: ");
        anoatual = scanner.nextInt();
        dif=anoatual-anonascimento;

        if (dif>=18){
            System.out.println("\nCaro(a) "+ nome + " informamos que já pode votar.");
        } else {
            System.out.println("\nCaro(a) "+ nome + " informamos que NÃO pode votar.");
        }
    }
}
