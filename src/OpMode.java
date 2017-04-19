/**
 * Created by omerh on 4/14/2017.
 */
import java.util.Base64;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
public class OpMode {


    public static List<byte[]> CBC_Encrypt(String algo,List<byte[]> blocks,byte[] IV,byte[] key){
        Algo algorithm = Algorithms.Algorithm(algo,"Encrypt");
        List<byte[]> blocksAfterOp = new LinkedList<>();
        byte[] xorArg = IV;
        for(byte[] block : blocks) {
            byte[] out = new byte[block.length];
            for (int i = 0; i < block.length; i++) {
                out[i] = (byte) (block[i] ^ IV[i]);
            }
            byte[] blockAfterAlgo = algorithm.operation(key, out);
            IV = blockAfterAlgo;
            blocksAfterOp.add(blockAfterAlgo);
        }
        return blocksAfterOp;
    }


    private static byte[] base64Decode(String s) {
        try {
            Base64.Decoder d = Base64.getDecoder();
            return d.decode(s);
        } catch (Exception e) {throw new RuntimeException(e);}
    }

    public static List<byte[]> CBC_Decrypt(String algo,List<byte[]> blocks,byte[] IV,byte[] key){
        List<byte[]> newBlocks = new ArrayList<>();
        for(byte[] block : blocks){
            newBlocks.add(Arrays.copyOf(block,block.length));
        }
        Algo algorithm = Algorithms.Algorithm(algo,"Decrypt");
        List<byte[]> blocksAfterOp = new LinkedList<>();
        byte[] new_IV;
        for(byte[] block : newBlocks) {
            new_IV = new byte[block.length];
            for(int i=0;i<block.length;i++){
                new_IV[i] = block[i];
            }
            byte[] blockAfterAlgo = algorithm.operation(key, block);
            byte[] xorArg = IV;

            for (int i = 0; i < blockAfterAlgo.length; i++) {
                blockAfterAlgo[i] = (byte) (blockAfterAlgo[i] ^ IV[i]);
            }
            IV = new_IV;
            blocksAfterOp.add(blockAfterAlgo);
        }
        return blocksAfterOp;
    }
}
