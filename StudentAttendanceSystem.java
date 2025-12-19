import java.util.ArrayList;
import java.util.List;
// Component
abstract class AttendanceComponent {
    protected String name;
    public AttendanceComponent(String name) {
        this.name = name;
    }
    public abstract void markPresent();
    public abstract void markAbsent();
    public abstract double getAttendanceRate();  // percentage
    public void add(AttendanceComponent component) {
        throw new UnsupportedOperationException();
    }
    public void remove(AttendanceComponent component) {
        throw new UnsupportedOperationException();
    }
    public AttendanceComponent getChild(int index) {
        throw new UnsupportedOperationException();
    }
    public String getName() {
        return name;
    }
}
class Student extends AttendanceComponent {
    private int totalSessions = 0;
    private int presentCount = 0;
    public Student(String name) {
        super(name);
    }
    @Override
    public void markPresent() {
        totalSessions++;
        presentCount++;
    }
    @Override
    public void markAbsent() {
        totalSessions++;
    }
    @Override
    public double getAttendanceRate() {
        if (totalSessions == 0) return 0.0;
        return (presentCount * 100.0) / totalSessions;
    }
    @Override
    public String toString() {
        return "Student: " + name + " - Attendance: " +
               String.format("%.2f%%", getAttendanceRate());
    }
}
// Composite
class AttendanceGroup extends AttendanceComponent {
    private List<AttendanceComponent> children = new ArrayList<>();
    public AttendanceGroup(String name) {
        super(name);
    }
    @Override
    public void add(AttendanceComponent component) {
        children.add(component);
    }
    @Override
    public void remove(AttendanceComponent component) {
        children.remove(component);
    }
    @Override
    public AttendanceComponent getChild(int index) {
        return children.get(index);
    }
    @Override
    public void markPresent() {
        for (AttendanceComponent child : children) {
            child.markPresent();
        }
    }
    @Override
    public void markAbsent() {
        for (AttendanceComponent child : children) {
            child.markAbsent();
        }
    }
    @Override
    public double getAttendanceRate() {
        if (children.isEmpty()) return 0.0;
        double totalRate = 0;
        for (AttendanceComponent child : children) {
            totalRate += child.getAttendanceRate();
        }
        return totalRate / children.size();
    }
    public void printStructure() {
        System.out.println("Group: " + name + " - Avg Attendance: " +
                           String.format("%.2f%%", getAttendanceRate()));
        for (AttendanceComponent child : children) {
            if (child instanceof AttendanceGroup) {
                ((AttendanceGroup) child).printStructure();
            } else {
                System.out.println("  " + child);
            }
        }
    }
}
public class StudentAttendanceSystem {
    public static void main(String[] args) {
        Student s1 = new Student("Adham");
        Student s2 = new Student("Eslam");
        Student s3 = new Student("Hagar");
        Student s4 = new Student("Pawlo");
        AttendanceGroup sectionA = new AttendanceGroup("Section A");
        sectionA.add(s1);
        sectionA.add(s2);
        AttendanceGroup sectionB = new AttendanceGroup("Section B");
        sectionB.add(s3);
        sectionB.add(s4);
        AttendanceGroup course = new AttendanceGroup("SET412 Course");
        course.add(sectionA);
        course.add(sectionB);
        System.out.println("=== Lecture 1: All present ===");
        course.markPresent();       
        System.out.println("=== Lecture 2: Section A present, Section B absent ===");
        sectionA.markPresent();
        sectionB.markAbsent();
        System.out.println("=== Lecture 3: Only Adham and Eslam present ===");
        s1.markPresent();
        s3.markPresent();
        course.markAbsent();
        s1.markPresent();
        s3.markPresent();
        System.out.println("\nFinal Attendance Report:");
        course.printStructure();
    }
}