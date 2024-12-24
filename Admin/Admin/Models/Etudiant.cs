using Microsoft.EntityFrameworkCore;
using System;
using System.ComponentModel.DataAnnotations;

namespace Admin.Models
{
    [Index(nameof(Cin), IsUnique = true)]
    [Index(nameof(Nce), IsUnique = true)]
    [Index(nameof(Email), IsUnique = true)]

    public class Etudiant
    {
        public int Id { get; set; }

        [Required(ErrorMessage = "Le champ Nom est obligatoire.")]
        public string? Nom { get; set; }

        [Required(ErrorMessage = "Le champ Prenom est obligatoire.")]
        public string? Prenom { get; set; }

        [Required(ErrorMessage = "Le champ CIN est obligatoire.")]
        public string? Cin { get; set; }

        [Display(Name = "Numéro de carte d'étudiant")]
        public string? Nce { get; set; }

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

        [Display(Name = "Date de naissance")]
        [DataType(DataType.Date)]
        public DateTime? DateN { get; set; }

        public int ClasseId { get; set; }
        public string? NomComplet
        {
            get
            {
                return Nom + " " + Prenom;
            }
        }

        public Classe? Classe { get; set; }

        public ICollection<Evaluation>? Evaluations { get; set; }
    }
}
