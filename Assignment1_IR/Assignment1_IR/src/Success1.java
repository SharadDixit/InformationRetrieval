import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Success1 {
    public static void main(String args[]) throws IOException {

            File main_file = new File("/Users/sharad/IdeaProjects/Assignment1_IR/src/input.txt");
            FileReader fileReader = new FileReader(main_file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            StringBuilder out1 = new StringBuilder();
            while((line = bufferedReader.readLine())!=null){
                out1.append(" ");
                out1.append(line);
            }
            System.out.println(out1);
            String out1_string = out1.toString();
            String out1_string_split[]= out1_string.split(" ");
           // System.out.println("check"+out1_string_split[0]);    0th element problem that is the indexing starts from 1 elements


           // System.out.println("Check");
        ArrayList<HashMap<String,Integer>> each_filemap = new ArrayList<HashMap<String, Integer>>();


                String obtained_elements = null;
                    for (int x=1;x<out1_string_split.length;x++) {
                        HashMap<String, Integer> hmap = new HashMap<String, Integer>();
                        File file = new File(out1_string_split[x]);
                        Document doc = Jsoup.parse(file, "UTF-8", "");
                        //System.out.println(doc);
                        obtained_elements = doc.text();

                        //Lower-case to the whole string
                        String obtained_elements_lowercase = obtained_elements.toLowerCase();
                        System.out.println(obtained_elements_lowercase);
                        String each_element[] = obtained_elements_lowercase.split(" ");


                        Success1 func = new Success1();
                        for (int i = 0; i < each_element.length; i++) {
                            //System.out.println(each_element[i]);
                            String returned_elements = func.matching(each_element[i]);
                            // System.out.println(returned_elements);
                            int value = 0;
                            if (hmap.containsKey(returned_elements)) {
                                value = hmap.get(returned_elements) + 1;
                                hmap.put(returned_elements, value);
                            } else {
                                hmap.put(returned_elements, 1);
                            }
                        }
                        each_filemap.add(hmap);
                    }
                    HashMap<String,Integer> l = new HashMap<String, Integer>();
                 l = each_filemap.get(0);
                 HashMap<String,Integer> y = new HashMap<String, Integer>();
                    y= each_filemap.get(1);

                for (Map.Entry<String, Integer> entry : l.entrySet()) {
                    System.out.println(entry.getKey() + " : " + entry.getValue());
                }
                System.out.println("hello\n");
        System.out.println("hello\n");
        System.out.println("hello\n");
        System.out.println("hello\n");
                for (Map.Entry<String, Integer> entry : y.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
                }



    }

    public String matching(String each_element){
       // System.out.println(each_element);
        Pattern unicodeOutliers = Pattern.compile("[^\\x41-\\x5A\\x30-\\x39\\xC0-\\xFF]",
                Pattern.UNICODE_CASE | Pattern.CANON_EQ
                        | Pattern.CASE_INSENSITIVE);

        String s1=each_element.substring(0,each_element.length()-1);
        Matcher match = unicodeOutliers.matcher(s1);
        Matcher match_main =unicodeOutliers.matcher(each_element);
        int count=0;
        while(match.find())
        {
            count++;
        }
        //System.out.println(count);
        if (count!=0){
             //System.out.println("Count!=0"+each_element);
            return each_element;
        }else{
            each_element = match_main.replaceAll(" ");
           // System.out.println("Count=0"+each_element);
            return each_element;

        }
        //System.out.println(each_element);
        //return each_element;
        //System.out.println(each_element[i]);


    }

}


