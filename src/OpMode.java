/**
 * Created by omerh on 4/14/2017.
 */
import java.util.Base64;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
public class OpMode {


    public static List<char[]> CBC_Encrypt(String algo,List<char[]> blocks,char[] IV,char[] key){
        Algo algorithm = Algorithms.Algorithm(algo,"Encrypt");
        List<char[]> blocksAfterOp = new LinkedList<>();
        char[] xorArg = IV;
        for(char[] block : blocks) {
            byte[] out = new byte[block.length];
            for (int i = 0; i < block.length; i++) {
                out[i] = (byte) (block[i] ^ IV[i]);
            }
            char[] outDecode = base64Encode(out).toCharArray();
            char[] blockAfterAlgo = algorithm.operation(key, outDecode);
            IV = blockAfterAlgo;
            blocksAfterOp.add(blockAfterAlgo);
        }
        return blocksAfterOp;
    }

    private static String base64Encode(byte[] bytes) {
        Base64.Encoder enc = Base64.getEncoder();
        return enc.encodeToString(bytes).replaceAll("\\s", "");

    }

    private static byte[] base64Decode(String s) {
        try {
            Base64.Decoder d = Base64.getDecoder();
            return d.decode(s);
        } catch (Exception e) {throw new RuntimeException(e);}
    }

    public static List<char[]> CBC_Decrypt(String algo,List<char[]> blocks,char[] IV,char[] key){
        List<char[]> newBlocks = new ArrayList<>();
        for(char[] block : blocks){
            newBlocks.add(Arrays.copyOf(block,block.length));
        }
        Algo algorithm = Algorithms.Algorithm(algo,"Decrypt");
        List<char[]> blocksAfterOp = new LinkedList<>();
        char[] new_IV;
        for(char[] block : newBlocks) {
            new_IV = new char[block.length];
            for(int i=0;i<block.length;i++){
                new_IV[i] = block[i];
            }
            char[] blockAfterAlgo = algorithm.operation(key, block);
            byte[] out = base64Decode(new String(blockAfterAlgo));
            char[] xorArg = IV;

            for (int i = 0; i < out.length; i++) {
                blockAfterAlgo[i] = (char) (out[i] ^ IV[i]);
            }
            IV = new_IV;
            blocksAfterOp.add(blockAfterAlgo);
        }
        return blocksAfterOp;
    }
}
