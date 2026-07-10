public class TP3_02_Arrays {

    public static int max(int[] array){
        int M=Integer.MIN_VALUE;
        for(int i=0; i<array.length; i++){
            if(array[i]>M)
                M = array[i];
        }
        return M;
    }

    public static int min(int[] array){
        int m=Integer.MAX_VALUE;
        for(int i=0; i<array.length; i++){
            if(array[i]<m)
                m = array[i];
        }
        return m;
    }

    public static int sum(int[] array){
        int soma = 0;
        for (int i=0; i< array.length; i++){
            soma=soma+array[i];
        }
        return soma;
    }

    public static double avg(int[] array){
        double t= sum(array);
        double med= t/(array.length);
        return med;
    }

    public static void main(String[] args) {
        int[] a = {1, 2, 5, 8, 6, 3, 4,7, 9, 0};
        System.out.println(max(a));
        System.out.println(min(a));
        System.out.println(sum(a));
        System.out.println(avg(a));
    }
}
