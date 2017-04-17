import com.swabunga.spell.event.SpellChecker;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

/**
 * Created by omerh on 4/14/2017.
 */
public class Main {
    public static void main(String[] args){
        Base64.Encoder enc = Base64.getEncoder();
        long start_time = System.nanoTime();
        List<char[]> blocks = readBlocks("test4.txt");
        char[] IV = readIV("PartB/IV_short.txt");

        char[] key = readKey("PartB/key_short.txt");
        //List<char[]> values = OpMode.CBC_Encrypt("sub",blocks,IV,key);
        //System.out.println("finished encrypting");
        //EnglishCheck(values);
        //writeToFile("test4.txt",values);
        List<char[]> values = OpMode.CBC_Decrypt("sub",blocks,IV,key);
        System.out.println("finished decrypting");
        writeToFile("plain.txt",values);
        //SpellChecker checker = TextOnlyAttack.makeChecker();

        //for (char c : getKey(blocks,IV,checker)){
        //    System.out.print(c);
       // }
        System.out.println();
        System.out.println("finished decrypting");
        System.out.println("RunTime: "+(System.nanoTime() - start_time)/1000000+"ms");
        //for(char[] b : values2)
        //System.out.print(new String(b));


    }

    private static char[] getKey(List<char[]> blocks,char[] IV,SpellChecker checker){
        Character[] keyBasic = new Character[]{'a','b','c','d','e','f','g','h'};
        Character[] keyPermutation = new Character[8];
        char[] key = new char[8];
        char[] master_key = new char[8];
        int maxWords = 0;
        Permutations iter = Permutations.create(keyBasic,keyPermutation);
        while(iter.next()){
            for(int i=0;i<key.length;i++)
                key[i] = keyPermutation[i].charValue();
            int end = blocks.size()>250 ? 250 : blocks.size();
            List<char []> values3 = OpMode.CBC_Decrypt("sub",blocks.subList(0,end),IV,key);
            int value = EnglishCheck(values3,checker);
            if(value>maxWords){
                System.out.println("changed max words to "+value);
                maxWords = value;
                for(int i=0;i<master_key.length;i++)
                    master_key[i] = keyPermutation[i].charValue();
            }
        }
        return master_key;
    }

    private static int EnglishCheck(List<char[]> blocks,SpellChecker checker) {
        String text = "";
        for(int i=0;i<100 && i<blocks.size();i++){
            text += new String(blocks.get(i));
        }

        List<String> words = TextOnlyAttack.breakToWords(text);
        Map<String, Long> collect =
                words.stream().collect(groupingBy(Function.identity(), counting()));
        LinkedHashMap<String, Long> countByWordSorted = TextOnlyAttack.sortCollect(collect);

        return TextOnlyAttack.checkWords(countByWordSorted,checker);
    }


    public static char[] readKey(String keyfile){
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
        return key;
    }

    public static char[] readIV(String IVfile){
        char[] IV = new char[10];
        try (BufferedReader br = new BufferedReader(new FileReader(IVfile))) {

            int i=0;

            int r;
            while ((r = br.read()) != -1) {
                IV[i] = (char) r;
                i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return IV;
    }

    public static List<char[]> readBlocks(String file){
        List<char[]> blocks = new ArrayList<>();

        File fileDir = new File(file);
        try (BufferedReader br = new BufferedReader( new InputStreamReader(
                new FileInputStream(fileDir), "UTF-8"))) {
            char[] block = new char[10];
            int r;
            while((r = br.read(block,0,10)) != -1){
                if (r < 10){
                    for(int i=r;i<10;i++){
                        block[i] = (char)0;
                    }
                }
                blocks.add(block);
                block = new char[10];
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return blocks;
    }

    public static void writeToFile(String dest,List<char []> contents){

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(dest))) {

            for(char[] content : contents)
                bw.write(new String(content));

            // no need to close it.
            //bw.close();

            System.out.println("Done writing");

        } catch (IOException e) {

            e.printStackTrace();

        }

    }


}
