public class TP3_05_Silabas {
    public static boolean isVogal(char ch) {
        if (ch=='a'||ch=='e'||ch=='i'||ch=='o'||ch=='u') {
        return true;
        }
        else {
        return false;
        }
    }

    public static int countVogais(String str) {
        int count=0;
        for (int i=0; i<str.length(); i++)
        if (isVogal(str.charAt(i))==true) {
        count= count+1;
        }
        return count;
        }

    public static int countParesVogais(String str) {
        int count=0;
        for (int i=0; i<str.length()-1; i++)
        if (isVogal(str.charAt(i))==true && isVogal(str.charAt(i+1))==true) {
        count=count+1;
        }
        return count;
    }

    public static int silabas(String str) {
        return (countVogais(str)-countParesVogais(str));
    }
    public static void main(String[] args) {
        System.out.println(silabas("baleia"));
        System.out.println(silabas("bibliotecas"));
    }
}
