package edu.univ.erp.domain;

import java.sql.Date;

public class Section {
    private int sectionId;
    private int courseId;
    private int instructorId;
    private String dayTime;
    private String room;
    private int capacity;
    private String semester;
    private int year;
    private Date dropDeadline;

    public Section() {}

    public Section(int sectionId, int courseId, int instructorId, String dayTime, String room,
                   int capacity, String semester, int year, Date dropDeadline) {
        this.sectionId = sectionId;
        this.courseId = courseId;
        this.instructorId = instructorId;
        this.dayTime = dayTime;
        this.room = room;
        this.capacity = capacity;
        this.semester = semester;
        this.year = year;
        this.dropDeadline = dropDeadline;
    }

    public int getSectionId() { return sectionId; }
    public void setSectionId(int sectionId) { this.sectionId = sectionId; }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public int getInstructorId() { return instructorId; }
    public void setInstructorId(int instructorId) { this.instructorId = instructorId; }

    public String getDayTime() { return dayTime; }
    public void setDayTime(String dayTime) { this.dayTime = dayTime; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public Date getDropDeadline() { return dropDeadline; }
    public void setDropDeadline(Date dropDeadline) { this.dropDeadline = dropDeadline; }

    @Override
    public String toString() {
        return "Section{" +
                "sectionId=" + sectionId +
                ", courseId=" + courseId +
                ", instructorId=" + instructorId +
                ", dayTime='" + dayTime + '\'' +
                ", room='" + room + '\'' +
                ", capacity=" + capacity +
                ", semester='" + semester + '\'' +
                ", year=" + year +
                ", dropDeadline=" + dropDeadline +
                '}';
    }
}
