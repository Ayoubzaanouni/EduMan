using System.ComponentModel.DataAnnotations;

namespace Admin.Models
{
    public class Evaluation
    {
        public int Id { get; set; }
        public string? Type { get; set; }

        [Display(Name = "Date")]
        [DataType(DataType.Date)]
        public DateOnly? Date { get; set; }

        public float? Note { get; set; }

        public int MatiereId { get; set; }

        public Matiere? Matiere { get; set; }
        public int EtudiantId { get; set; }

        public Etudiant? Etudiant { get; set; }

    }
}
