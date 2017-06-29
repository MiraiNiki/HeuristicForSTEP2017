import java.io.*;
import java.util.*;

class heuristic2{

    static ArrayList<Integer> xPos = new ArrayList<Integer>();
    static ArrayList<Integer> yPos = new ArrayList<Integer>();
    static ArrayList<Integer> order = new ArrayList<Integer>();
    static ArrayList<Integer> reverseOrder = new ArrayList<Integer>();
    static ArrayList<Integer> distance = new ArrayList<Integer>();
    static ArrayList<Integer> leftPoints = new ArrayList<Integer>();
    static int startPoint = 0;
    static int xMaxPoint = 0;
    static int yMaxPoint = 0;

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
            for(int i = reverseOrder.size() - 1; i >= 0; i--){
                pw.println(reverseOrder.get(i));
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

    public static void getXMaxPoint(){
        for(int i = 0; i < xPos.size(); i++){
            int x = xPos.get(i);
            int xMax = xPos.get(xMaxPoint);
            if(xMax < x){
                xMaxPoint = i;
            }
        }
    }

    public static void getYMaxPoint(){
        for(int i = 0; i < yPos.size(); i++){
            int y = yPos.get(i);
            int yMax = yPos.get(yMaxPoint);
            if(yMax < y){
                yMaxPoint = i;
            }
        }
    }  

    public static void testGetXMaxPoint(){
        System.out.println("xMax: = " + xMaxPoint);
        System.out.print("X: " + xPos.get(xMaxPoint));
        System.out.println(", Y: " + yPos.get(xMaxPoint));
    }

    public static void testGetYMaxPoint(){
        System.out.println("yMax: = " + yMaxPoint);
        System.out.print("X: " + xPos.get(yMaxPoint));
        System.out.println(", Y: " + yPos.get(yMaxPoint));
    }
    
    public static void testGetStartPoint(){
        System.out.println(startPoint);
        System.out.print("X: " + xPos.get(startPoint));
        System.out.println(", Y: " + yPos.get(startPoint));
    }
    //最初に入れる
    public static void initLeftPoints(){
        for(int i = 0; i < xPos.size(); i++){
            leftPoints.add(i);
        }
        
    }

    public static void calcOrder(){
        initLeftPoints();
        getDistance(startPoint);
        //testGetDistance();
        getFirstTwoPoints();
        while(leftPoints.size() != 0){
            int headPoint = order.get(order.size() - 1);
            int endPoint = reverseOrder.get(reverseOrder.size() - 1);
            System.out.println("headpoint : " + headPoint + " ,endpoint : " + endPoint);
            getNextPoints(headPoint, endPoint);
        }
    }

    public static void testLeftPoints(){
        System.out.println("");
        for(int i = 0; i < leftPoints.size(); i++){
            System.out.print("leftPoints :" + leftPoints.get(i));
        }
    }

    //終了条件として、head == endを使いたい
    public static void getNextPoints(int head, int end){
        int nextHead = searchPoint(head);
        leftPoints.remove((Integer)nextHead);
        int nextEnd = 0;
        if(leftPoints.size() != 0){
            nextEnd = searchPoint(end);
            leftPoints.remove((Integer)nextEnd);
            System.out.println("in");
            
        }
        System.out.println("nextHead : " + nextHead + " ,nextEnd : " + nextEnd);
        if(leftPoints.size() == 0){
            order.add(nextHead);
        }else if(ifCross(head, nextHead, end, nextEnd)){
            order.add(nextEnd);
            reverseOrder.add(nextHead);
        }else{
            order.add(nextHead);
            reverseOrder.add(nextEnd);
        }
    } 

    public static int searchPoint(int point){
        int nextPoint = 0;
        getDistance(point);
        nextPoint = leftPoints.get(distance.indexOf(Collections.min(distance)));
        return nextPoint;
    }


    public static boolean ifCross(int a, int b, int c, int d){
        int ta = (xPos.get(c) - xPos.get(d)) * (yPos.get(a) - yPos.get(c)) + (yPos.get(c) - yPos.get(d)) * (xPos.get(c) - xPos.get(a));
        int tb = (xPos.get(c) - xPos.get(d)) * (yPos.get(b) - yPos.get(c)) + (yPos.get(c) - yPos.get(d)) * (xPos.get(c) - xPos.get(b));
        int tc = (xPos.get(a) - xPos.get(b)) * (yPos.get(c) - yPos.get(a)) + (yPos.get(a) - yPos.get(b)) * (xPos.get(a) - xPos.get(c));
        int td = (xPos.get(a) - xPos.get(b)) * (yPos.get(d) - yPos.get(a)) + (yPos.get(a) - yPos.get(b)) * (xPos.get(a) - xPos.get(d));
        return tc * td < 0 && ta * tb < 0;
    }

    public static void getFirstTwoPoints(){
        int cnt = 0;
        for(int i = 0; i < distance.size() && cnt < 3; i++){
            int min = distance.indexOf(Collections.min(distance));
            if(cnt % 2 == 0){
                order.add(min);
                
                leftPoints.remove((Integer)min);
                if(cnt == 2)
                    System.out.println("order[1] = " + min);
                cnt++;
                
            }else{
                reverseOrder.add(min);
                
                leftPoints.remove((Integer)min);
                if(cnt == 1)
                    System.out.println("reverseOrder[0] = " + min);
                cnt++;
                
            }   
            distance.set(min, Integer.MAX_VALUE);
        }
    }

    //最適化をかけるべきかも
    public static void getDistance(int point){
        distance.clear();
        for(int i = 0; i < leftPoints.size(); i++){
            double dx = xPos.get(leftPoints.get(i))-xPos.get(point);
            double dy = yPos.get(leftPoints.get(i))-yPos.get(point);
            distance.add( (int)(Math.sqrt( dx*dx + dy*dy )) );
        }
    }

    public static void testCalcOrder(){
        for(int i = 0; i < order.size(); i++){
            System.out.println(order.get(i));
        }
        for(int i = reverseOrder.size() - 1; i >= 0 ; i--){
            System.out.println(reverseOrder.get(i));
        }
    }

    public static void main(String args[]){
        if(args.length != 2){
            System.out.println("Usage: heuristic2 inputFileName outputFileName.");
            System.exit(1);
        }
        readCSVFile(args[0]);
        getStartPoint();
        getXMaxPoint();
        getYMaxPoint();

        calcOrder();
        testCalcOrder();
        writeCSVFile(args[1]);   

        
    }
}
