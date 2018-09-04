package Project4_IR;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Read_linebyline {

    public static void main(String args[]) throws IOException {
        String check = "identity theft";
        String check_lowercase = check.toLowerCase();
        String[] checks = check_lowercase.split(" ");
        File f = new File(Read_linebyline.class.getResource("/Project4_IR/inverted_index.txt").getFile());


        int count = 0;
        String line_first_word;
        int total_files = 503;
        ArrayList<HashMap> main_list = new ArrayList<HashMap>();
        for (String each_element : checks) {
            String line;
            FileReader fr = new FileReader(f);
            BufferedReader bf = new BufferedReader(fr);
//            System.out.println(each_element);
            HashMap<Double, Double> hash_fileno_tf = new HashMap<Double, Double>();
            while ((line = bf.readLine()) != null) {
                count++;
                String tokens[] = line.split("\t");
                String token = tokens[0];
                String posting_unedited = line.substring(token.length());
                String posting = posting_unedited.replaceAll("[,\\[\\]<-]", "");
                String[] posting_elements = posting.split(" ");
                double[] num = new double[posting_elements.length];
                if (each_element.equals(line.split("\t")[0])) {
//                    System.out.println("Found in the file");
//                    System.out.println(token);
//                    System.out.println(posting_unedited);
//                    System.out.println(posting);
                    for (int i = 0; i < posting_elements.length; i++) {
                        String gg = posting_elements[i].trim();
                        try {
                            num[i] = Integer.parseInt(gg);
                        } catch (NumberFormatException e) {
//                            System.out.println(gg);
                        }

//                        System.out.println(num[i]);
                    }
                    if (num.length == 2) {
                        if (hash_fileno_tf.containsKey(num[0])) {
                            double count_tf = hash_fileno_tf.get(num[0]);
                            hash_fileno_tf.put(num[0], count_tf + num[1]);
                        } else {
                            hash_fileno_tf.put(num[0], num[1]);
                        }
                    } else {
//                        System.out.println(num.length);
                        for (int j = 0; j < num.length; j += 2) {
                            if (hash_fileno_tf.containsKey(num[j])) {
                                double count_tf = hash_fileno_tf.get(num[j]);
                                hash_fileno_tf.put(num[j], count_tf + num[j + 1]);
//                        System.out.println("dasd");
                            } else {
                                try {
                                    hash_fileno_tf.put(num[j], num[j + 1]);
                                } catch (ArrayIndexOutOfBoundsException e) {
//                                System.out.println("check"+num[j]);
                                }
                            }
                        }
                    }

                }
            }
            main_list.add(hash_fileno_tf);
            if (hash_fileno_tf.size() == 0) {
                System.out.println("Token Not found in Inverted Index");
                return;
            }
        }
//        System.out.println(count);
//        System.out.println("\n");
        for (int i = 0; i < checks.length; i++) {
            HashMap hashes = main_list.get(i);
            int idf_value = hashes.size();
            double idfscore = Math.log10(total_files / idf_value);
            System.out.println(checks[i]);
            Iterator iterator_invertedindex = hashes.entrySet().iterator();

            while (iterator_invertedindex.hasNext()) {
                Map.Entry check1 = (Map.Entry) iterator_invertedindex.next();
//                System.out.println("Key:" + check1.getKey().toString() + "Value:" + check1.getValue().toString());   ///Inverted index to hashmap
                Double key_fileno = (Double) check1.getKey();
                double value_tf_unedited = (double) check1.getValue();
                double value_tf = 1 + Math.log10(value_tf_unedited);
                double value_tf_idf = idfscore * value_tf;
                hashes.put(key_fileno, value_tf_idf);    /////Updated value with score

//                System.out.println(check1.getKey().toString() + ".HTML" + "\t" + "Value:" + check1.getValue().toString());
            }

//            System.out.println("\n");
        }
        if (main_list.size()==1){
            Map<Double, Double> sortedmap = sortByValues(main_list.get(0));
            Iterator iterator_sortedmap = sortedmap.entrySet().iterator();
            while (iterator_sortedmap.hasNext()){
                Map.Entry final_check = (Map.Entry) iterator_sortedmap.next();
                System.out.println(final_check.getKey().toString()+".HTML"+"\t"+"Weight:"+final_check.getValue().toString());
            }
        }
        HashMap<Double,Double> retrieval_docs = new HashMap<Double,Double>();
        if (main_list.size() > 1) {

            HashMap hashes_0 = main_list.get(0);
            HashMap hashes_1 = main_list.get(1);
            Iterator iterator_hashes_samekeys = hashes_0.entrySet().iterator();
            while (iterator_hashes_samekeys.hasNext()) {
                Map.Entry comparing = (Map.Entry) iterator_hashes_samekeys.next();
                if(hashes_1.containsKey(comparing.getKey())){
//                    System.out.println("Yes"+comparing.getKey().toString());
                    double final_score = (double)comparing.getValue() + (double)hashes_1.get(comparing.getKey());
//                    System.out.println("Final_Score:"+final_score);
                    retrieval_docs.put((double)comparing.getKey(),final_score);
                }else {
//                    System.out.println("No");
                }
            }

        }
        if (retrieval_docs.size()==0){
            System.out.println("No common documents between query tokens");
        }
        Map<Double, Double> sortedmap = sortByValues(retrieval_docs);


//        System.out.println("\n");
//        System.out.println("\n");
        Iterator iterator_sortedmap = sortedmap.entrySet().iterator();
        while (iterator_sortedmap.hasNext()){
            Map.Entry final_check = (Map.Entry) iterator_sortedmap.next();
            System.out.println(final_check.getKey().toString()+".HTML"+"\t"+"Weight:"+final_check.getValue().toString());
        }
    }
    public static HashMap sortByValues(HashMap map) {
        List list = new LinkedList(map.entrySet());
        // Defined Custom Comparator here
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o2)).getValue())
                        .compareTo(((Map.Entry) (o1)).getValue());
            }
        });

        // Here I am copying the sorted list in HashMap
        // using LinkedHashMap to preserve the insertion order
        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }
}
//    String check1 = "aabbcd";
////    String check2 = "aabbcc";
////    int num = check1.compareTo(check2);
////      System.out.println(num);