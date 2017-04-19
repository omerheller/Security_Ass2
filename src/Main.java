import com.swabunga.spell.event.SpellChecker;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

/**
 * Created by omerh on 4/14/2017.
 */
public class Main {
    public static void main(String[] args){
        long start_time = System.nanoTime();
        List<byte[]> blocks = readBlocks("PartB/cipher.txt");
        byte[] IV = readIV("PartB/IV_short.txt");

        //byte[] key = readKey("key_example.txt");
        //List<byte[]> values = OpMode.CBC_Encrypt("sub",blocks,IV,key);
        //System.out.println("finished encrypting");
        //EnglishCheck(values);
        //writeToFile("test55.txt",values);
        //List<byte[]> values = OpMode.CBC_Decrypt("sub",blocks,IV,key);
        //System.out.println("finished decrypting");
        //writeToFile("plain222.txt",values);
        SpellChecker checker = TextOnlyAttack.makeChecker();


        for (byte c : getKey(blocks,IV,checker)){
            System.out.print((char)c);
        }
        System.out.println();
        //System.out.println("finished decrypting");
        System.out.println("RunTime: "+((System.nanoTime() - start_time)/1000000)/1000+"sec");
        //for(char[] b : values2)
        //System.out.print(new String(b));


    }

    private static byte[] getKey(List<byte[]> blocks,byte[] IV,SpellChecker checker){
        byte[] keyBasic = new byte[]{'a','b','c','d','e','f','g','h'};
        byte[] keyPermutation = new byte[8];
        byte[] key = new byte[8];
        byte[] master_key = new byte[8];
        int maxWords = 0;

        Permutations iter = Permutations.create(keyBasic,keyPermutation);
        while(iter.next()){
            for(int i=0;i<key.length;i++)
                key[i] = keyPermutation[i];
            boolean flag = true;
            for(int i=0;i<key.length;i++)
                if(true)
                    flag=false;
            if(flag){
                //System.out.println("checking right key now");
            }
            List<byte[]> sublist;
            if(blocks.size()>500){
                sublist = blocks.subList(blocks.size()/2,blocks.size()/2+200);
            }
            else{
                sublist = blocks.subList(0,(blocks.size()>200) ? 200 : blocks.size());
            }
            //System.out.println("im going from "+blocks.size()/2+" to "+end);
            List<byte []> values3 = OpMode.CBC_Decrypt("sub",sublist,IV,key);
            int value = 0;
            if(flag)
                   value =  EnglishCheck(values3,checker,false);
            else
                value =  EnglishCheck(values3,checker,false);
            if(value>maxWords){
                //System.out.println("changed max words to "+value);
                maxWords = value;
                for(int i=0;i<master_key.length;i++)
                    master_key[i] = keyPermutation[i];
            }
        }
        return master_key;
    }

    private static int EnglishCheck(List<byte[]> blocks,SpellChecker checker,boolean print) {
        String text = "";
        byte[] data = new byte[1000];
        int data_index=0;
        for(int i=0;i<100 && i<blocks.size();i++){
            for(int j=0;j<10;j++){
                data[data_index] = blocks.get(i)[j];
                data_index++;
            }
        }

        try {

            text += new String(data,StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println("got caught trying");
            e.printStackTrace();
        }

        List<String> words = TextOnlyAttack.breakToWords(text);
        Map<String, Long> collect =
                words.stream().collect(groupingBy(Function.identity(), counting()));
        LinkedHashMap<String, Long> countByWordSorted = TextOnlyAttack.sortCollect(collect);

        return TextOnlyAttack.checkWords(countByWordSorted,checker,print);
    }


    public static byte[] readKey(String keyfile){
        char[] key = new char[8];
        try (BufferedReader br = new BufferedReader(new FileReader(keyfile))) {

            String sCurrentLine;
            int i=0;

            while ((sCurrentLine = br.readLine()) != null) {
                key[(int)sCurrentLine.charAt(0)-97] = sCurrentLine.charAt(2);
                i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(key).getBytes();
    }

    public static byte[] readIV(String IVfile){
        byte[] IV = new byte[10];
        try (RandomAccessFile br = new RandomAccessFile(IVfile, "r")) {
            int i=0;

            int r;
            while ((r = br.read()) != -1) {
                IV[i] = (byte) r;
                i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return IV;
    }

    public static List<byte[]> readBlocks(String file){
        List<byte[]> blocks = new ArrayList<>();

        File fileDir = new File(file);
        try (RandomAccessFile br = new RandomAccessFile(file, "r")) {
            byte[] block = new byte[10];
            int r;
            while((r = br.read(block,0,10)) != -1){
                if (r < 10){
                    for(int i=r;i<10;i++){
                        block[i] = (byte)0;
                    }
                }
                blocks.add(block);
                block = new byte[10];
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return blocks;
    }

    public static void writeToFile(String dest,List<byte []> contents){

        try (RandomAccessFile bw = new RandomAccessFile(dest, "rw")) {

            for(byte[] content : contents)
                bw.write(content);

            // no need to close it.
            //bw.close();

            System.out.println("Done writing");

        } catch (IOException e) {

            e.printStackTrace();

        }

    }


}
