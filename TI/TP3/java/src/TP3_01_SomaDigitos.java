public class TP3_01_SomaDigitos {
    public static int somaDigitos(int n) {
        if (n==0) {
            return 0;
        } else {
            return n%10 + somaDigitos(n/10);
        }
    }

    public static void main(String[] args) {
        System.out.println(somaDigitos(1234));
    }

}
