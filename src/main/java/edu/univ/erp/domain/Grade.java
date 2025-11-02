package edu.univ.erp.domain;

public class GradeComponent {
    private int componentId;
    private int courseId;
    private String name;
    private double weight;

    public GradeComponent() {}

    public GradeComponent(int componentId, int courseId, String name, double weight) {
        this.componentId = componentId;
        this.courseId = courseId;
        this.name = name;
        this.weight = weight;
    }

    public int getComponentId() { return componentId; }
    public void setComponentId(int componentId) { this.componentId = componentId; }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
}
