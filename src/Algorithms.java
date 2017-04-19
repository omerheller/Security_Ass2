

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

    static Algo subEncrypt = (byte[] key, byte[] block) -> {
        for (int i = 0; i < block.length; i++) {
            if (block[i] >= 'a' && block[i] <= 'h') {
                byte c = block[i];
                block[i] = key[c - 97];
                //System.out.println("I just turned a "+c+" to a "+key[c-97]);
            }
        }
        return block;
    };

    static Algo subDecrypt = (byte[] key, byte[] block) -> {
        for (int i = 0; i < block.length; i++) {
            if (block[i] >= 'a' && block[i] <= 'h') {
                byte c = block[i];
                int dec = 0;
                while(key[dec]!=c)
                    dec++;
                block[i] = (byte) (97 + dec);
               // System.out.println("I just turned a "+c+" to a "+(char)(97+dec));
            }
        }
        return block;
    };



}
