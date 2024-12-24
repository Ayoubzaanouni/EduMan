using Admin.Models;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;

namespace Admin.Data
{
    public class AppDbContext:IdentityDbContext<AppUser>
    {
        public AppDbContext(DbContextOptions<AppDbContext> options):base(options)
        {

            
        }
        public DbSet<Admin.Models.Classe> Classe { get; set; } = default!;
        public DbSet<Admin.Models.Department> Department { get; set; } = default!;
        public DbSet<Admin.Models.Etudiant> Etudiant { get; set; } = default!;
        public DbSet<Admin.Models.Matiere> Matiere { get; set; } = default!;
        public DbSet<Admin.Models.Prof> Prof { get; set; } = default!;
        public DbSet<Admin.Models.Depot> Depot { get; set; } = default!;
        public DbSet<Admin.Models.Assignment> Assignment { get; set; } = default!;
        public DbSet<Admin.Models.DemandeRatt> DemandeRatt { get; set; } = default!;
        public DbSet<Admin.Models.Evaluation> Evaluation { get; set; } = default!;
        public DbSet<Admin.Models.Salle> Salle { get; set; } = default!;
        public DbSet<Admin.Models.Enseigner> Enseigner { get; set; } = default!;
        public DbSet<Admin.Models.AssClasse> AssClasse { get; set; } = default!;

        /*        public DbSet<Admin.Models.Classe> Classe { get; set; } = default!;
                public DbSet<Admin.Models.Etudiant> Etudiant { get; set; } = default!;*/
    }
}
