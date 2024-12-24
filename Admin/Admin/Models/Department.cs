namespace Admin.Models
{
    public class Department
    {
        public int Id { get; set; }
        public string? Name { get; set; }
        public string? Responsable { get; set; }

        public ICollection<Classe>? Classes { get; set; }
        public ICollection<Salle>? Salles { get; set; }
        public ICollection<Prof>? Profs { get; set; }

    }
}
