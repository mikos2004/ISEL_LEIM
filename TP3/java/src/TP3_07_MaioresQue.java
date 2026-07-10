import java.util.Arrays;

public class TP3_07_MaioresQue {
public static int maioresQue(int[]array, int val) {
        if (array.length==0) {
            return 0;
        }
        else {
            if(array[0]>val) {
            int count=1;
            array = Arrays.copyOfRange(array, 1, array.length);
            return count + maioresQue(array,val);
        }
        else{
            int count=0;
            array = Arrays.copyOfRange(array, 1, array.length);
            return count + maioresQue(array,val);
            }
        }
}

public static void main(String[] args) {
        int[] array = {1, 2, 3, 4, 5};
        System.out.println(maioresQue(array, 3));
        }
        }