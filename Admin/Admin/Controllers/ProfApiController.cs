using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Admin.Data;
using Admin.Models;
using Microsoft.AspNetCore.Mvc.Rendering;
using SkiaSharp;
using System.Net.Http;
using System.Diagnostics;
using System.Drawing.Imaging;
using System.Runtime.Intrinsics.X86;
using System.Net.Http;
using System.Text.Json.Serialization;

namespace Admin.Controllers
{
    [Route("api/profs")]
    [ApiController]
    public class ProfApiController : ControllerBase
    {
        private readonly AppDbContext _context;
        private readonly IWebHostEnvironment _hostingEnvironment;
        private readonly string _uploadFolderPath;


        public ProfApiController(AppDbContext context,  IWebHostEnvironment hostingEnvironment)
        {
            _context = context;
            _hostingEnvironment = hostingEnvironment;
            _uploadFolderPath = Path.Combine(Directory.GetCurrentDirectory(), "wwwroot", "files/assignments");

            // Create the upload folder if it doesn't exist
            if (!Directory.Exists(_uploadFolderPath))
            {
                Directory.CreateDirectory(_uploadFolderPath);
            }

        }

        // GET: api/DemandeRatts1
        [HttpGet]
        public async Task<ActionResult<IEnumerable<DemandeRatt>>> GetDemandeRatt()
        {
            return await _context.DemandeRatt.ToListAsync();
        }

        // GET: api/DemandeRatts1/5
        [HttpGet("{id}")]
        public async Task<ActionResult<DemandeRatt>> GetDemandeRatt(int id)
        {
            var demandeRatt = await _context.DemandeRatt.FindAsync(id);

            if (demandeRatt == null)
            {
                return NotFound();
            }

            return demandeRatt;
        }

        // PUT: api/DemandeRatts1/5
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPut("{id}")]
        public async Task<IActionResult> PutDemandeRatt(int id, DemandeRatt demandeRatt)
        {
            if (id != demandeRatt.Id)
            {
                return BadRequest();
            }

            _context.Entry(demandeRatt).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!DemandeRattExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return NoContent();
        }

