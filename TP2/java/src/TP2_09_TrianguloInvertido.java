import java.util.Scanner;

public class TP2_09_TrianguloInvertido {
    public static void main(String[] args) {
        int i, a,  n, nc;
        String card = "#";
        String esp = " ";

        System.out.print("Insira um número de linhas: ");
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();

        for(i=n; i>0; i--){
            nc=1+(i-1)*2;
            int ne = n-i;
            System.out.println(esp.repeat(ne)+card.repeat(nc));
            }
        }
    }
