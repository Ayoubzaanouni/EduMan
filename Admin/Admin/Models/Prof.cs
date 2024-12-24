using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Admin.Models
{
    public class Prof
    {
        public int Id { get; set; }

        [Required(ErrorMessage = "Le champ Nom est obligatoire.")]
        public string? Nom { get; set; }

        [Required(ErrorMessage = "Le champ Prenom est obligatoire.")]
        public string? Prenom { get; set; }

        [Required(ErrorMessage = "Le champ CIN est obligatoire.")]
        public string? Cin { get; set; }


        [Required(ErrorMessage = "Le champ Email est obligatoire.")]
        [EmailAddress(ErrorMessage = "Veuillez entrer une adresse e-mail valide.")]
        public string? Email { get; set; }

        [Required(ErrorMessage = "Le champ Mot de passe est obligatoire.")]
        [DataType(DataType.Password)]
        public string? Password { get; set; }

        [Compare("Password", ErrorMessage = "Les mots de passe ne correspondent pas.")]
        [DataType(DataType.Password)]
        [Display(Name = "Confirmer le mot de passe")]
        public string? ConfirmPassword { get; set; }

        //grade
        [Required(ErrorMessage = "Le champ Grade est obligatoire.")]
        public string? Grade { get; set; }
        public string? NomComplet { get {
            return Nom + " " + Prenom;
            } }

        public string? ImageName { get; set; }

        [NotMapped]
        public IFormFile? Image { get; set; }

        public ICollection<Enseigner>? Enseigners { get; set; }
        public ICollection<Assignment>? Assignments { get; set; }
        public ICollection<DemandeRatt>? DemandeRatts { get; set; }

        public int DepartmentId { get; set; }

        public Department? Department { get; set; }


    }
}
