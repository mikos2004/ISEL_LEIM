public class TP3_03_EscreveDigitos {
public static String getDigitoEmString(int digito) {
        if (digito==0) {
        return "zero ";
        }
        if (digito==1) {
        return "um ";
        }
        if (digito==2) {
        return "dois ";
        }
        if (digito==3) {
        return "três ";
        }
        if (digito==4) {
        return "quatro ";
        }
        if (digito==5) {
        return "cinco ";
        }
        if (digito==6) {
        return "seis ";
        }
        if (digito==7) {
        return "sete ";
        }
        if (digito==8) {
        return "oito ";
        }
        if (digito==9) {
        return "nove ";
        }
        return "";
        }

public static int getDigito(int n, int i) {
        int digito = 0;
        for (int j=0; j<i+1; j++) {
        digito=n%10;
        n=n/10;
        }
        return digito;
        }

public static int getNumDigitos(int n) {
        int cont = 0;
        while (n!=0) {
        n = n/10;
        ++cont;
        }
        return cont;
        }

public static String mostraDigitos(int n) {
        String numerostring = "";
        for(int j=0; j<getNumDigitos(n);j++) {
        numerostring = getDigitoEmString(getDigito(n,j)) + numerostring;
        }
        return numerostring;
        }

public static void main(String[] args) {
        System.out.println(mostraDigitos(2345));
        }
}
