import java.util.Scanner;

public class TP2_04_MaiordeTres{
    public static void main(String[] args) {
        int a, b, c, M, mn, md;
        System.out.print("Insira o valor de a: ");
        Scanner scanner = new Scanner(System.in);
        a = scanner.nextInt();
        System.out.print("Insira o valor de b: ");
        b = scanner.nextInt();
        System.out.print("Insira o valor de c: ");
        c = scanner.nextInt();

        if (a > b) {
            if (a > c) {
                M = a;
                if (b > c) {
                    md = b;
                    mn = c;
                } else {
                    md = c;
                    mn = b;
                }
            } else {
                md = a;
                if (b > c) {
                    M = b;
                    mn = c;
                } else {
                    M = c;
                    mn = b;
                }
            }
        } else {
            if (b > c) {
                M = b;
                if (a > c) {
                    md = a;
                    mn = c;
                } else {
                    md = c;
                    mn = a;
                }
            } else {
                M = c;
                if (a > b) {
                    md = a;
                    mn = b;
                } else {
                    md = b;
                    mn = a;
                }
            }
        }
        System.out.println("O maior é: "+ M);
        System.out.println("O do meio é: "+ md);
        System.out.println("O menor é: "+ mn);
        }
    }