        // POST: api/DemandeRatts1
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPost]
        public async Task<ActionResult<DemandeRatt>> PostDemandeRatt(DemandeRatt demandeRatt)
        {
            _context.DemandeRatt.Add(demandeRatt);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetDemandeRatt", new { id = demandeRatt.Id }, demandeRatt);
        }

        // DELETE: api/DemandeRatts1/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteDemandeRatt(int id)
        {
            var demandeRatt = await _context.DemandeRatt.FindAsync(id);
            if (demandeRatt == null)
            {
                return NotFound();
            }

            _context.DemandeRatt.Remove(demandeRatt);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        private bool DemandeRattExists(int id)
        {
            return _context.DemandeRatt.Any(e => e.Id == id);
        }

        [HttpGet("InfoProf")]
        public async Task<ActionResult<IEnumerable<object>>> Info([FromQuery] string email)
        {
            var result = from prof in _context.Prof
                            where prof.Email == email
                            select new
                            {
                                prof.Id,
                                prof.Nom,
                                prof.Prenom,
                                prof.Cin,
                                prof.Email,
                                prof.Password,
                                prof.ConfirmPassword,
                                prof.Grade,
                                prof.DepartmentId
                            };
            return Ok(result.ToList());
        }


        [HttpGet("Matieres")]
        public async Task<ActionResult<IEnumerable<object>>> Mat()
        {
            SelectList marieres = new SelectList(_context.Matiere, "Id", "NameMatiere");
            // new list of classes and matieres in the same list
            List<object> list = [marieres];
            return Ok(list);
        }
        [HttpGet("classes")]
        public async Task<ActionResult<IEnumerable<object>>> Clas()
        {
            SelectList classes = new SelectList(_context.Classe, "Id", "CodeClasse");
            // new list of classes and matieres in the same list
            List<object> list = [classes];
            return Ok(list);
        }


        [HttpGet("listratt")]
        public async Task<ActionResult<IEnumerable<object>>> ListRatt([FromQuery] int id)
        {
            var result = from DemandeRatt in _context.DemandeRatt
                         join prof in _context.Prof on DemandeRatt.ProfId equals prof.Id
                         where prof.Id == id
                         orderby DemandeRatt.DateRatt descending
                         select new
                         {
                             DemandeType = DemandeRatt.Type,
                             DemandeDateRatt = DemandeRatt.DateRatt,
                             DemandeHeureDebut = DemandeRatt.HeureDebut,
                             DemandeHeureFin = DemandeRatt.HeureFin,
                             DemandeMatiere = DemandeRatt.Matiere,
                             DemandeClasse = DemandeRatt.Classe,


                         };
            return Ok(result.ToList());
        }
        [HttpPost("file")]
        public async Task<IActionResult> UploadFile(IFormFile file)
        {
            try
            {
                if (file == null || file.Length == 0)
                {
                    return BadRequest("No file received.");
                }

                // Generate a unique file name to prevent overwriting
                var fileName = $"{Guid.NewGuid()}_{file.FileName}";

                // Combine the upload folder path with the file name
                var filePath = Path.Combine(_uploadFolderPath, fileName);

                // Save the file to the server
                using (var stream = new FileStream(filePath, FileMode.Create))
                {
                    await file.CopyToAsync(stream);
                }

                return Ok(new { fileName });
            }
            catch (Exception ex)
            {
                return StatusCode(500, $"Internal server error: {ex.Message}");
            }
        }
        public class AssignmentInputModel
        {
            public string Title { get; set; }
            public string Description { get; set; }
            public DateTime Deadline { get; set; }
            public string FileName { get; set; }
            public int MatiereId { get; set; }
            public int ProfId { get; set; }
            public List<int> SelectedClasses { get; set; }

        }
        public class DepotFakeInputModel
        {
            public int AssignmentId { get; set; }
            public int EtudiantId { get; set; }
            public string FileName { get; set; }
        }

        [HttpPost("depot")]
        public async Task<ActionResult<Depot>> PostDepot1([FromBody] DepotFakeInputModel depotInput)
        {
            Depot dep = new Depot
            {
                AssignmentId = depotInput.AssignmentId,
                EtudiantId = depotInput.EtudiantId,
                FileName = depotInput.FileName,
                TimeStamp = DateTime.Now
            };

            // Add the Depot to the context
            _context.Depot.Add(dep);

            // Save changes to generate the DepotId
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetDepot", new { id = dep.Id }, dep);
        }


      

        [HttpPost("assignment")]
        public async Task<ActionResult<Assignment>> PostAssignment([FromBody] AssignmentInputModel assignmentInput)
        {
            Debug.WriteLine("title: " + assignmentInput.Title);

            Assignment ass = new Assignment
            {
                Title = assignmentInput.Title,
                Description = assignmentInput.Description,
                Deadline = assignmentInput.Deadline,
                FileName = assignmentInput.FileName,
                MatiereId = assignmentInput.MatiereId,
                ProfId = assignmentInput.ProfId,
                SelectedClasses = assignmentInput.SelectedClasses
            };

            // Add the Assignment to the context
            _context.Assignment.Add(ass);

            // Save changes to generate the AssignmentId
            await _context.SaveChangesAsync();

            foreach (var item in ass.SelectedClasses)
            {
                Debug.WriteLine(item.ToString());
                AssClasse assClasse = new AssClasse();
                assClasse.AssignmentId = ass.Id;
                assClasse.ClasseId = item;

                Debug.WriteLine("id: " + assClasse.Id + "     classeid: " + assClasse.ClasseId + "assid:" + assClasse.AssignmentId);

                // Add the AssClasse to the context
                _context.Add(assClasse);
            }

            // Save changes to add the AssClasse records
            await _context.SaveChangesAsync();

            // Remove the circular reference for serialization
            ass.AssClasses = null;

            return CreatedAtAction("GetAssignment", new { id = ass.Id }, ass);
        }

        [HttpGet("listass")]
        public async Task<ActionResult<IEnumerable<object>>> ListAss([FromQuery] int id)
        {
            var result = from Assignment in _context.Assignment
                         join prof in _context.Prof on Assignment.ProfId equals prof.Id
                         where prof.Id == id
                         orderby Assignment descending
                         select new
                         {
                             AssignmentId = Assignment.Id,
                             AssignmentTitle = Assignment.Title,
                             AssignmentDesc = Assignment.Description,
                             AssignmentDeadLine = Assignment.Deadline,
                             AssignmentFile = Assignment.FileName,
                             AssignmentMatiere = Assignment.Matiere.NameMatiere,
                         };
            return Ok(result.ToList());
        }
        [HttpGet("depots")]
        public async Task<ActionResult<IEnumerable<object>>> ListDepots([FromQuery] int ida,int idc)
        {
            var result = from Depot in _context.Depot
                         join ass in _context.Assignment on Depot.AssignmentId equals ass.Id
                         where (ass.Id == ida) && (Depot.Etudiant.Classe.Id == idc)
                         orderby Depot.TimeStamp ascending
                         select new
                         {
                             DepotId = Depot.Id,
                             DepotAssignmentId = Depot.AssignmentId,
                             DepotEtudiantNom = Depot.Etudiant.NomComplet,
                             DepotEtudiantClasse = Depot.Etudiant.Classe.CodeClasse,
                             DepotEtudiantNce = Depot.Etudiant.Nce,
                             DepotEtudiantFile = Depot.FileName,
                             DepotEtudiantTime = Depot.TimeStamp,
                         };
            return Ok(result.ToList());
        }
        [HttpGet("assclasses")]
        public async Task<ActionResult<IEnumerable<object>>> ListAssClasses([FromQuery] int id)
        {
            var result = from AssClasse in _context.AssClasse
                         join classe in _context.Classe on AssClasse.ClasseId equals classe.Id
                         where AssClasse.AssignmentId == id
                         select new
                         {
                            AssClasseId = AssClasse.ClasseId,
                            AssClasseName = AssClasse.Classe.CodeClasse,
                            AssId = AssClasse.AssignmentId,
                         };
            return Ok(result.ToList());
        }

        [HttpGet("emploi/{professorId}")]
        public IActionResult GetProfessorScheduleImageForCurrentWeek(int professorId)
        {
            // Get the schedule items from the database
            var scheduleItems = GetProfessorScheduleForCurrentWeek(professorId);

            // Create a bitmap to draw the schedule
            using (var bitmap = new SKBitmap(1100, 900))
            {
                // Create an SKImageInfo with the same dimensions as the bitmap
                var info = new SKImageInfo(bitmap.Width, bitmap.Height);

                // Create an SKSurface using the SKImageInfo
                using (var surface = SKSurface.Create(info))
                {
                    var canvas = surface.Canvas;
                    canvas.Clear(SKColors.White);


                    // Set up paint for drawing
                    var paint = new SKPaint
                    {
                        Color = SKColors.Black,
                        TextSize = 18.0f,
                        TextAlign = SKTextAlign.Left,
                        FakeBoldText = true,
                    };

                    // Draw headers (days and hours)

                    // Draw schedule items on the table with new formatting
                    DrawScheduleTableWithNewFormat(canvas, paint, scheduleItems);

                    // Save the bitmap as a PNG image
                    using (var stream = new MemoryStream())
                    {
                        // Encode and save the image directly from the canvas
                        surface.Snapshot().Encode(SKEncodedImageFormat.Png, 100).SaveTo(stream);
                        return File(stream.ToArray(), "image/png");
                    }
                }
            }
        }

        private void DrawScheduleTableWithNewFormat(SKCanvas canvas, SKPaint paint, List<dynamic> scheduleItems)
        {
            var paint_jour = new SKPaint
            {
                Color = SKColors.Cyan,
                TextSize = 18.0f,
                TextAlign = SKTextAlign.Left,
            };
            float startX = 50.0f;
            float startY = 80.0f; // Adjust the starting Y position
            float textSizeMatiere = 18.0f;
            float textSizeHoursClasse = 16.0f;

            foreach (var day in Enum.GetValues(typeof(DayOfWeek)).Cast<DayOfWeek>())
            {
                float x = startX + ((int)day * 150.0f);
                float y = startY;

                // Draw day header
                if(day == DateOnly.FromDateTime(DateTime.Now).DayOfWeek)
                {
                    paint.Color = SKColors.DarkBlue;
                }
                else
                {
                    paint.Color = SKColors.Black;
                }
                canvas.DrawText(day.ToString(), x, y, paint);

                // Filter schedule items for the current day
                var dayScheduleItems = scheduleItems.Where(item => item.DayOfWeek == day.ToString());

                // Draw schedule items for the current day
                foreach (var item in dayScheduleItems)
                {
                    y += 45.0f;

                    // Draw Matiere with smaller text size
                    paint.TextSize = textSizeMatiere;
                    string matiereText = $"{item.NameMatiere}  {item.CodeClasse}";
                    if(item.Rattrapage == true)
                    {
                        paint.Color = SKColors.Red;
                        canvas.DrawText(matiereText, x, y, paint);
                    }
                    else if (item.Rattrapage == false)
                    {
                        paint.Color = SKColors.Black;
                        canvas.DrawText(matiereText, x, y, paint);
                    }
                    else
                    {
                        paint.Color = SKColors.Cyan;
                        canvas.DrawText(matiereText, x, y, paint);
                    }

                    // Draw Hours and Classe with even smaller text size
                    paint.TextSize = textSizeHoursClasse;
                    string hoursClasseText = $"{item.HeureDebut} - {item.HeureFin}  {item.Salle.CodeSalle}";
                    canvas.DrawText(hoursClasseText, x, y + textSizeMatiere , paint);
                }
            }
        }



        private List<dynamic> GetProfessorScheduleForCurrentWeek(int professorId)
        {
            // Your existing code for getting schedule items
            // ...
            DateOnly currentDate = DateOnly.FromDateTime(DateTime.Now);
            DateOnly startOfWeek = currentDate.AddDays(-(int)currentDate.DayOfWeek);
            DateOnly endOfWeek = startOfWeek.AddDays(6);

            // Query the database to get the schedule items for the specified professor within the current week
            var scheduleItems = _context.Enseigner
                .Where(e => e.ProfId == professorId && e.DateSeance >= startOfWeek && e.DateSeance <= endOfWeek)
                .Select(e => new
                {
                    e.DayOfWeek,
                    e.HeureDebut,
                    e.Salle,
                    e.HeureFin,
                    e.Matiere.NameMatiere, 
                    e.Classe.CodeClasse,
                    e.Rattrapage, // Assuming there is a CourseName property in Matiere
                                           // Add other relevant fields
                })
                .OrderBy(e => e.HeureDebut)
                .ToList();
            return scheduleItems.Cast<dynamic>().ToList();
        }

    }
}
