

/**
 * Created by omerh on 4/14/2017.
 */
public class Algorithms {

    public static Algo Algorithm(String type, String mode){
        switch(mode){
            case "Encrypt":
                return subEncrypt;
            case "Decrypt":
                return subDecrypt;
        }
        return null;

    }

    static Algo subEncrypt = (char[] key, char[] block) -> {
        for (int i = 0; i < block.length; i++) {
            if (block[i] >= 'a' && block[i] <= 'h') {
                char c = block[i];
                block[i] = key[c - 97];
            }
        }
        return block;
    };

    static Algo subDecrypt = (char[] key, char[] block) -> {
        for (int i = 0; i < block.length; i++) {
            if (block[i] >= 'a' && block[i] <= 'h') {
                char c = block[i];
                int dec = 0;
                while(key[dec]!=c)
                    dec++;
                block[i] = (char) (97 + dec);
            }
        }
        return block;
    };



}
