public class TP2_13_MergeSorted {
    public static void main(String[] args){
        int[] array1 = new int[]{10, 12, 17, 20, 21};
        int[] array2 = new int[]{9, 13, 19, 22, 25};
        System.out.print("Array 1 = ");
        for (int i = 0; i < array1.length; i++) {
            System.out.print(array1[i] + " ");
        }
        System.out.print("\nArray 2 = ");
        for (int i = 0; i < array2.length; i++) {
            System.out.print(array2[i] + " ");
        }
        int a1 = array1.length;
        int a2 = array2.length;
        int a3 = a1 + a2;
        int[] array3 = new int[a3];
        for (int i = 0; i < a1; i++) {
            array3[i] = array1[i];
        }
        for (int i = 0; i < a2; i++) {
            array3[a1 + i] = array2[i];
        }
        boolean troca=true;
        int recebe;
        while (troca == true) {
            troca = false;
            for (int i = 0; i < a3 -1; i++) {
                if (array3[i] > array3[i + 1]) {
                    recebe = array3[i + 1];
                    array3[i + 1] = array3[i];
                    array3[i] = recebe;
                    troca = true;
                }
            }
        }
        System.out.print("\nLista com todos os elementos = ");
        for (int i = 0; i < a3; i++) {
            System.out.print(array3[i] + " ");
        }
    }
}

