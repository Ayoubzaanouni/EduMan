using Newtonsoft.Json;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Text;

namespace Admin.Models
{
    public class Assignment
    {
        public int Id { get; set; }
       public string? Title { get; set; }
        public string? Description { get; set; }

        [Display(Name = "Dead Line")]
        [DataType(DataType.DateTime)]
        public DateTime? Deadline { get; set; }
        public string? FileName { get; set; }

        [NotMapped]
        public IFormFile? File { get; set; }

        public int MatiereId { get; set; }

        public Matiere? Matiere { get; set; }
        public int ProfId { get; set; }

        public Prof? Prof { get; set; }

        public ICollection<Depot>? Depots { get; set; }
        [JsonIgnore]
        public ICollection<AssClasse>? AssClasses { get; set; }

        [NotMapped]
        public virtual List<int>? SelectedClasses { get; set; }

    }
}
