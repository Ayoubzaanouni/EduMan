package com.example.eduapp.etudiant.models;

import java.time.LocalDateTime;

public class Assignment {
    private int id;
    private String title;
    private String description;
    private LocalDateTime deadline;
    private String FileName;
    private String ProfName;
    private String MatiereName;

    public Assignment() {
    }

    public Assignment( int id, String title, String description, LocalDateTime deadline, String fileName, String profName, String matiereName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        FileName = fileName;
        ProfName = profName;
        MatiereName = matiereName;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getProfName() {
        return ProfName;
    }

    public void setProfName(String profName) {
        ProfName = profName;
    }

    public String getMatiereName() {
        return MatiereName;
    }

    public void setMatiereName(String matiereName) {
        MatiereName = matiereName;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", deadline=" + deadline +
                ", FileName='" + FileName + '\'' +
                ", ProfName='" + ProfName + '\'' +
                ", MatiereName='" + MatiereName + '\'' +
                '}';
    }
}
