import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Weekend implements Serializable {//fazer esta classe 9am->2pm

    private int id;

    ConcurrentHashMap<Integer, Boolean> timeTable;
    ConcurrentHashMap<Integer, Lesson> hmLessons;
    ArrayList<Lesson> lessonList;

    public Weekend(int id){


        timeTable = new ConcurrentHashMap<Integer, Boolean>(5);
        timeTable.put(9, false);
        timeTable.put(10, false);
        timeTable.put(11, false);
        timeTable.put(12, false);
        timeTable.put(13, false);


        hmLessons = new ConcurrentHashMap<>(5);

        lessonList = new ArrayList<Lesson>();


        this.id = id;
    }

    public void scheduleLesson(Lesson c) {
        if (timeTable.containsValue(false)) {
            int hour;
            do {
                hour = askTimeTable();
                if (hour >=9 && hour<=13) {
                    hmLessons.put(hour, c);
                    timeTable.put(hour, true);
                }
            } while (hour != 0);
        } else {
            System.out.println("The timeTable schedule is all full");
        }
    }

    public int askTimeTable() {
        System.out.println("Let me know the time of the lesson :"
                + "\n0- Cancel"
                + "\n9- 9am " + (timeTable.get(9) ? "Occupied" : "Free") // if the lesson is occupied(true), it returns "Occupied", else "Free"
                + "\n10- 10am " + (timeTable.get(10) ? "Occupied" : "Free")
                + "\n11- 11am " + (timeTable.get(11) ? "Occupied" : "Free")
                + "\n12- 0am " + (timeTable.get(12) ? "Occupied" : "Free")
                + "\n13- 1pm " + (timeTable.get(13) ? "Occupied" : "Free"));
        int hour = Ler.umInt();
        boolean fitsInTimeTable = (hour >= 9 && hour <= 13);
        if (fitsInTimeTable && !timeTable.get(hour)) {
            return hour;
        } else if (fitsInTimeTable && timeTable.get(hour)) {
            System.out.println("There is already a lesson going on, in this hour");
            return 2;
        } else {
            System.out.println("Please choose an hour in the timeTable or leave by choosing 0 ");
            if(hour == 0) {
                return 0;
            } else {
                return 2;
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString(){
        return "" + hmLessons;
    }

}
