package com.example.eduapp.etudiant.models;

public class AssClasse {
    public int id;
    public int classeId;

    public Classe classe;

    public int assignmentId;
    public Assignment assignment;

    public String toString(Object obj) {
        return "Id: " + id + " ClasseId: " + classeId + " AssignmentId: " + assignmentId;
    }
}

