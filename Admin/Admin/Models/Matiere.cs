using System.ComponentModel.DataAnnotations;

namespace Admin.Models
{
    public class Matiere
    {
        public int Id { get; set; }

        [Required(ErrorMessage = "Le champ Nom de la matière est obligatoire.")]
        public string? NameMatiere { get; set; }

        [Required(ErrorMessage = "Le champ Coefficient est obligatoire.")]
        [Range(0.1, double.MaxValue, ErrorMessage = "Veuillez entrer une valeur supérieure à zéro.")]
        public float? Coefficient { get; set; }

        [Required(ErrorMessage = "Le champ Taux Horaire est obligatoire.")]
        [Range(1, int.MaxValue, ErrorMessage = "Veuillez entrer une valeur supérieure à zéro.")]
        public int? TauxHoraire { get; set; }

        public ICollection<Evaluation>? Evaluations { get; set; }
        public ICollection<Enseigner>? Enseigners { get; set; }


    }
}
