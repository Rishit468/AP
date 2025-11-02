package edu.univ.erp.domain;

public class Enrollment {
    private int enrollmentId;
    private int studentId;
    private int sectionId;
    private String grade;

    public Enrollment() {}

    public Enrollment(int enrollmentId, int studentId, int sectionId, String grade) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.sectionId = sectionId;
        this.grade = grade;
    }

    public int getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(int enrollmentId) { this.enrollmentId = enrollmentId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getSectionId() { return sectionId; }
    public void setSectionId(int sectionId) { this.sectionId = sectionId; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
}
