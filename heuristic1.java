import java.io.*;
import java.util.*;

class heuristic1{

    static ArrayList<Integer> xPos = new ArrayList<Integer>();
    static ArrayList<Integer> yPos = new ArrayList<Integer>();
    static ArrayList<Integer> order = new ArrayList<Integer>();
    static int startPoint = 0;
    static ArrayList<Integer> distance = new ArrayList<Integer>();

    public static void readCSVFile(String fileName){
        try{
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);

            String line = br.readLine(); //最初の1行は要らない
            StringTokenizer token;
            while((line = br.readLine()) != null){
                token = new StringTokenizer(line,",");
                float x = Float.parseFloat(token.nextToken());
                float y = Float.parseFloat(token.nextToken());
                xPos.add((int)x);
                yPos.add((int)y);
            }
            br.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public static void testReadCSVFile(){
        for(int i = 0; i < xPos.size(); i++){
            System.out.print("X: " + xPos.get(i));
            System.out.println(", Y: " + yPos.get(i));
        }
    }

    public static void writeCSVFile(String fileName){
        try{
            FileWriter fw = new FileWriter(fileName, false);
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            pw.println("index");
            for(int i = 0; i < order.size(); i++){
                pw.println(order.get(i));
            }
            pw.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    //xが小さいものをstartPointとした
    public static void getStartPoint(){
        for(int i = 0; i < xPos.size(); i++){
            int x = xPos.get(i);
            int startX = xPos.get(startPoint);
            if(startX > x){
                startPoint = i;
            }else if(startX == x){
                int y = yPos.get(i);
                int startY = yPos.get(startPoint);
                if(startY > y){
                    startPoint = i;
                }
            }
        }
    }
    
    public static void testGetStartPoint(){
        System.out.println(startPoint);
        System.out.print("X: " + xPos.get(startPoint));
        System.out.println(", Y: " + yPos.get(startPoint));
    }

    public static void calcOrder(){
        getDistance();
        testGetDistance();
        for(int i = 0; i < distance.size(); i++){
            int min = distance.indexOf(Collections.min(distance));
            order.add(min);
            distance.set(min, Integer.MAX_VALUE);
        }
    }

    public static void getDistance(){
        for(int i = 0; i < xPos.size(); i++){
            if(i == startPoint){
                distance.add(0);
            }else{
                double dx = xPos.get(i)-xPos.get(startPoint);
                double dy = yPos.get(i)-yPos.get(startPoint);
                distance.add( (int)(Math.sqrt( dx*dx + dy*dy )) );
            }
        }
    }

    public static void testGetDistance(){
        for(int i = 0; i < distance.size(); i++){
            System.out.println(distance.get(i));
        }
    }

    public static void testCalcOrder(){
        for(int i = 0; i < order.size(); i++){
            System.out.println(order.get(i));
        }
    }

    public static void main(String args[]){
        if(args.length != 2){
            System.out.println("Usage: heuristic1 inputFileName outputFileName.");
            System.exit(1);
        }
        readCSVFile(args[0]);
        testReadCSVFile();
        getStartPoint();
        testGetStartPoint();
        calcOrder();
        testCalcOrder();
        writeCSVFile(args[1]);
    }
}
