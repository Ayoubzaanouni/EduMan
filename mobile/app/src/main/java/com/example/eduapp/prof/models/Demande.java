package com.example.eduapp.prof.models;

import java.time.LocalDate;

public class Demande {

    private String type;
    private String dateRatt;
    private String heureDebut;
    private String heureFin;
    private String matiere;
    private String classe;
    private int profId;

    // Default constructor
    public Demande() {
    }

    // Getters and setters


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDateRatt() {
        return dateRatt;
    }

    public void setDateRatt(String dateRatt) {
        this.dateRatt = dateRatt;
    }

    public String getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(String heureDebut) {
        this.heureDebut = heureDebut;
    }

    public String getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(String heureFin) {
        this.heureFin = heureFin;
    }

    public String getMatiere() {
        return matiere;
    }

    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public int getProfId() {
        return profId;
    }

    public void setProfId(int profId) {
        this.profId = profId;
    }
}

