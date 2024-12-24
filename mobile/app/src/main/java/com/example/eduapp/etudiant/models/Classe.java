package com.example.eduapp.etudiant.models;

import java.util.List;

public class Classe {
    public int id;
    public String codeClasse;
    public String nameClasse;

    public List<Etudiant> etudiants;

    public int departmentId;

    public Department department;
    public List<Enseigner> enseigners;
    public List<AssClasse> assClasses;
}
