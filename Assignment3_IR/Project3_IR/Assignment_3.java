import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Assignment3 {

    public static void main(String args[]) throws IOException {

        ArrayList<HashMap<String,Double>> each_filemap = new ArrayList<HashMap<String, Double>>();
        //List of Hashmap for each designated file
        Assignment3 reference = new Assignment3();  //Instance of class created to call methods
        //File read main input from console
        File main_file = new File(args[0]);
        File files[] = main_file.listFiles();
        int total_file_length = files.length;
        ArrayList<File> files_arraylist = new ArrayList<File>();
        for(File check_file : files){
            files_arraylist.add(check_file);
        }
        Collections.sort(files_arraylist);

        //Static path given (Change later) and path given of project2_IR
       /* FileReader fileReader = new FileReader(main_file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        StringBuilder out1 = new StringBuilder();
        while((line = bufferedReader.readLine())!=null){
            out1.append(" ");
            out1.append(line);
        }
        //System.out.println(out1);
        String out1_string = out1.toString();
        String out1_string_split[]= out1_string.split(" ");
        // System.out.println("check"+out1_string_split[0]);    0th element problem that is the indexing starts from 1 elements
        // System.out.println(out1_string_split.length);

        */
        String obtained_elements = null;
        for (File check_file: files_arraylist) {
            //Each map is assigned to a file and tokens saved in each hashmap
            HashMap<String, Double> hmap = new HashMap<String, Double>();
            File file = new File(check_file.toString());
            //HTML parser used for getting the text
            Document doc = Jsoup.parse(file, "UTF-8", "");
            //System.out.println(doc);
            obtained_elements = doc.text();

            //Lower-case to the whole string
            String obtained_elements_lowercase = obtained_elements.toLowerCase();
            //System.out.println(obtained_elements_lowercase);
            String each_element[] = obtained_elements_lowercase.split(" ");
            Assignment3 func = new Assignment3();
            //Each Hashmap is stored with the key and token count
            for (int i = 0; i < each_element.length; i++) {
                //System.out.println(each_element[i]);
                String returned_elements = func.matching(each_element[i]);
                // System.out.println(returned_elements);
                Double value = 0.0;
                if (returned_elements.length() > 1) {   //To remove words tokens with length 1.
                    if (hmap.containsKey(returned_elements)) {
                        value = hmap.get(returned_elements) + 1;
                        hmap.put(returned_elements, value);
                    } else {
                        hmap.put(returned_elements, 1.0);
                    }
                }
            }
            each_filemap.add(hmap);  //Hashmap is added to the arraylist
        }
        reference.stopwords_remove(each_filemap,total_file_length); //Function to remove stopwords

        reference.tfcalculate(each_filemap,total_file_length); //Function to calculate Term frequency
        reference.idfcalculate(each_filemap,total_file_length); //Function to calculate inverse document frequency
    }
    public void stopwords_remove(ArrayList<HashMap<String,Double>> obtained_list_removestopwords, int totalfiles_removestopwords)throws IOException{
        //Removing stopwords from each Hashmap
        for (int i=0;i<totalfiles_removestopwords-1;i++){
            HashMap<String,Double> eachfile_map_removestopwords =obtained_list_removestopwords.get(i);

            File file_stopwords = new File("/Users/sharad/IdeaProjects/Project_3_IR/src/stoplist.txt");
            FileReader fr = new FileReader(file_stopwords);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                eachfile_map_removestopwords.remove(line);
            }
        }
    }

    public void tfcalculate(ArrayList<HashMap<String,Double>> obtained_list_tf,int totalfiles_tf){
        //To calculate term frequency which is the count of term in document.
        for (int i=0;i<totalfiles_tf-1;i++){
            HashMap<String,Double> eachfile_map_tf = obtained_list_tf.get(i);
            Iterator map_iterate = eachfile_map_tf.entrySet().iterator();
            while (map_iterate.hasNext()){
                Map.Entry check = (Map.Entry)map_iterate.next();
                String key = check.getKey().toString();
                Double value = (Double) check.getValue();
                double new_value = 1+Math.log10(value);
                eachfile_map_tf.put(key,new_value);
            }
        }

    }
    public void idfcalculate(ArrayList<HashMap<String,Double>> obtained_list_idf,int totalfiles_idf){
        //To calculate inverse document frequency which is how many documents a term occurs.

        HashMap<String,ArrayList> final_postinglist = new HashMap<String,ArrayList>();
        for (int i =0; i<totalfiles_idf-1; i++){
            // System.out.println("Total_Files" + totalfiles_idf);
            HashMap<String, Double> eachfile_map_idf = obtained_list_idf.get(i);
            //Now set up iterator for each hash so as to compare the values.
            Iterator map_iterate = eachfile_map_idf.entrySet().iterator();
            Iterator map_iterate_final = eachfile_map_idf.entrySet().iterator();
            double weight_normalize_denominator=0;
            double final_weight_normalize_denominator_sqrt=0;
            double tf_idf=0;
            while (map_iterate.hasNext()) {

                Map.Entry check = (Map.Entry)map_iterate.next();
                double value_idf=0;
                for (int j = 0; j < totalfiles_idf-1; j++) {
                    HashMap<String,Double> check_eachfilemap = obtained_list_idf.get(j);
                    if(check_eachfilemap.containsKey(check.getKey())){
                        value_idf++;
                        //  System.out.println("yes");
                    }else{
                        //  System.out.println("No");
                    }
                }
                //NOTE: Since each document is compared first with its own document, therefore min(df) is always equal to one.
                //value_idf needed

                value_idf = Math.log10(totalfiles_idf-1/value_idf);
                //System.out.println(check.getKey());
                //System.out.println(value_idf);
                String key_idf = check.getKey().toString();
                double value_tf= (double) check.getValue();
                tf_idf = value_idf*value_tf;
                //NEW CHANGES IN THE CODE
                ArrayList<Double> inside_arraylist = new ArrayList<Double>();
                double i_double = (double) i;
                inside_arraylist.add(i_double);
                inside_arraylist.add(tf_idf);
                if (final_postinglist.containsKey(key_idf)){

                    final_postinglist.get(key_idf).add(inside_arraylist);

                }else{

                   ArrayList<ArrayList<Double>> postinglist_arraylist = new ArrayList<ArrayList<Double>>();
                    postinglist_arraylist.add(inside_arraylist);
                    final_postinglist.put(key_idf,postinglist_arraylist);

                }
                //CHANGES DONE IN THE CODE TILL HERE
                weight_normalize_denominator = weight_normalize_denominator + Math.pow(tf_idf,2);

                eachfile_map_idf.put(key_idf,tf_idf);
            }
            final_weight_normalize_denominator_sqrt=Math.sqrt(weight_normalize_denominator);
            //System.out.println(final_weight_normalize_denominator_sqrt);
            while (map_iterate_final.hasNext()) {

                Map.Entry check = (Map.Entry) map_iterate_final.next();
                String key_idf = check.getKey().toString();
                double value_tf= (double) check.getValue();
                // System.out.println("check"+ ":"+value_tf);
                double weight_normalize = value_tf/final_weight_normalize_denominator_sqrt;
                eachfile_map_idf.put(key_idf,weight_normalize);
            }
        }
        finalposting_operation(final_postinglist);
        //weight_filewrite(obtained_list_idf,totalfiles_idf);  //Function invoked to calculate normalized weights

    }
    public void finalposting_operation(HashMap<String,ArrayList> finalposting_received){
        //Alphabatically arranging key values
        Map<String,ArrayList> map_finalposting = new TreeMap<>(finalposting_received);
        Iterator iterator_finalpostinglist = map_finalposting.entrySet().iterator();
        int count =0;
        int flag=0;
        try {
            FileWriter file_write = new FileWriter("/Users/sharad/IdeaProjects/Project_3_IR/src/Posting_file.txt");
            BufferedWriter object_writes = new BufferedWriter(file_write);
            FileWriter write_dictionary = new FileWriter("/Users/sharad/IdeaProjects/Project_3_IR/src/dictionary_file.txt");
            BufferedWriter object_write_dictionary = new BufferedWriter(write_dictionary);
            while (iterator_finalpostinglist.hasNext()) {
                Map.Entry check1 = (Map.Entry) iterator_finalpostinglist.next();

              //Posting File
               // object_writes.write(check1.getKey().toString());
              //object_writes.write(":");
              //object_writes.write("\t");
               ArrayList<ArrayList<Double>> obtain_outsidelist = finalposting_received.get(check1.getKey());
                for(int i=0 ; i< obtain_outsidelist.size();i++){
                    object_writes.write(obtain_outsidelist.get(i).toString());
                    object_writes.newLine();
                }

              //object_writes.write(check1.getValue().toString());
               // object_writes.newLine();

              //Dictionary File
                object_write_dictionary.write(check1.getKey().toString());
                object_write_dictionary.newLine();

                int length_write_countarraylist = finalposting_received.get(check1.getKey()).size();
                String write_countarraylist = Integer.toString(length_write_countarraylist);
                object_write_dictionary.write(write_countarraylist);
                object_write_dictionary.newLine();

                if(flag==0){
                    object_write_dictionary.write("1");
                    count = count + length_write_countarraylist;
                }else{
                    count = count + length_write_countarraylist;
                    String count_String = Integer.toString(count);
                    object_write_dictionary.write(count_String);
                }
                flag++;
                object_write_dictionary.newLine();
            }
            object_writes.close();
            object_write_dictionary.close();
        }catch(IOException e){}

    }

    /*public void weight_filewrite(ArrayList<HashMap<String,Double>> list_writeup, int totalfiles_writeup){
        //Function invoked to calculate normalized weights where W(i)=sqrt(W1square+W2square+....)
        for (int i=0;i<totalfiles_writeup-1;i++){

            HashMap eachfile_map_writeup = list_writeup.get(i);
            Iterator iterate_writeup = eachfile_map_writeup.entrySet().iterator();
            try {
                FileWriter file_write = new FileWriter("/Users/sharad/IdeaProjects/Project_3_IR/src/"+i+".txt");
                BufferedWriter object_write = new BufferedWriter(file_write);
                while (iterate_writeup.hasNext()) {
                    Map.Entry check = (Map.Entry) iterate_writeup.next();

                    object_write.write(check.getKey().toString());
                    object_write.write(":");
                    object_write.write("\t");
                    object_write.write(check.getValue().toString());

                    object_write.newLine();
                }
                object_write.close();   //A resource must be closed when it is no longer needed
                //The documentation for BufferedWriter.close() notes that it "Closes the stream, flushing it first"
                //output file does not include all the text you wrote to your BufferedWriter because it stored some of that text in a buffer.
                // The BufferedWriter never emptied that buffer, passing it through to the file, because you never told it to do so.
                //Therefore BuffereWriter is needed to be closed.
            }catch (IOException e){}
            long End_time = System.nanoTime();
           // System.out.println("File Written Successfully");

        }
        System.out.println("All files Written");
    }*/


    public String matching(String each_element){
        // Matching function called in order to remove unwanted tokens and extract correct tokens from the stream of each file
        // System.out.println(each_element);
        Pattern unicodeOutliers = Pattern.compile("[^\\x41-\\x5A\\x30-\\x39\\xC0-\\xFF]",
                Pattern.UNICODE_CASE | Pattern.CANON_EQ
                        | Pattern.CASE_INSENSITIVE);
        String s1;
        if (each_element.length()>1) {
            s1 = each_element.substring(0, each_element.length());
        }else {
            s1= each_element;
        }
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


