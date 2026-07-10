import java.util.Random;
import java.util.Scanner;

public class TP2_08_Normalizar{
    public static void main(String[] args) {
        System.out.println("Escreva o seu nome e o programa irá normalizá-lo");
        Scanner scanner = new Scanner(System.in);
        String nome = scanner.nextLine();
        String nomef = ("");
        int pl = nome.charAt(0);
        if (pl > 64 && pl < 91) {
            nomef = nomef + (char) pl;
        } else {
            pl = pl - 32;
            nomef = nomef + (char) pl;
        }
        int i = 1;
        while (i < nome.length()) {
            int cn = nome.charAt(i);
            if (cn > 96 && cn < 123) {
                nomef = nomef + (char) cn;
                i = i + 1;
            } else if (cn > 64 && cn < 91) {
                cn = cn + 32;
                nomef = nomef + (char) cn;
                i = i + 1;
            } else if (cn == 32) {
                nomef = nomef + (char) cn;
                int ncn = nome.charAt(i + 1);
                if ((nome.charAt(i + 1) =='d' || nome.charAt(i + 1) =='D') && (nome.charAt(i + 2)=='o' || nome.charAt(i + 2)=='O')  && (nome.charAt(i + 3) == 's' || nome.charAt(i + 3)=='S')) {
                    nomef = nomef + 'd' + 'o' + 's';
                    i = i + 4; }
                else if ((nome.charAt(i + 1) =='d' || nome.charAt(i + 1) =='D') && (nome.charAt(i + 2)=='a' || nome.charAt(i + 2)=='A')  && (nome.charAt(i + 3) == 's' || nome.charAt(i + 3)=='S')) {
                    nomef = nomef + 'd' + 'a' + 's';
                    i = i + 4; }
                else if ((nome.charAt(i + 1) =='d' || nome.charAt(i + 1) =='D') && (nome.charAt(i + 2)=='o' || nome.charAt(i + 2)=='O')) {
                    nomef = nomef + 'd' + 'o';
                    i=i+3;}
                else if ((nome.charAt(i + 1) =='d' || nome.charAt(i + 1) =='D') && (nome.charAt(i + 2)=='a' || nome.charAt(i + 2)=='A')) {
                    nomef = nomef + 'd' + 'a';
                    i=i+3;}
                else if ((nome.charAt(i + 1) =='d' || nome.charAt(i + 1) =='D') && (nome.charAt(i + 2)=='e' || nome.charAt(i + 2)=='E')) {
                    nomef = nomef + 'd' + 'e';
                    i=i+3;}
                else if ((nome.charAt(i + 1) =='e' || nome.charAt(i+1) =='E') && nome.charAt(i + 2)==' ') {
                    nomef = nomef + 'e';
                    i=i+2;}
                else if (ncn > 64 && ncn < 91) {
                    nomef = nomef + (char) ncn;
                    i = i + 2;}
                else {
                    ncn = ncn - 32;
                    nomef = nomef + (char) ncn;
                    i = i + 2;
                }
            }
        } System.out.println(nomef);
    }
}
