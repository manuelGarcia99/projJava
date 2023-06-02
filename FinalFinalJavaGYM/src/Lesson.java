import java.io.Serializable;
import java.util.*;

public class Lesson implements Serializable {
    private int id;
    private String name;
    private String teacher;
    private int teacherId;
    // private ArrayList<Integer> horas;
    ArrayList<User> usersInLesson;
    private int capacity;
    private int participants;
    private static int last = 0;


    public static void setLast(int last) {
        Lesson.last = last;
    }

    public static int getLast() {
        return last;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public Lesson(String name, String teacher, int capacity, int teacherId) {
        this.name = name;
        this.capacity = capacity;
        last++;
        id = last;
        this.teacher = teacher;
        this.teacherId= teacherId;
        usersInLesson = new ArrayList<>(capacity);
    }

    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }
    
    public String getTeacher() {
    	return teacher;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getParticipants() {
        return participants;
    }



    public void setName(String name) {
        this.name = name;
    }
    
    public void setTeacher(String teacher) {
    	this.teacher = teacher;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }

    public ArrayList<User> getUsersInLesson() {
        return usersInLesson;
    }

    public String toString() {
        return "\nLesson Name = "  + name + "\ncapacity = " + capacity + "\nparticipants = " + participants + "\nID = " + id + "\n" ;
    }

    public void addParticipant(User user) {
        if(getParticipants()<getCapacity() && !(usersInLesson.contains(user))) {
            participants++;
            usersInLesson.add(user);

        } else {
            System.out.println("The lesson is full");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return capacity == lesson.capacity && participants == lesson.participants && name.equals(lesson.name) && teacher.equals(lesson.teacher);
    }
}
