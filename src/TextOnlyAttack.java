import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by omerh on 4/16/2017.
 */
public class TextOnlyAttack {

    public static List<String> breakToWords(String text){
        List<String> words = new ArrayList<String>();
        //System.out.println("Text Before Split: "+text);
            text = text.replaceAll("[!?,._—]", " ");
            String[] splitted =  text.split("[\\W+]");
            for(String split : splitted) {
                words.add(split);
            }
        return words;
    }

    public static LinkedHashMap<String, Long> sortCollect(Map<String, Long> collect) {
        return collect.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (v1, v2) -> {
                            throw new IllegalStateException();
                        },
                        LinkedHashMap::new
                ));
    }

    public static int checkWords(LinkedHashMap<String, Long> words,SpellChecker checker,boolean print){
        Set<String> strings = words.keySet();
        Iterator<String> iter = strings.iterator();
        int i=0;
        int correct_words = 0;
        while(iter.hasNext()) {
            String str = iter.next();
            if(print)
                System.out.print(str);
            if (checker.isCorrect(str)) {
                correct_words++;
                if (print)
                    System.out.print("----TRUE");
            }
            if(print)
                System.out.println();
            //else
                //System.out.print(str+" , ");
            i++;
        }
        //System.out.println("Correct Words: "+correct_words);
        return correct_words;
    }
    public static SpellChecker makeChecker(){
        SpellDictionaryHashMap dictionaryHashMap = null;
        File dict = new File("dictionary/dictionary.txt");
        try {
            dictionaryHashMap = new SpellDictionaryHashMap(dict);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return new SpellChecker(dictionaryHashMap);
    }


    public static final List<String> Dictionary = new ArrayList<>();
    public static void initDictionary(){
        Dictionary.add("the");
        Dictionary.add("be");
        Dictionary.add("to");
        Dictionary.add("of");
        Dictionary.add("and");
        Dictionary.add("a");
        Dictionary.add("in");
        Dictionary.add("that");
        Dictionary.add("have");
        Dictionary.add("I");
        Dictionary.add("it");
        Dictionary.add("for");
        Dictionary.add("not");
        Dictionary.add("on");
        Dictionary.add("with");
        Dictionary.add("he");
        Dictionary.add("as");
        Dictionary.add("you");
        Dictionary.add("do");
        Dictionary.add("at");
        Dictionary.add("this");
        Dictionary.add("but");
        Dictionary.add("his");
        Dictionary.add("by");
        Dictionary.add("from");
        Dictionary.add("they");
        Dictionary.add("we");
        Dictionary.add("say");
        Dictionary.add("her");
        Dictionary.add("she");
        Dictionary.add("or");
        Dictionary.add("an");
        Dictionary.add("will");
        Dictionary.add("my");

    };


}
