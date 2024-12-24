namespace Admin.Models
{
    public class AssClasse
    {
        public int Id { get; set; }
        public int ClasseId { get; set; }


        public Classe? Classe { get; set; }

        public int AssignmentId { get; set; }
        public Assignment? Assignment { get; set; }

        public string ToString(object? obj)
        {
            return "Id: " + Id + " ClasseId: " + ClasseId + " AssignmentId: " + AssignmentId;
        }
    }
}