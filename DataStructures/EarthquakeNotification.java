package DataStructures;

import java.util.Iterator;
import java.util.Scanner;
import java.io.*;

public class EarthquakeNotification { //I try to write efficient way our methods generally working with O(n) or O(1) time I never used nested for or while loops in my methods.

    static int index = 0; //for find the earthquake index.
    static int a = 0; //for how many magnitute is in order.
    static int earthCounter = 0; // for find my first earthquake is in which time(we will delete first earthquake if past 6 hours).

    static String watcherListFileName,earthquakeFileName;

    static boolean isAll;//we have --all option or not.

    static Scanner inputStreamWatcher = null, inputStreamEarthquake = null;

    //it can be adjust exact array size but we know that array size can be max 1000; therefore, I give 1000 value.
    static String[] timeEarthquake = new String[1000];//it will store time of earthquakes.
    static String[] earthquakePlace = new String[1000];//it will store place of earthquakes.
    //this is ArrayQueue because we just use one time so, I want to use data str but others string array because we could be use more than ones.
    static ArrayQueue<String> earthquakeCoordinates = new ArrayQueue<>();//it will store coordinates of earthquakes.

    static String[] watcherCoordinates = new String[1000];//it will store coordinates of watchers.
    static ArrayQueue<String> timeWatcher = new ArrayQueue<>();//it will store time of watchers.
    static ArrayQueue<String> operation = new ArrayQueue<>();//it will store operations.
    static ArrayQueue<String> watchers = new ArrayQueue<>();//it will store watchers name.

    static LinkedPositionalList<String> magnitudeNotOrderedList = new LinkedPositionalList<>();//it will store magnitudes.

    //they are our data str which we have to use.
    static DoublyLinkedList<String> watcherList = new DoublyLinkedList<>();
    static LinkedQueue<String> earthquakeQueue = new LinkedQueue<>();
    static LinkedPositionalList<String> magnitudeOrderedList = new LinkedPositionalList<>();

    public static void main(String[] args) {

        if (args[0].equals("--all")) {//check for our args have contain --all option
            isAll = true;
            watcherListFileName = args[1];
            earthquakeFileName = args[2];
        } else {
            isAll = false;
            watcherListFileName = args[0];
            earthquakeFileName = args[1];
        }
        try {
            inputStreamWatcher = new Scanner(new File(watcherListFileName));
            inputStreamEarthquake = new Scanner(new File(earthquakeFileName));
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            System.exit(0);
        }

        parser();//this method for take all information from file to our data str and arrays.
        inputStreamWatcher.close();
        inputStreamEarthquake.close();

        boolean flag = true; //check the our watcher time is come.
        String timer = "";
        for (int time = 0; time < 1000; time++) { //our simulation is starting.

            if (flag)
                timer = timeWatcher.dequeue();

            if (timer != null && timer.equals("" + time)) {//check the our watcher time and now time.

                String op = operation.dequeue(); //first we need to look operation.
                if (op.equals("add"))
                    addWatcher(); //this method for add watcher to watcher list.
                else if (op.equals("delete"))
                    deleteWatcher(); //this method for remove watcher and coordinates from watcher list.
                else
                    query(); //this method is find the biggest magnitute and print it.
                flag = true; //now we can look at other time of watcher.
            } else //our watcher time has not come.
                flag = false;

            if (timeEarthquake[index] != null && timeEarthquake[index].equals("" + time)) {//check the our earthquake time and now time.
                insertEarthquake(); //this method for insert earthquake which time has come.
                index++; //our earthquake was occur now I go to next index.
            }
            checkTime(time); //look at time and check is more than 6 hours is past or not.
        }
    }

    public static void parser() { //this method for take all information from file to our data str and arrays.

        int i = 0;
        while (inputStreamEarthquake.hasNext()) {
            String line = inputStreamEarthquake.next();

            if (line.equals("<time>"))
                timeEarthquake[i] = inputStreamEarthquake.next();
            else if (line.equals("<place>")) {
                inputStreamEarthquake.useDelimiter("<");//because of file format I changed the delimiter <...> place <...> now it takes after ">" to "<" it means just take place.
                earthquakePlace[i] = inputStreamEarthquake.next();
                inputStreamEarthquake.useDelimiter("\\p{javaWhitespace}+");//again I change back to white space.
            } else if (line.equals("<coordinates>")) {
                inputStreamEarthquake.useDelimiter("<");
                String cor =inputStreamEarthquake.next();
                earthquakeQueue.enqueue(timeEarthquake[i] + "," + cor +"," + earthquakePlace[i]);
                earthquakeCoordinates.enqueue(cor);
                i++;
                inputStreamEarthquake.useDelimiter("\\p{javaWhitespace}+");
            } else if (line.equals("<magnitude>"))
                magnitudeNotOrderedList.addLast(inputStreamEarthquake.next());
        }
        i = 0;
        while (inputStreamWatcher.hasNext()) {
            String line = inputStreamWatcher.next();
            timeWatcher.enqueue(line);
            line = inputStreamWatcher.next();

            if (line.contains("query"))
                operation.enqueue(line);
            else {
                operation.enqueue(line);
                if (!line.equals("delete")) {
                    line = inputStreamWatcher.next();
                    watcherCoordinates[i] = (line + ", " + inputStreamWatcher.next());
                    line = inputStreamWatcher.next();
                    watchers.enqueue(line);
                    i++;
                } else {
                    line = inputStreamWatcher.next();
                    watchers.enqueue(line);
                }
            }
        }
    }

    public static void addWatcher() { //this method for add watcher to watcher list.
        String name = watchers.dequeue();
        watcherList.addLast(name);
        System.out.println(name + " is added to the watcher-list" + "\n");
    }

