import java.util.Scanner;

public class TP2_01_Idade {
    public static void main(String[] args) {
        int ano, dif;
        System.out.println("Em que ano nasceu? ");
        Scanner scanner = new Scanner(System.in);
        ano = scanner.nextInt();
        dif=2022-ano;
        System.out.println("Nasceu em "+ ano + " e em 2022 tem "+ dif + " anos.");
    }
}