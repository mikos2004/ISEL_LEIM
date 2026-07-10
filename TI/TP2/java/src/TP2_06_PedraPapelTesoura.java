import java.util.Random;
import java.util.Scanner;

public class TP2_06_PedraPapelTesoura {
    public static void main(String[] args) {
        Random random = new Random();
        int ppt = random.nextInt(3);
        String ppts;
        if (ppt==0) {
            ppts = ("papel");
        } else if (ppt==1) {
            ppts = ("pedra");
        } else {
            ppts = ("tesoura");
        }
        System.out.println("Escolha pedra, papel ou tesoura! (use minúsculas)");
        Scanner scanner = new Scanner(System.in);
        String ppt2 = scanner.next();
        System.out.println(ppts);
        if (ppt2.equals("papel")) {
            if (ppts.equals(ppt2)) {
                System.out.println("Empataram!");
            } else {
                if (ppts.equals("pedra")) {
                    System.out.println("Ganhou!");
                } else {
                    System.out.println("Perdeu!");
                }
            }
        } else {
            if (ppt2.equals("pedra")) {
                if (ppts.equals(ppt2)) {
                    System.out.println("Empataram!");
                } else {
                    if (ppts.equals("tesoura")) {
                        System.out.println("Ganhou!");
                    } else {
                        System.out.println("Perdeu!");
                    }
                }
            } else {
                if (ppts.equals(ppt2)) {
                    System.out.println("Empataram!");
                } else {
                    if (ppts.equals("papel")) {
                        System.out.println("Ganhou!");
                    } else {
                        System.out.println("Perdeu!");
                    }
                }
            }
        }
    }
}

