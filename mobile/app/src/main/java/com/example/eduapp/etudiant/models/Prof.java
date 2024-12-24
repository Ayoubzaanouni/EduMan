package com.example.eduapp.etudiant.models;

import java.util.List;

public class Prof {
    public int id;
    public String nom;
    public String prenom;
    public String cin;
    public String email;
    public String password;
    public String confirmPassword;
    public String grade;
    public String nomComplet;
    public List<Enseigner> enseigners;
    public List<Assignment> assignments;
    public List<DemandeRatt> demandeRatts;
    public int departmentId;
    public Department department;
}
