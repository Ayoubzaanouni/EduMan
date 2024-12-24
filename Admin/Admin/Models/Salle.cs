using System.ComponentModel.DataAnnotations;

namespace Admin.Models
{
    public class Salle
    {
        public int Id { get; set; }

        [Required(ErrorMessage = "Le champ Code de la salle est obligatoire.")]
        public string? CodeSalle { get; set; }

        [Required(ErrorMessage = "Le champ Type de salle est obligatoire.")]
        public string? TypeSalle { get; set; }

        [Required(ErrorMessage = "Le champ Capacité est obligatoire.")]
        [RegularExpression("^[1-9][0-9]*$", ErrorMessage = "Veuillez entrer une valeur numérique positive.")]
        [Display(Name = "Capacite")]
        public int? Capacity { get; set; }

        [Display(Name = "Projecteur")]
        public bool? Projector { get; set; }

        public int DepartmentId { get; set; }

        public Department? Department { get; set; }

        public ICollection<Enseigner>? Enseigners { get; set; }

    }
}
