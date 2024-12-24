using System.ComponentModel.DataAnnotations;

namespace Admin.Models
{
    public class DemandeRatt
    {
        public int Id { get; set; }
        public string? Type { get; set; }

        [Display(Name = "Date Rattrapage")]
        [DataType(DataType.Date)]
        public DateOnly? DateRatt { get; set; }

        [DataType(DataType.Time)]
        public string? HeureDebut { get; set; }

        [DataType(DataType.Time)]
        public string? HeureFin { get; set; }

        public string? Matiere { get; set; }
        public string? Classe { get; set; }

        public int ProfId { get; set; }

        public Prof? Prof { get; set; }

    }
}