    public static void deleteWatcher() { //this method for remove watcher and coordinates from watcher list.
        String name = watchers.dequeue();
        Iterator<String> a = watcherList.iterator();
        for (int i = 0; i < watcherList.size(); i++) {
            if (a.next().equals(name)) {
                watcherList.remove(i);
                watcherCoordinates[i] = null;
            }
        }

        for (int i = 0; i < watcherCoordinates.length - 2; i++) { //this is for remove coordinates of watcher from our array.
            if (watcherCoordinates[i] == null && watcherCoordinates[i + 1] != null) {
                watcherCoordinates[i] = watcherCoordinates[i + 1];
                watcherCoordinates[i + 1] = null;
            }
        }

        System.out.println(name + " is removed from the watcher-list" + "\n");
    }

    public static void query() { //this method is find the biggest magnitute and print it.
        int count = 0;

        String magnitute = magnitudeNotOrderedList.first().getElement();
        Position<String> first = magnitudeNotOrderedList.first();

        if (magnitudeOrderedList.first() == null)
            System.out.println("No record on list" + "\n");
        else {
            for (int j = 0; j < magnitudeNotOrderedList.size(); j++) {//this loop for find the index of which earthquake have max magnitute.

                if (magnitute.equals(magnitudeOrderedList.first().getElement()))
                    count = j;

                first = magnitudeNotOrderedList.after(first);
                if (first == null)
                    break;
                magnitute = first.getElement();
            }
            System.out.println("Largest earthquake in the past 6 hours:");
            System.out.println("Magnitude " + magnitudeOrderedList.first().getElement() + " at" + earthquakePlace[count] + "\n");
        }
    }

    public static void insertEarthquake() { //this method for insert earthquake which time has come.

        Position<String> magnitudeIndex = magnitudeNotOrderedList.first();

        double magnitude;
        if (index != 0)         //this is for find the our index of magnitude of earthquake if index = 0 it means this is first earthquake of our simulation
            for (int i = 0; i < index; i++)     //so, index of magnitude = first index of magnitudeNotOrderedList.
                magnitudeIndex = magnitudeNotOrderedList.after(magnitudeIndex);

        magnitude = Double.parseDouble(magnitudeIndex.getElement());

        double formula = 2 * Math.pow(magnitude, 3); //our distance formula.

        String[] cordinateEar = earthquakeCoordinates.dequeue().split(",");

        double xEar = Double.parseDouble(cordinateEar[0]);//store x value.
        double yEar = Double.parseDouble(cordinateEar[1]);//store y value.

        int size = watcherList.size();
        Iterator<String> cursor = watcherList.iterator();//we need to travel our watcher list for find all members who close to earthquake.

        if (isAll)
            System.out.println("Earthquake" + earthquakePlace[index] + "is inserted into the magnitude-ordered-list");
        for (int i = 0; i < size; i++) {
            cordinateEar = watcherCoordinates[i].split(",");
            double xWatcher = Double.parseDouble(cordinateEar[0]);//store x value.
            double yWatcher = Double.parseDouble(cordinateEar[1]);//store y value.

            //diferance between 2 points formula is sqrt((X2-X1)^2 + (Y2 - Y1)^2).
            if (Math.sqrt(Math.pow(xWatcher - xEar, 2) + Math.pow(yWatcher - yEar, 2)) < formula)
                System.out.println("Earthquake" + earthquakePlace[index] + "is close to " + cursor.next());
            else
                cursor.next();
        }
        System.out.println();

        sort();//we add new earthquake therefore we need to check the magnitudeOrderedList.
    }

    public static void sort() {//sort the our new earthquake for magnitudeOrderedList.
        Position<String> cursor = magnitudeNotOrderedList.first();//we need to look into magnitudeNotOrderedList because we stored in the list before.
        for (int i = 0; i < a; i++)
            cursor = magnitudeNotOrderedList.after(cursor);
        Position<String> cursor2 = magnitudeOrderedList.first();
        boolean flag = true;// this is check the if it is smallest element of list.

        if (!magnitudeOrderedList.isEmpty()) {
            for (int i = 0; i < magnitudeOrderedList.size(); i++) {
                if (Double.parseDouble(cursor.getElement()) < Double.parseDouble(cursor2.getElement()))//it means our magnitude is smaller than ordered list.
                    cursor2 = magnitudeOrderedList.after(cursor2);
                else {
                    magnitudeOrderedList.addBefore(cursor2, cursor.getElement());//we find the our magnitude index added the order.
                    flag = false;
                    break;
                }
            }
            if (flag)//this is smallest elemnt it means this is last element of list.
                magnitudeOrderedList.addLast(cursor.getElement());
        } else//our list is empty; therefore, I can add first index.
            magnitudeOrderedList.addFirst(cursor.getElement());
        a++;
    }

    public static void checkTime(int time) { //this method for time control if past more than 6 hours it will delete from lists.

        String earthTime = timeEarthquake[earthCounter];//it looks our earthquake time.
        if (earthTime != null && time - Integer.parseInt(earthTime) > 5) { //I wrote 5 in my code because time come from the our occurrence loop
            earthquakeQueue.dequeue();         // and it not increase if loop not finish but our time pass so our real time actually time + 1.

            Position<String> magnituteIndex = magnitudeNotOrderedList.first();

            if (earthCounter != 0)
                for (int i = 0; i < earthCounter; i++)
                    magnituteIndex = magnitudeNotOrderedList.after(magnituteIndex);
            earthCounter++;//removed the earthquake therefore we do not need to look the index I can increase the index.
            //we gave to the position of index and removed it.
            magnitudeOrderedList.remove(magnitudeOrderedList.get(magnituteIndex.getElement()));
        }
    }
}
