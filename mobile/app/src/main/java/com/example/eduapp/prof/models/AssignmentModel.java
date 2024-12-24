package com.example.eduapp.prof.models;

public class AssignmentModel {
    private int assignmentId;
    private String assignmentTitle;
    private String assignmentDesc;
    private String assignmentDeadLine;
    private String assignmentFile;
    private String assignmentMatiere;

    public AssignmentModel() {
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getAssignmentTitle() {
        return assignmentTitle;
    }

    public void setAssignmentTitle(String assignmentTitle) {
        this.assignmentTitle = assignmentTitle;
    }

    public String getAssignmentDesc() {
        return assignmentDesc;
    }

    public void setAssignmentDesc(String assignmentDesc) {
        this.assignmentDesc = assignmentDesc;
    }

    public String getAssignmentDeadLine() {
        return assignmentDeadLine;
    }

    public void setAssignmentDeadLine(String assignmentDeadLine) {
        this.assignmentDeadLine = assignmentDeadLine;
    }

    public String getAssignmentFile() {
        return assignmentFile;
    }

    public void setAssignmentFile(String assignmentFile) {
        this.assignmentFile = assignmentFile;
    }

    public String getAssignmentMatiere() {
        return assignmentMatiere;
    }

    public void setAssignmentMatiere(String assignmentMatiere) {
        this.assignmentMatiere = assignmentMatiere;
    }
}
