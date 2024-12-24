using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Admin.Models
{
    public class Enseigner
    {
        public int Id { get; set; }

        public int ClasseId { get; set; }

        [ForeignKey("ClasseId")]
        public Classe? Classe { get; set; }

        [DataType(DataType.Date)]
        public DateOnly DateSeance { get; set; }

        [DataType(DataType.Time)]
        public string? HeureDebut { get; set; }

        [DataType(DataType.Time)]
        public string? HeureFin { get; set; }

        public bool? Rattrapage { get; set; }

        public int SalleId { get; set; }

        [ForeignKey("SalleId")]
        public Salle? Salle { get; set; }

        [ForeignKey("ProfId")]
        public int ProfId { get; set; }

        public Prof? Prof { get; set; }

        public int MatiereId { get; set; }

        [ForeignKey("MatiereId")]
        public Matiere? Matiere { get; set; }

        //make a function that return the day of the week based on the date
        public string DayOfWeek
        {
            get
            {
                return DateSeance.DayOfWeek.ToString();
            }
        }
    }
}
