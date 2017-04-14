import java.util.LinkedList;
import java.util.List;

/**
 * Created by omerh on 4/14/2017.
 */
public class Main {
    public static void main(String[] args){
        char[] plaintext1 = {'h','e','l','l','o','_','w','o','r','d'};
        char[] plaintext2 = {'d','l','a','i','n','_','t','e','x','t'};
        List<char[]> blocks = new LinkedList<>();
        blocks.add(plaintext1);
        blocks.add(plaintext2);
        char[] IV = {0,0,0,0,0,0,0,0,0,0};
        char[] key = {'b', 'a', 'c', 'd', 'e', 'g', 'h', 'f'};
        List values = OpMode.CBC_Encrypt("sub",blocks,IV,key);
        System.out.println("finished encrypting");
        List<char []> values2 = OpMode.CBC_Decrypt("sub",values,IV,key);
        System.out.println("finished decrypting");
        for(char[] b : values2)
        System.out.println(new String(b));


    }
}
