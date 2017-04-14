/**
 * Created by omerh on 4/14/2017.
 */
import java.util.LinkedList;
import java.util.List;
public class OpMode {


    public static List<char[]> CBC_Encrypt(String algo,List<char[]> blocks,char[] IV,char[] key){
        Algo algorithm = Algorithms.Algorithm(algo,"Encrypt");
        List<char[]> blocksAfterOp = new LinkedList<>();
        char[] xorArg = IV;
        for(char[] block : blocks) {
            for (int i = 0; i < block.length; i++) {
                block[i] = (char) (block[i] ^ IV[i]);
            }
            char[] blockAfterAlgo = algorithm.operation(key, block);
            IV = blockAfterAlgo;
            blocksAfterOp.add(blockAfterAlgo);
        }
        return blocksAfterOp;
    }

    public static List<char[]> CBC_Decrypt(String algo,List<char[]> blocks,char[] IV,char[] key){
        Algo algorithm = Algorithms.Algorithm(algo,"Decrypt");
        List<char[]> blocksAfterOp = new LinkedList<>();
        char[] new_IV;
        for(char[] block : blocks) {
            new_IV = new char[block.length];
            for(int i=0;i<block.length;i++){
                new_IV[i] = block[i];
            }
            char[] blockAfterAlgo = algorithm.operation(key, block);
            char[] xorArg = IV;

            for (int i = 0; i < blockAfterAlgo.length; i++) {
                blockAfterAlgo[i] = (char) (blockAfterAlgo[i] ^ IV[i]);
            }
            IV = new_IV;
            blocksAfterOp.add(blockAfterAlgo);
        }
        return blocksAfterOp;
    }
}
