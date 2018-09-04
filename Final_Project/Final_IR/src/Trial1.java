import Project4_IR.Read_linebyline;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Trial1 {
    public static void main(String args[]) throws IOException {
        File main_file = new File("/Users/sharad/IdeaProjects/Assignment4_IR/src/files");
        File files[] = main_file.listFiles();
        ArrayList<File> files_arraylist = new ArrayList<File>();
        for (File f : files) {
            files_arraylist.add(f);
        }
        Collections.sort(files_arraylist);
        Trial1 tokenizer_object = new Trial1();
        tokenizer_object.tokenize(files_arraylist);
        Read_linebyline check = new Read_linebyline();

    }

    public void tokenize(ArrayList<File> files) throws IOException {
        HashMap<String, ArrayList<ArrayList<Integer>>> inverted_index = new HashMap<String, ArrayList<ArrayList<Integer>>>();
        int fileno_count = 1;
        for (File check_file : files) {
               //Count maintained for file number, that is when in this file each token assigned to file number for this token
            Document doc = Jsoup.parse(check_file, "UTF-8", "");
            String obtain_fullstring = doc.text();
            String fullstring_downcase = obtain_fullstring.toLowerCase();
            String tokens_file[] = fullstring_downcase.split(" ");
            Trial1 matcher_object = new Trial1();
            for (int i = 0; i < tokens_file.length; i++) {
                String returned_elements = matcher_object.matcher(tokens_file[i]);
                int count = 1; //Count maintaining for tf of token  ////////wait on this count operation   //Given 1 already as this token will be present in the file where is coming from
                ArrayList<Integer> vertical_list = new ArrayList<Integer>();
                vertical_list.add(fileno_count);
                vertical_list.add(count);   /////need to be updated if a token comes which is same
                if (inverted_index.containsKey(returned_elements)) {

                    ArrayList<ArrayList<Integer>> obtain_Horizontal_list = inverted_index.get(returned_elements);
                    ArrayList<Integer> obtain_vertical_list = obtain_Horizontal_list.get(obtain_Horizontal_list.size() - 1);
                    int file_count_check = obtain_vertical_list.get(0);
                    if (file_count_check == fileno_count) {
                        ////If the count on the vertical list is same as that of file no  which is running presently then update count of that file
                        int tf = obtain_vertical_list.get(1);
                        obtain_vertical_list.set(1, tf + 1);
                    } else {
                        //if count of the vertical list is not same as that of the file no that means it is from a new file therefore make add new vertical list
                        obtain_Horizontal_list.add(vertical_list);
                    }
                } else {   /////If the token is not present in the inverted index then add the token with the arraylist<arraylist<integer =(1)fileno,(2)count>>
                    ArrayList<ArrayList<Integer>> posting_list = new ArrayList<ArrayList<Integer>>();    //posting list is same horizontal list
                    posting_list.add(vertical_list);
                    inverted_index.put(returned_elements, posting_list);
                }
            }
            fileno_count++;
        }
        writeOperation(inverted_index);
        //////Stopwords Removal and Then File Write to check the posting list
        //////Query search with development of tf-idf
    }

    public String matcher(String each_element) {
        //////Check this matcher and improve it more
        Pattern unicodeOutliers = Pattern.compile("[^\\x41-\\x5A\\x30-\\x39\\xC0-\\xFF]",
                Pattern.UNICODE_CASE | Pattern.CANON_EQ
                        | Pattern.CASE_INSENSITIVE);
        String s1;
        if (each_element.length() > 1) {
            s1 = each_element.substring(0, each_element.length());
        } else {
            s1 = each_element;
        }
        Matcher match = unicodeOutliers.matcher(s1);
        Matcher match_main = unicodeOutliers.matcher(each_element);
        int count = 0;
        while (match.find()) {
            count++;
        }
        //System.out.println(count);
        if (count != 0) {
            //System.out.println("Count!=0"+each_element);
            return each_element;
        } else {
            each_element = match_main.replaceAll(" ");
            // System.out.println("Count=0"+each_element);
            return each_element;

        }
    }

    public void writeOperation(HashMap<String,ArrayList<ArrayList<Integer>>> write_invertedindex){
        Map<String,ArrayList> map_invertedindex = new TreeMap<>(write_invertedindex);
        Iterator iterator_invertedindex = map_invertedindex.entrySet().iterator();
        try{    ///////////********** GIVES ONE WRONG VALUE AFTER Descriptions: WHICH CREATES PROBLEM SO MANUALLY REMOVE IT
            FileWriter fileWriter = new FileWriter("/Users/sharad/IdeaProjects/Assignment4_IR/src/inverted_index.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            while(iterator_invertedindex.hasNext()){
                Map.Entry check = (Map.Entry) iterator_invertedindex.next();
                bufferedWriter.write(check.getKey().toString());
                bufferedWriter.write("\t");
                bufferedWriter.write(check.getValue().toString());
                bufferedWriter.newLine();

            }
            bufferedWriter.close();
            fileWriter.close();


        }catch (IOException e){}
    }
}


