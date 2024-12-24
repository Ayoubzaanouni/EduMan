package com.example.eduapp.etudiant.models;
import androidx.annotation.Nullable;

import java.io.File;


public class Depot {

    int id;
    @Nullable
    File file ;
    int Assignment_id;
    int Etudiant_id;

    public Depot() {
    }

    public Depot(@Nullable File file, int assignment_id, int etudiant_id) {

        this.file = file;
        Assignment_id = assignment_id;
        Etudiant_id = etudiant_id;
    }

    public Depot(String param, String param1) {
        this.Assignment_id = Integer.parseInt(param);
        this.Etudiant_id = Integer.parseInt(param1);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getAssignment_id() {
        return Assignment_id;
    }

    public void setAssignment_id(int assignment_id) {
        Assignment_id = assignment_id;
    }

    public int getEtudiant_id() {
        return Etudiant_id;
    }

    public void setEtudiant_id(int etudiant_id) {
        Etudiant_id = etudiant_id;
    }

    @Override
    public String toString() {
        return "Depot{" +
                "id=" + id +
                ", file=" + file +
                ", Assignment_id=" + Assignment_id +
                ", Etudiant_id=" + Etudiant_id +
                '}';
    }
}
