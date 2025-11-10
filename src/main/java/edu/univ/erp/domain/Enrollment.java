package edu.univ.erp.domain;

public class Enrollment {
    private int enrollmentId;
    private int studentId;
    private int sectionId;
    private String status;

    // Optional joined info for display
    private String studentName;
    private String rollNo;
    private String courseCode;
    private String courseTitle;

    // --- FIELDS ADDED FROM ENROLLMENTDETAILS ---
    private String dayTime;
    private String room;
    private String instructorName;
    // ------------------------------------------

    public Enrollment() {}

    public Enrollment(int enrollmentId, int studentId, int sectionId, String status) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.sectionId = sectionId;
        this.status = status;
    }

    // ... (existing getters/setters for enrollmentId, studentId, etc.) ...

    public int getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(int enrollmentId) { this.enrollmentId = enrollmentId; }
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public int getSectionId() { return sectionId; }
    public void setSectionId(int sectionId) { this.sectionId = sectionId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }
    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public String getCourseTitle() { return courseTitle; }
    public void setCourseTitle(String courseTitle) { this.courseTitle = courseTitle; }

    // --- NEW GETTERS/SETTERS ---
    public String getDayTime() { return dayTime; }
    public void setDayTime(String dayTime) { this.dayTime = dayTime; }
    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
    public String getInstructorName() { return instructorName; }
    public void setInstructorName(String instructorName) { this.instructorName = instructorName; }
    // ---------------------------

    @Override
    public String toString() {
        return "Enrollment{" +
                "enrollmentId=" + enrollmentId +
                ", studentId=" + studentId +
                ", sectionId=" + sectionId +
                ", status='" + status + '\'' +
                ", studentName='" + studentName + '\'' +
                ", rollNo='" + rollNo + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", courseTitle='" + courseTitle + '\'' +
                ", dayTime='" + dayTime + '\'' +
                ", room='" + room + '\'' +
                ", instructorName='" + instructorName + '\'' +
                '}';
    }
}
