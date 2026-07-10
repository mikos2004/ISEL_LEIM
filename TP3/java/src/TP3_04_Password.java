import java.util.Scanner;

public class TP3_04_Password {

    public static boolean isLetter(char ch) {
        if (ch > 64 && ch < 91) {
            return true;
        } else if (ch > 96 && ch < 123) {
            return true;
        } else {
            return false;
        }
    }


        public static boolean isDigit (char ch){
            if (ch > 47 && ch < 58) {
                return true;
            } else {
                return false;
            }
        }

        public static boolean isValid (String password){
            int contL = 0;
            int contD = 0;
            for (int i = 0; i < password.length(); i++) {
                if (isLetter(password.charAt(i)) == true)
                    contL = contL + 1;
                if (isDigit(password.charAt(i)) == true)
                    contD = contD + 1;
            }
            if (contL >= 3 && contD >= 2) {
                if (password.length() >= 10) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }


        public static void main (String[]args){
            System.out.println("Introduza uma password apenas com letras e números:");
            Scanner scanner = new Scanner(System.in);
            String str = scanner.next();
            System.out.println(isValid(str));
        }
    }
