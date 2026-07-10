public class TP3_09_SilabasRecursivas {
    public static boolean isVogal(char ch) {
        if (ch=='a'|| ch=='e'|| ch=='i'|| ch=='o'|| ch=='u') {
            return true;
        }
        else {
            return false;
        }
    }
    public static int silabas(String str) {
        if (str.length()==1) {
            return 0;
        }
        else {
            if (isVogal(str.charAt(0))==true && isVogal(str.charAt(1))==false) {
                int cont = 1;
                str=str.substring(1);
                return cont + silabas(str);
            } else if ((isVogal(str.charAt(0))==true && isVogal(str.charAt(1))==true)) {
                int cont =0;
                str=str.substring(2);
                return cont + silabas(str);
            }
        }
        str=str.substring(1);
        return silabas(str);
    }

    public static void main(String[] args) {
        System.out.println(silabas("valor"));
        System.out.println(silabas("biblioteca"));
    }
}
