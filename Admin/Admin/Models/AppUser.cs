using Microsoft.AspNetCore.Identity;
using System.ComponentModel.DataAnnotations;

namespace Admin.Models
{
    public class AppUser:IdentityUser
    {
        [StringLength(100)]
        [MaxLength(100)]
        [Required]
        public String? Name { get; set; }

        public String? Adress { get; set; }
        public string? FirstName { get; set; }

        public string? Cin { get; set; }
        public string? Nce { get; set; }
        public DateTime? DateN { get; set; }

        public string? Speciality { get; set; }
    }
}
