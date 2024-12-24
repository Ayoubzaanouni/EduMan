package com.example.eduapp.etudiant.models;

import java.util.List;

public class Department {
    public int id;
    public String name;
    public String responsable;

    public List<Classe> classes;
    public List<Salle> salles;
    public List<Prof> profs;
}
