import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class Idea_2 {

    public static ArrayList<ArrayList> All_cluster = new ArrayList<>();
    public static void main(String args[]) throws IOException, ClassNotFoundException{
        FileInputStream inputFile = new FileInputStream("/Users/sharad/IdeaProjects/Project_5_IR/src/file.ser");
        ObjectInputStream input = new ObjectInputStream(inputFile);

        double receive[][] = (double[][]) input.readObject();

        double newArray[][] = new double[receive.length][receive.length];
        for (int i =0;i<receive.length;i++){
            receive[i][i] = Double.NEGATIVE_INFINITY;
        }
//        System.out.println(Arrays.deepToString(receive));


        similarityScoreCheck(receive);

//        clusterLaunch(receive, newArray);
//        for(ArrayList check : All_cluster){
//            System.out.println(All_cluster.indexOf(check) + "Length" + check.size());
//        }
    }
    public static void similarityScoreCheck(double receive[][]){
        double maxValue = receive[0][0];
        int x_index=-1;
        int y_index=-1;
        for (int j = 0; j < receive.length; j++) {
            for (int i = 0; i < receive[j].length; i++) {
                if (receive[j][i] >= maxValue) {
                    maxValue = receive[j][i];

                    x_index = j;
                    y_index = i;

                }
            }

        }
//        cluster_info.add(x_index);
//        cluster_info.add(y_index);

        if (maxValue>0.4){
            int count = 0 ;
               for (ArrayList check : All_cluster) {

                   if (check.contains(x_index) || check.contains(y_index)) {
                       if (check.contains(x_index)) {
                           check.add(y_index);
                       }else {
                           check.add(x_index);
                       }

                       System.out.println("Document added to previous cluster:"+All_cluster.indexOf(check));
                       System.out.println("Documents in cluster:");
                       for (Object print : check) {
                           System.out.println(print);
                       }
                       count++;
                   }
               }
           if (count==0){
               ArrayList<Integer> cluster_info = new ArrayList<Integer>();
               cluster_info.add(x_index);
               cluster_info.add(y_index);
               All_cluster.add(cluster_info);
               System.out.println("Cluster is formed between Documents:");
               System.out.println("Cluster Number:"+All_cluster.indexOf(cluster_info));
               System.out.println(x_index);
               System.out.println(y_index);
           }


            clusterLaunch(receive, x_index,y_index);

        }else{
            System.out.println("Clustering Over");
        }
    }

    public static void clusterLaunch(double receive[][],int x_index, int y_index){


        double infinity = Double.NEGATIVE_INFINITY;

        for (int j = 0; j < receive.length ; j++) {

            receive[y_index][j] = ((receive[x_index][j]) + (receive[y_index][j])) / 2;
            receive[j][y_index] = receive[y_index][j];

        }

        for (int j =0; j< receive.length  ;j++){
            //Setting both row and column of index 2 as infinity
            receive[x_index][j] = infinity;              //x_index = 2
            receive[j][x_index] = infinity;
        }

//        System.out.println(Arrays.deepToString(receive));

        similarityScoreCheck(receive);
    }

}
