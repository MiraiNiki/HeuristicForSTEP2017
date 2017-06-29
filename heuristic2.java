import java.io.*;
import java.util.*;

class heuristic2{

    static ArrayList<Integer> xPos = new ArrayList<Integer>();
    static ArrayList<Integer> yPos = new ArrayList<Integer>();
    static ArrayList<Integer> order = new ArrayList<Integer>();
    static ArrayList<Integer> bestOrder = new ArrayList<Integer>();
    static ArrayList<Integer> reverseOrder = new ArrayList<Integer>();
    static ArrayList<Integer> distance = new ArrayList<Integer>();
    static ArrayList<Integer> leftPoints = new ArrayList<Integer>();
    static int startPoint = 0;
    static int xMaxPoint = 0;
    static int yMaxPoint = 0;
    static double len = 0;
    static double bestLength = 0;

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
            for(int i = 0; i < bestOrder.size(); i++){
                pw.println(bestOrder.get(i));
            }
            pw.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    //xが小さいものをstartPointとした
    public static void getStartSmallPoint(){
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

    public static void getStartMidiamPoint(){
        for(int i = 0; i < xPos.size(); i++){
            int x = xPos.get(i);
            int startX = xPos.get(startPoint);
            int maxX = xPos.get(xMaxPoint);
            if(maxX/2 - startX > maxX/2 - x){
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

    public static void getStartRndPoint(int rnd){
        startPoint = rnd;
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
        order.clear();
        reverseOrder.clear();
        initLeftPoints();
        getDistance(startPoint);
        //testGetDistance();
        getFirstTwoPoints();
        while(leftPoints.size() != 0){
            int headPoint = order.get(order.size() - 1);
            int endPoint = reverseOrder.get(reverseOrder.size() - 1);
            //System.out.println("headpoint : " + headPoint + " ,endpoint : " + endPoint);
            getNextPoints(headPoint, endPoint);
        }
        for(int i = reverseOrder.size() - 1; i >= 0 ; i--){
            order.add(reverseOrder.get(i));
        }        

    }

    public static void testLeftPoints(){
        System.out.println("");
        for(int i = 0; i < leftPoints.size(); i++){
            //System.out.print("leftPoints :" + leftPoints.get(i));
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
        }
        //System.out.println("nextHead : " + nextHead + " ,nextEnd : " + nextEnd);
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
                cnt++;
                
            }else{
                reverseOrder.add(min);
                
                leftPoints.remove((Integer)min);
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
    }

    public static void getLength(){
        int end = order.size() - 1;
        double edx = xPos.get(order.get(end))-xPos.get(order.get(0));
        double edy = yPos.get(order.get(end))-yPos.get(order.get(0));
        len = Math.sqrt( edx*edx + edy*edy );
        for(int i = 1; i < order.size(); i++){
            int current = order.get(i-1);
            int next = order.get(i);
            double dx = xPos.get(current)-xPos.get(next);
            double dy = yPos.get(current)-yPos.get(next);
            len += Math.sqrt( dx*dx + dy*dy );
        }
    }

    public static void printLength(){
        System.out.println(len);
    }

    public static void copyOrder(){
        bestOrder.clear();
        for(int i = 0; i < order.size(); i++){
            bestOrder.add(order.get(i));
        }
    }




    public static void main(String args[]){
        if(args.length != 2){
            System.out.println("Usage: heuristic2 inputFileName outputFileName.");
            System.exit(1);
        }
        readCSVFile(args[0]);
        getXMaxPoint();
        getYMaxPoint();

        getStartSmallPoint();
        calcOrder();
        getLength();
        //printLength();

        bestLength = len;
        copyOrder();
        //testCalcOrder();
        getStartMidiamPoint();
        calcOrder();
        getLength();
        //printLength();

        if(bestLength > len){
            bestLength = len;
            copyOrder();
        }
        //testCalcOrder();

        long seed = 1L;
        Random r = new Random(seed);
        int cnt = 0;
        while(cnt <10){
            int rnd = r.nextInt(xPos.size());
            getStartRndPoint(rnd);
            calcOrder();
            getLength();
            //printLength();
            if(bestLength > len){
                bestLength = len;
                copyOrder();
            }
            cnt++;
        }

        //testCalcOrder();
        System.out.println("length: " + bestLength);
        writeCSVFile(args[1]);   

        
    }
}
