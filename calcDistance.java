import java.io.*;
import java.util.*;

class calcDistance{

    static ArrayList<Double> xPos = new ArrayList<Double>();
    static ArrayList<Double> yPos = new ArrayList<Double>();
    static ArrayList<Integer> order = new ArrayList<Integer>();
    static double distance = 0;

    public static void readPostionFile(String fileName){
        try{
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine(); //最初の1行は要らない
            StringTokenizer token;
            while((line = br.readLine()) != null){
                token = new StringTokenizer(line,",");
                double x = Double.parseDouble(token.nextToken());
                double y = Double.parseDouble(token.nextToken());
                xPos.add(x);
                yPos.add(y);
            }
            br.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public static void readOrderFile(String fileName){
        try{
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);

            String line = br.readLine(); //最初の1行は要らない
            while((line = br.readLine()) != null){
                order.add(Integer.parseInt(line));
            }
            br.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public static void getDistance(){
        int end = order.size() - 1;
        double edx = xPos.get(order.get(end))-xPos.get(order.get(0));
        double edy = yPos.get(order.get(end))-yPos.get(order.get(0));
        distance = Math.sqrt( edx*edx + edy*edy );
        for(int i = 1; i < order.size(); i++){
            int current = order.get(i-1);
            int next = order.get(i);
            double dx = xPos.get(current)-xPos.get(next);
            double dy = yPos.get(current)-yPos.get(next);
            distance += Math.sqrt( dx*dx + dy*dy );
        }
    }

    public static void printDistance(){
        System.out.println(distance);
    }

    public static void main(String args[]){
        if(args.length != 2){
            System.out.println("Usage: calcDistance positionFile orderFile.");
            System.exit(1);
        }
        readPostionFile(args[0]);
        readOrderFile(args[1]);
        getDistance();
        printDistance();
    }
}
