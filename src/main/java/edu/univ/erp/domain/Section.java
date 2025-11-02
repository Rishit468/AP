package edu.univ.erp.domain;

public class Section {
    private int sectionId;
    private int courseId;
    private int instructorId;
    private String term;
    private String schedule;

    public Section() {}

    public Section(int sectionId, int courseId, int instructorId, String term, String schedule) {
        this.sectionId = sectionId;
        this.courseId = courseId;
        this.instructorId = instructorId;
        this.term = term;
        this.schedule = schedule;
    }

    public int getSectionId() { return sectionId; }
    public void setSectionId(int sectionId) { this.sectionId = sectionId; }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public int getInstructorId() { return instructorId; }
    public void setInstructorId(int instructorId) { this.instructorId = instructorId; }

    public String getTerm() { return term; }
    public void setTerm(String term) { this.term = term; }

    public String getSchedule() { return schedule; }
    public void setSchedule(String schedule) { this.schedule = schedule; }
}
