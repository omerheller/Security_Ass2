import java.util.LinkedList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * Created by omerh on 4/14/2017.
 */
public class Main {
    public static void main(String[] args){
        List<char[]> blocks = readBlocks("plainMsg_example.txt");
        char[] IV = readIV("IV_example.txt");

        char[] key = readKey("key_example.txt");
        List<char[]> values = OpMode.CBC_Encrypt("sub",blocks,IV,key);
        System.out.println("finished encrypting");
        writeToFile("cypher.txt",values);
        List<char []> values2 = OpMode.CBC_Decrypt("sub",values,IV,key);
        System.out.println("finished decrypting");
        //for(char[] b : values2)
        //System.out.print(new String(b));


    }

    public static char[] readKey(String keyfile){
        char[] key = new char[8];
        try (BufferedReader br = new BufferedReader(new FileReader(keyfile))) {

            String sCurrentLine;
            int i=0;

            while ((sCurrentLine = br.readLine()) != null) {
                key[i] = sCurrentLine.charAt(2);
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
        List<char[]> blocks = new LinkedList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
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
