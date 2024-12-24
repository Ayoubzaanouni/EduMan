using System.ComponentModel.DataAnnotations.Schema;

namespace Admin.Models
{
    public class Depot
    {
        public int Id { get; set; }
        public DateTime? TimeStamp { get; set; }

        public string? FileName { get; set; }

        [NotMapped]
        public IFormFile? File { get; set; }

        public int AssignmentId { get; set; }

        public Assignment? Assignment { get; set; }

        public int EtudiantId { get; set; }

        public Etudiant? Etudiant { get; set; }

    }
}
