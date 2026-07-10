import java.util.Scanner;

public class TP3_10_Normalizar {

    public static  String normalizar(String str){
        String nomet2;                                  //String que deixa de lado a 1ª letra
        String nomef="";                                //String que recebra o nome normalizado
        if (str.length()==0){
            return nomef;
        }
        if(str.charAt(0)==' '){
            int ch = str.charAt(1);
            ch = ch-32;
            char M = (char) ch;

            nomet2=str.substring(2);
            nomef=nomef+str.charAt(0)+M+normalizar(nomet2);
            return nomef;
        }else {
            nomet2=str.substring(1);
            nomef=nomef+str.charAt(0)+normalizar(nomet2);
            return nomef;
        }

    }

    public static String removeEspacosConsecutivos(String str) {
        char emp=' ';
        String str2="";
        String strf="";
        if (str.length()==0){
            return strf;
        }
        if (str.charAt(0)==emp && str.charAt(1)==emp){
            str.replace(str.charAt(0),Character.MIN_VALUE); //troca todos os espaços por char vazios
            str2=str.substring(1);
            strf= strf+ removeEspacosConsecutivos(str2);
        }else{
            str2=str.substring(1);
            strf= strf + str.charAt(0)+ removeEspacosConsecutivos(str2);
        }
        return strf;
    }

    public static void main(String[] args) {
        System.out.println("Insira um nome:");
        Scanner scanner = new Scanner(System.in);
        String nome = scanner.nextLine();
        String strse = removeEspacosConsecutivos(nome);  //remove espaços
        String strdown = strse.toLowerCase();           // poe tudo em minusculas
        String nomet= " "+strdown;                      // coloca um espaço antes
        System.out.println("A forma normalizada de "+ nome + " é" +normalizar(nomet));
    }
}
