using Microsoft.EntityFrameworkCore;
using System.ComponentModel.DataAnnotations;

namespace Admin.Models
{
   [Index(nameof(CodeClasse), IsUnique = true)]
   [Index(nameof(NameClasse), IsUnique = true)]

    public class Classe
    {
        public int Id { get; set; }
        //unique annotation
        public string? CodeClasse { get; set; }
        public string? NameClasse { get; set; }

        [Display(Name = "Etudiants")]
        public ICollection<Etudiant>? Etudiants { get; set; }

        public int DepartmentId { get; set; }

        public Department? Department { get; set; }
        public ICollection<Enseigner>? Enseigners { get; set; }
        public ICollection<AssClasse>? AssClasses { get; set; }



    }
}
