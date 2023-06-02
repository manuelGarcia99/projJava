import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Weekday implements Serializable {

    private int id; // 1 = Monday, 5 = Friday



    public ConcurrentHashMap<Integer, Boolean> morning;
    public ConcurrentHashMap<Integer, Boolean> evening;
    public ConcurrentHashMap<Integer, Lesson> hmMorningLessons;
    public ConcurrentHashMap<Integer, Lesson> hmEveningLessons;

    ArrayList<Lesson> lessonList;

    public int getId() {
        return id;
    }

    Weekday(int id) {
        morning = new ConcurrentHashMap<Integer, Boolean>(4);
        morning.put(8, false);
        morning.put(9, false);
        morning.put(10, false);
        morning.put(11, false);


        evening = new ConcurrentHashMap<Integer, Boolean>(5);
        evening.put(14, false);
        evening.put(15, false);
        evening.put(16, false);
        evening.put(17, false);
        evening.put(18, false);

        hmMorningLessons = new ConcurrentHashMap<>(4);
        hmEveningLessons = new ConcurrentHashMap<>(5);

        lessonList = new ArrayList<Lesson>();


        this.id = id;
    }

    public static int scheduleLessonPrompt(){
        int answer =-1;
        do {
            System.out.println("0- Cancel");
            System.out.println("1- Morning");
            System.out.println("2- Evening");
            answer = Ler.umInt();
        } while(answer!=0 && answer!=1 && answer!=2);

        return answer;
    }
    public void scheduleLesson(Lesson c) {
        int answer = scheduleLessonPrompt();//remaster askhourevening
        if(answer!=0){
            if (answer == 1) {
                if (morning.containsValue(false)) {
                    int hour ;
                    do {
                        hour = askHourMorning();
                        if (hour>=8 && hour<=11) {
                            hmMorningLessons.put(hour, c);
                            morning.put(hour, true);

                        }
                    } while (hour != 0);
                } else {
                    System.out.println("The morning shedule is all full");
                }

            } else if (answer == 2) {
                if (evening.containsValue(false)) {
                    int hour;
                    do {
                        hour = askHourEvening();
                        if (hour >=14 && hour <= 18) {
                            hmEveningLessons.put(hour, c);
                            evening.put(hour, true);
                        }
                    } while (hour != 0);
                } else {
                    System.out.println("The evening shedule is all full");
                }
            } else {
                System.out.println("Invalid Input");
                scheduleLesson(c);
            }
        }

    }

    public int askHourMorning() {
        System.out.println("Let me know the time of the lesson :"
        + "\n0- Cancel"
                + "\n8- 8am " + (morning.get(8) ? "Occupied" : "Free") // if the lesson is occupied(true), it returns "Occupied", else "Free"
                + "\n9- 9am " + (morning.get(9) ? "Occupied" : "Free")
                + "\n10- 10am " + (morning.get(10) ? "Occupied" : "Free")
                + "\n11- 11am " + (morning.get(11) ? "Occupied" : "Free"));
        int hour = Ler.umInt();
        boolean isInMorning = (hour >= 8 && hour <= 11);
        if (isInMorning && !morning.get(hour)) {
            return hour;
        } else if (isInMorning && morning.get(hour)) {
            System.out.println("There is already a lesson going on, in this hour");
            return 2;
        } else {
            System.out.println("Please choose an hour in the morning or leave by choosing 0");
            if(hour == 0) {
                return 0;
            } else {
                return 2;//make a do...while?
            }
        }
    }

    public int askHourEvening() {
        System.out.println("Let me know the time of the lesson :"
        + "\n0- Cancel "
                + "\n14- 2pm " + (evening.get(14) ? "Occupied" : "Free") // if the lesson is occupied(true), it returns "Occupied", else "Free"
                + "\n15- 3pm " + (evening.get(15) ? "Occupied" : "Free")
                + "\n16- 4pm " + (evening.get(16) ? "Occupied" : "Free")
                + "\n17- 5pm " + (evening.get(17) ? "Occupied" : "Free")
                + "\n18- 6pm " + (evening.get(18) ? "Occupied" : "Free"));
        int hour = Ler.umInt();
        boolean isInEvening = (hour >= 14 && hour <= 18);
        if (isInEvening && !evening.get(hour)) {
            return hour;
        } else if (isInEvening && evening.get(hour)) {
            System.out.println("There is already a lesson going on, in this hour");
            return 2;
        } else {
            System.out.println("Please choose an hour in the evening or leave by choosing 0");
            if(hour == 0) {
                return 0;
            } else {
                return 2;//make a do while?;
            }
        }
    }

    public String toString(){
        return "" + hmMorningLessons + hmEveningLessons;
    }
}

/*
 * startMorning = 8
 * endMorning = 12
 * startEvening = 14
 * endEvening = 19
 */
