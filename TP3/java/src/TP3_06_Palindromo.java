import java.util.Scanner;

public class TP3_06_Palindromo {
    public static boolean palindromo(String str){
        boolean c;
        String strUp = str.toUpperCase();
        String str2;
        char ch1, ch2;
        ch1 = strUp.charAt(0);
        ch2 = strUp.charAt((strUp.length()-1));

        if(ch1==ch2){ // Verfica se o primeiro char e ultimo char sao iguais
            c= true;
        }else {
            c= false;
        }

        if (strUp.length()>2){
            str2=strUp.substring(1, strUp.length()-1);
        }else {
            return c;
        }
        c = c && palindromo(str2);
        return c;
    }

    public static void main(String[] args) {
        System.out.print("Insira uma palavra: ");
        Scanner scanner = new Scanner(System.in);
        String str = scanner.next();

        if (palindromo(str)==true){
            System.out.println("É um palindromo.");
        }else {
            System.out.println("Não é um palindromo.");
        }
    }
}

