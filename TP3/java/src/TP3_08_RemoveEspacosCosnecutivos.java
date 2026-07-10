import java.util.Scanner;

public class TP3_08_RemoveEspacosCosnecutivos {
    public static String removeEspacosConsecutivos(String str) {
        char emp=' ';
        String str2="";
        String strf="";
        if (str.length()==0){
            return strf;
        }
        if (str.charAt(0)==emp && str.charAt(1)==emp){
            str2=str.substring(1);
            strf= strf+ removeEspacosConsecutivos(str2);
        }else{
            str2=str.substring(1);
            strf= strf + str.charAt(0)+ removeEspacosConsecutivos(str2);
        }
        return strf;
    }

    public static void main(String[] args) {
        System.out.println("Insira uma palavra:");
        Scanner scanner = new Scanner(System.in);
        String frase = scanner.nextLine();
        System.out.println(removeEspacosConsecutivos(frase));
    }
}
