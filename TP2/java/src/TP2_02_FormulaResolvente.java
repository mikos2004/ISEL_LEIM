//TP2 - TP2-02-FormulaResolvente
import java.util.Scanner;

public class TP2_02_FormulaResolvente{
    public static void main(String[] args) {
        int a, b, c;
        double frp, frn;
        System.out.print("Insira os valores para resolver a função ax2+bx+c.\nInsira o valor de a: ");
        Scanner scanner = new Scanner(System.in);
        a = scanner.nextInt();
        System.out.print("Insira o valor de b: ");
        b = scanner.nextInt();
        System.out.print("Insira o valor de c: ");
        c = scanner.nextInt();
        frp=((-b+Math.sqrt(b*b-4*a*c))/(2*a));
        frn=((-b-Math.sqrt(b*b-4*a*c))/(2*a));
        System.out.println("Os zeros da função são: "+ frp + " e " + frn);
    }
}
