import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Teacher extends User implements Serializable {
    private static String so = System.getProperty("user.name");

   // list of lessons that the teacher teaches

    @Override
    public String toString() {
        return "\n\nName: " + getName() + "\nID: " + getID();
    }

    public Teacher(String name, String password) {//Ask leader if it wouldn't be better to put a string in Schedule lessons
        super(name, password);
         }
    public static void insert(ArrayList<User> users, ArrayList<Teacher> teachers) {
        System.out.println("What is your name? ");
        String name;
        do {
            name = Ler.umaString();
        } while(name.equals(""));
        String password= Sign_In.insertPassword();
        Teacher l = new Teacher(name, password);
        teachers.add(l);
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("C:\\Users\\"+so+"\\Desktop\\Teacher.dat"));
            os.writeInt(Teacher.getLast());
            os.writeObject(teachers);
            os.flush();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
        users.add(l);
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("C:\\Users\\"+ so +"\\Desktop\\User.dat"));
            os.writeInt(User.getLast());
            os.writeObject(users);
            os.flush();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Your ID is: " + (User.getLast()) + ". Please remember this ID.");
    }

    public void createLessons(ArrayList<Lesson> list, String name, int id) {
        System.out.println("Insert the name of the lesson you want to create: ");
        String lessonName = Ler.umaString();
        int capacity ;
        do{
            System.out.println("What is the maximum number of students for this lesson: ");
            capacity = Ler.umInt();
            if(capacity<=0 || capacity >=25){
                System.out.println("That number is inadmissible");
            }
        } while(capacity<=0 || capacity>=25 );
        Lesson c = new Lesson(lessonName, name, capacity, id);// teacher must be given
        list.add(c);
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("C:\\Users\\"+so+"\\Desktop\\Lesson.dat"));
            os.writeInt(Lesson.getLast());
            os.writeObject(list);
            os.flush();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public static int scheduleLessonPrompt(){
        int weekdayChoosen =-1;
        do{
            System.out.println("When do you want to schedule the lesson? ");
            System.out.println("0- Cancel");
            System.out.println("1- Monday");
            System.out.println("2- Tuesday");
            System.out.println("3- Wednesday");
            System.out.println("4- Thursday");
            System.out.println("5- Friday");
            System.out.println("6- Saturday");
            System.out.println("7- Sunday");
            weekdayChoosen = Ler.umInt();
            if(weekdayChoosen<0 || weekdayChoosen>7)
                System.out.println("Invalid input");
        } while((weekdayChoosen<1 || weekdayChoosen>7) && weekdayChoosen!=0);
        return weekdayChoosen;
    }

    public static void askSheduleLesson( ArrayList<Lesson> lessonsList , int teacherId) {
        int weekdayChoosen= scheduleLessonPrompt();
        if(weekdayChoosen!=0) {
            ConcurrentHashMap<Integer,Weekday> idsToWeekday;
            idsToWeekday= new ConcurrentHashMap<>(5);
            try {
                ObjectInputStream isWeekday = new ObjectInputStream(new FileInputStream(
                        "C:\\Users\\"+so+"\\Desktop\\Weekday.dat"));
                idsToWeekday = (ConcurrentHashMap<Integer, Weekday>) isWeekday.readObject();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
            ConcurrentHashMap<Integer,Weekend> idsToWeekend;
            idsToWeekend= new ConcurrentHashMap<>(2);
            try {
                ObjectInputStream isWeekend = new ObjectInputStream(new FileInputStream(
                        "C:\\Users\\"+so+"\\Desktop\\Weekend.dat"));
                idsToWeekend = (ConcurrentHashMap<Integer, Weekend>) isWeekend.readObject();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
            if(idsToWeekday.get(weekdayChoosen) == null && weekdayChoosen >=1 && weekdayChoosen <=5) {
                Weekday newDay = new Weekday(weekdayChoosen);
                idsToWeekday.put(weekdayChoosen, newDay);
                boolean found = true;
                int position = -1, choice;
                do {
                    System.out.println("0- Cancel");
                    System.out.println("Choose a lesson by ID :");
                    for (Lesson t : lessonsList) {
                        if(t.getTeacherId() == teacherId)
                            System.out.println("\nID : " + t.getId() + "\nName: " + t.getName() + "\nCapacity: " + t.getCapacity() +"\nParticipants: " +t.getParticipants()+"\n.............");
                    }
                    choice = Ler.umInt();
                    for (int i = 0; i < lessonsList.size(); i++) {
                        if (choice == lessonsList.get(i).getId()) {
                            position = i;
                            found = false;
                        }
                    }
                } while (found && choice != 0);
                if (position != -1) {
                    newDay.scheduleLesson(lessonsList.get(position));//make a reader for lessons
                    idsToWeekday.put(weekdayChoosen, newDay);
                }
                try {
                    ObjectOutputStream osSchedule = new ObjectOutputStream(new FileOutputStream("C:\\Users\\"+so+"\\Desktop\\Weekday.dat"));
                    osSchedule.writeObject(idsToWeekday);
                    osSchedule.flush();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
            else if(weekdayChoosen == 6 || weekdayChoosen ==7 && idsToWeekend== null){
                    //Ask leader what about weekend schedule
                    Weekend newDay = new Weekend(weekdayChoosen);
                    idsToWeekend.put(weekdayChoosen, newDay);
                    boolean found = true;
                    int position = -1;
                    do {

                        System.out.println("Choose a lesson by ID :");
                        for (Lesson t : lessonsList) {
                            if(t.getTeacherId() == teacherId)
                                System.out.println("ID : " + t.getId() + "; Name: " + t.getName() + "; Capacity:" + t.getCapacity());
                        }
                        int choice = Ler.umInt();
                        for (int i = 0; i < lessonsList.size(); i++) {
                            if (choice == lessonsList.get(i).getId()) {
                                position = i;
                                found = false;
                            }
                        }

                    } while (found);
                    newDay.scheduleLesson(lessonsList.get(position));//make a reader for lesson
                    idsToWeekend.put(weekdayChoosen,newDay);
                    try {
                        ObjectOutputStream osSchedule = new ObjectOutputStream(new FileOutputStream("C:\\Users\\"+so+"\\Desktop\\Weekend.dat"));
                        osSchedule.writeObject(idsToWeekend);
                        osSchedule.flush();
                    } catch(IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            else if(idsToWeekday.get(weekdayChoosen) != null && weekdayChoosen >=1 && weekdayChoosen <=5) {
                boolean found = true;
                int position = -1;
                do {
                    System.out.println("Choose a lesson by ID :");
                    for (Lesson t : lessonsList) {
                        if(t.getTeacherId() == teacherId)
                            System.out.println("ID : " + t.getId() + "; Name: " + t.getName() + "; Capacity:" + t.getCapacity());
                    }
                    int choice = Ler.umInt();
                    for (int i = 0; i < lessonsList.size(); i++) {
                        if (choice == lessonsList.get(i).getId()) {
                            position = i;
                            found = false;
                        }
                    }
                } while (found);
                idsToWeekday.get(weekdayChoosen).scheduleLesson(lessonsList.get(position));//make a reader for lessons

                try {
                    ObjectOutputStream osSchedule = new ObjectOutputStream(new FileOutputStream("C:\\Users\\"+so+"\\Desktop\\Weekday.dat"));
                    osSchedule.writeObject(idsToWeekday);
                    osSchedule.flush();
                } catch(IOException e) {
                    System.out.println(e.getMessage());
                }
            } else if(idsToWeekend.get(weekdayChoosen) != null && weekdayChoosen >=6 && weekdayChoosen <=7){
                boolean found = true;
                int position = -1;
                do {
                    System.out.println("Choose a lesson by ID :");
                    for (Lesson t : lessonsList) {
                        if(t.getTeacherId() == teacherId)
                            System.out.println("ID : " + t.getId() + "; Name: " + t.getName() + "; Capacity:" + t.getCapacity());
                    }
                    int choice = Ler.umInt();
                    for (int i = 0; i < lessonsList.size(); i++) {
                        if (choice == lessonsList.get(i).getId()) {
                            position = i;
                            found = false;
                        }
                    }
                } while (found);
                idsToWeekend.get(weekdayChoosen).scheduleLesson(lessonsList.get(position));//make a reader for lessons

                try {
                    ObjectOutputStream osSchedule = new ObjectOutputStream(new FileOutputStream("C:\\Users\\"+so+"\\Desktop\\Weekend.dat"));
                    osSchedule.writeObject(idsToWeekend);
                    osSchedule.flush();
                } catch(IOException e) {
                    System.out.println(e.getMessage());
                }
            }

        }
    }

    public int getID(){
        return super.getId();
    }

    public static void enterIDT(ArrayList<Teacher> teachers, ArrayList<Lesson> lessons) {
        int answer = enterIDPrompt();
        if (answer != 0) {
                login(teachers, answer, lessons);
            }

    }
    public static int enterIDPrompt() {
        // nobody has ID = 0, that's why it's reserved for "Cancel"
        System.out.println("\n\n0- Cancel");
        System.out.println("Enter ID: ");
        int answer = Ler.umInt();
        return answer;
    }


    public static void login(ArrayList<Teacher> teachers, int id, ArrayList<Lesson> lessons) {
        boolean found = false;
        // guess from 0 to 9999 (guess has to be String, so 64 doesn't work, and you have
        // to type 0064 instead)
        for (int i = 0; i < teachers.size(); i++) {
            if (teachers.get(i).getId() == id) {
                found = true;
                String password = teachers.get(i).getPassword();
                //after the Teacher misses the guess three times, it comes back
                guessT(3, password, id, teachers, lessons);
            }
        }
        if (!found) {
            System.out.println("There is no teacher with that ID");
        }
    }

    public static void guessT(int maxGuesses, String password, int id, ArrayList<Teacher> teachers, ArrayList<Lesson> lessons) {
        String guess;
        int guessCount = 0;
        boolean outOfGuesses = false;
        /*
         * "guess == password" doesn't work because it only works for numbers.
         * For Strings, you must use "guess.equals(password)"
         */
        while (!outOfGuesses && guessCount < maxGuesses) {
            System.out.print("Enter password: ");
            guess = Ler.umaString();
            guessCount++;
            if (guess.equals(password))
                outOfGuesses = true;
        }
        if (outOfGuesses == true) {
            // o guess corresponde a password
            menu(teachers, id, lessons);
        } else {
            System.out.println("Try again");
        }
    }

    public static String integerToDay(int i){
        switch(i){
            case 1: return "Monday";
            case 2: return "Tuesday";
            case 3: return "Wednesday";
            case 4: return "Thursday";
            case 5: return "Friday";
            case 6: return "Saturday";
            case 7: return "Sunday";
            default: return "";
        }
    }

    public static void menu(ArrayList<Teacher> teachers, int id, ArrayList<Lesson> lessons) {//**//
        int answer = 0;
        do {
            System.out.println("\n\nYou are on the Teacher's menu");
            System.out.println("0- Logout");
            System.out.println("1- Check information about myself");
            System.out.println("2- Check my lessons");
            System.out.println("3- Check my students");
            System.out.println("4- Create lesson");
            System.out.println("5- Choose schedule for a lesson");
            answer = Ler.umInt();
            switch(answer) {
                case 1:
                    for (int i = 0; i < teachers.size(); i++) {
                        if (teachers.get(i).getId() == id) {
                            System.out.println(teachers.get(i));
                            break;
                        }
                    }
                    break;
                case 2:
                    //
                    ConcurrentHashMap<Integer,Weekday> idsToWeekday;
                    idsToWeekday= new ConcurrentHashMap<>(5);
                    try {
                        ObjectInputStream isWeekday = new ObjectInputStream(new FileInputStream(
                                "C:\\Users\\"+so+"\\Desktop\\Weekday.dat"));
                        idsToWeekday = (ConcurrentHashMap<Integer, Weekday>) isWeekday.readObject();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    } catch (ClassNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    ConcurrentHashMap<Integer,Weekend> idsToWeekend;
                    idsToWeekend= new ConcurrentHashMap<>(2);
                    try {
                        ObjectInputStream isWeekend = new ObjectInputStream(new FileInputStream(
                                "C:\\Users\\"+so+"\\Desktop\\Weekend.dat"));
                        idsToWeekend = (ConcurrentHashMap<Integer, Weekend>) isWeekend.readObject();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    } catch (ClassNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    for (int i = 0; i< teachers.size(); i++)
                    {
                        for(int j=0;j<lessons.size(); j++){
                            if (teachers.get(i).getID() == id && lessons.get(j).getTeacher().equals(teachers.get(i).getName())) {
                                System.out.println(lessons.get(j));
                                System.out.println("..........");
                                for(int h=1; h<8; h++) {
                                    if(h>=1 && h<=5) {
                                        if(idsToWeekday.get(h) != null){
                                           for(Integer key: idsToWeekday.get(h).hmMorningLessons.keySet()){
                                               if(idsToWeekday.get(h).hmMorningLessons.get(key).equals(lessons.get(j))){
                                                   System.out.println(integerToDay(h)+ ": " + key + "h");
                                               }
                                           }
                                            for(Integer key: idsToWeekday.get(h).hmEveningLessons.keySet()){
                                                if(idsToWeekday.get(h).hmEveningLessons.get(key).equals(lessons.get(j))){
                                                    System.out.println(integerToDay(h)+ ": " + key + "h");
                                                }
                                            }
                                        }
                                    } else if(h==6 || h==7) {
                                        if(idsToWeekend.get(h) != null){
                                            for(Integer key: idsToWeekend.get(h).hmLessons.keySet()){
                                                if(idsToWeekend.get(h).hmLessons.get(key).equals(lessons.get(j))){
                                                    System.out.println(integerToDay(h)+ ": " + key + "h");
                                                }
                                            }

                                        }
                                    }
                                }
                                System.out.println("--------------------------");
                            }
                        }
                    }

                    break;
                case 3:
                    //fazer
                    for(int i=0; i<teachers.size(); i++) {
                        for(int j = 0 ; j< lessons.size(); j++) {
                            if ((teachers.get(i).getID() == id) && (lessons.get(j).getTeacherId() == (teachers.get(i).getID()))) {
                                System.out.println("\nLesson's name : " + lessons.get(j).getName()+ "\n...........");
                                System.out.println("Students: ");
                                System.out.println(lessons.get(j).getUsersInLesson());
                                System.out.println("------------------------------------");
                            }
                        }
                    }
                    break;
                case 4:
                    for(int i = 0; i< teachers.size(); i++){
                        if(teachers.get(i).getID() == id){
                            teachers.get(i).createLessons(lessons, teachers.get(i).getName(), teachers.get(i).getID());
                            break;
                        }
                    }
                    break;
                case 5:
                    boolean found= false;
                    for (int i = 0; i< teachers.size(); i++)
                    {
                        if(teachers.get(i).getID() == id){
                            for(int j=0;j<lessons.size();j++){
                                if(lessons.get(j).getTeacherId() == id){
                                    found = true;
                                    askSheduleLesson(lessons,id);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    if(!found){
                        System.out.println("You didn't create any lesson yet.\nCreate a lesson first.");
                    }
                    break;
                case 0: break;
                default: System.out.println("Insert a valid number");
                    continue;
            }
        } while(answer != 0);
    }
}
