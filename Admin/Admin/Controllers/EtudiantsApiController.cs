using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Admin.Data;
using Admin.Models;
using BCrypt;
using System.ComponentModel.DataAnnotations;
using System.Drawing;
using SkiaSharp;
using Microsoft.Extensions.Hosting.Internal;


namespace Admin.Controllers
{
    [Route("api/etudiants")]
    [ApiController]
    public class EtudiantsApiController : ControllerBase
    {
        private readonly AppDbContext _context;
        private readonly IWebHostEnvironment _hostingEnvironment;
        private readonly string _uploadFolderPath;

        public EtudiantsApiController(AppDbContext context, IWebHostEnvironment hostingEnvironment)
        {
            _context = context;
            _hostingEnvironment = hostingEnvironment;
            //_uploadFolderPath = Path.Combine(_hostingEnvironment.WebRootPath, "files/depots");
            _uploadFolderPath = Path.Combine(Directory.GetCurrentDirectory(), "wwwroot", "files/depots");

        }

        // GET: api/EtudiantsApi
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Etudiant>>> GetEtudiant()
        {
            return await _context.Etudiant.ToListAsync();
        }

        // GET: api/EtudiantsApi/5
        [HttpGet("{id}")]
        public async Task<ActionResult<Etudiant>> GetEtudiant(int id)
        {
            var etudiant = await _context.Etudiant.FindAsync(id);

            if (etudiant == null)
            {
                return NotFound();
            }

            return etudiant;
        }

        // PUT: api/EtudiantsApi/5
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPut("{id}")]
        public async Task<IActionResult> PutEtudiant(int id, Etudiant etudiant)
        {
            if (id != etudiant.Id)
            {
                return BadRequest();
            }

            _context.Entry(etudiant).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!EtudiantExists(id))
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

        // POST: api/EtudiantsApi
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPost]
        public async Task<ActionResult<Etudiant>> PostEtudiant(Etudiant etudiant)
        {
            _context.Etudiant.Add(etudiant);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetEtudiant", new { id = etudiant.Id }, etudiant);
        }

        // DELETE: api/EtudiantsApi/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteEtudiant(int id)
        {
            var etudiant = await _context.Etudiant.FindAsync(id);
            if (etudiant == null)
            {
                return NotFound();
            }

            _context.Etudiant.Remove(etudiant);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        private bool EtudiantExists(int id)
        {
            return _context.Etudiant.Any(e => e.Id == id);
        }
        private bool verifyPassword(String enteredPassword, String hashedPassword)
        {
            return (BCrypt.Net.BCrypt.Verify(enteredPassword, hashedPassword));
        }

        [HttpPost("login")]
        public async Task<ActionResult<string>> Login([FromBody] LoginModel loginModel)
        {
            var etudiantFromDb = await _context.Etudiant.FirstOrDefaultAsync(e => e.Email == loginModel.Email);

            if (etudiantFromDb != null)
            {
                if (!verifyPassword(loginModel.Password, etudiantFromDb.Password))
                {
                    return Unauthorized();
                }

                return "Etudiant," + etudiantFromDb.Id + "," + etudiantFromDb.ClasseId;
            }
            var profFromDb = await _context.Prof.FirstOrDefaultAsync(p => p.Email == loginModel.Email);

            if (profFromDb != null)
            {
                if (!verifyPassword(loginModel.Password, profFromDb.Password))
                {
                    return Unauthorized();
                }
                return "Prof," + profFromDb.Id;
            }
            return NotFound();
        }

        public class LoginModel
        {
            public string Email { get; set; }
            public string Password { get; set; }
        }

        [HttpGet("InfoEtud")]
        public async Task<ActionResult<IEnumerable<object>>> Info([FromQuery] string email)
        {
            var result = from etudiant in _context.Etudiant
                         join classe in _context.Classe on etudiant.ClasseId equals classe.Id
                         where etudiant.Email == email
                         select new
                         {
                             EtudiantName = etudiant.Nom,
                             EtudiantPrenom = etudiant.Prenom,
                             EtudiantNomComplet = etudiant.NomComplet,
                             ClasseCode = classe.CodeClasse,
                             ClasseId = classe.Id,
                             EtudiantId = etudiant.Id,
                             EtudiantEmail = etudiant.Email,
                             EtudiantCin = etudiant.Cin,
                             EtudiantNce = etudiant.Nce,
                             EtudiantDateN = etudiant.DateN,
                         };
            return Ok(result.ToList());
        }

        [HttpGet("AssClasses")]
        public async Task<ActionResult<IEnumerable<object>>> GetAssClasses([FromQuery] int? classId)
        {
            if (classId == null)
            {
                // Retrieve all assignment classes
                var allAssClasses = await _context.AssClasse.ToListAsync();

                // You may customize the returned data structure based on your needs
                var results = allAssClasses.Select(ac => new
                {
                    ac.Id,
                    ac.ClasseId,
                    ac.AssignmentId,
                    // Include other properties if needed
                }).ToList();

                return Ok(results);
            }
            else
            {
                // Retrieve assignment classes by class ID
                var assClassesByClassId = await _context.AssClasse
                    .Where(ac => ac.ClasseId == classId)
                    .ToListAsync();

                // You may customize the returned data structure based on your needs
                var results = assClassesByClassId.Select(ac => new
                {
                    ac.Id,
                    ac.ClasseId,
                    ac.AssignmentId,
                    // Include other properties if needed
                }).ToList();

                return Ok(results);
            }
        }

        [HttpGet("Assignments")]
        public async Task<ActionResult<IEnumerable<object>>> Assignments([FromQuery] int? idc)
        {


            var results = from assignment in _context.Assignment
                          where assignment.AssClasses.Any(ac => ac.ClasseId == idc)
                          select new

                          {
                              assignment.Id,
                              assignment.Title,
                              assignment.Description,
                              assignment.Deadline,
                              assignment.FileName,
                              //assignment.AssClasses,

                              //select classeID from using
                              AssClasses = assignment.AssClasses.Select(ac => new
                              {
                                  ac.ClasseId,
                                  ac.Classe.CodeClasse,
                              }).ToList(),

                              assignment.MatiereId,
                              //select matiere name using matiereId
                              MatiereName = _context.Matiere.Where(m => m.Id == assignment.MatiereId).FirstOrDefault().NameMatiere,
                              assignment.ProfId,
                              //select prof name using profId
                              ProfName = _context.Prof.Where(p => p.Id == assignment.ProfId).FirstOrDefault().NomComplet,
                              //assignment.SelectedClasses,
                          };

            return Ok(results.ToList());

        }


        [HttpGet("AllAssignments")]
        public async Task<ActionResult<IEnumerable<object>>> AllAssignments([FromQuery] int? idc)
        {


            var results = from assignment in _context.Assignment
                          select new

                          {
                              assignment.Id,
                              assignment.Title,
                              assignment.Description,
                              assignment.Deadline,
                              assignment.FileName,
                              //assignment.AssClasses,

                              //select classeID from using
                              AssClasses = assignment.AssClasses.Select(ac => new
                              {
                                  ac.ClasseId,
                                  ac.Classe.CodeClasse,
                              }).ToList(),

                              assignment.MatiereId,
                              //select matiere name using matiereId
                              MatiereName = _context.Matiere.Where(m => m.Id == assignment.MatiereId).FirstOrDefault().NameMatiere,
                              assignment.ProfId,
                              //select prof name using profId
                              ProfName = _context.Prof.Where(p => p.Id == assignment.ProfId).FirstOrDefault().NomComplet,
                              //assignment.SelectedClasses,
                          };

            return Ok(results.ToList());

        }

        [HttpGet("Depots")]
        public async Task<ActionResult<IEnumerable<object>>> Depots([FromQuery] int? idc)
        {
            var results = from depot in _context.Depot
                          select new
                          {
                              depot.Id,
                              depot.TimeStamp,
                              depot.FileName,
                              depot.AssignmentId,
                              depot.EtudiantId,
                              EtudiantName = _context.Etudiant.Where(e => e.Id == depot.EtudiantId).FirstOrDefault().NomComplet,


                          };
            return Ok(results.ToList());

        }

        [HttpGet("EtudDepots")]
        public async Task<ActionResult<IEnumerable<object>>> EtudDepots([FromQuery] int? id)
        {
            var results = from depot in _context.Depot
                          where depot.EtudiantId == id
                          select new
                          {
                              depot.Id,
                              depot.TimeStamp,
                              depot.FileName,
                              depot.AssignmentId,
                              depot.EtudiantId,
                              EtudiantName = _context.Etudiant.Where(e => e.Id == depot.EtudiantId).FirstOrDefault().NomComplet,


                          };
            return Ok(results.ToList());

        }

        [HttpPost("CreateDepot")]
        public async Task<ActionResult<Depot>> CreateDepot([FromForm] DepotCreateModel depotModel)
        {
            try
            {
                // Validate the model if needed

                if (depotModel.File != null)
                {
                    // Process and save the file
                    string folder = "files/depots/";
                    string uniqueFileName = Guid.NewGuid().ToString() + "_" + depotModel.File.FileName;
                    string filePath = Path.Combine(_hostingEnvironment.WebRootPath, folder, uniqueFileName);

                    using (var stream = new FileStream(filePath, FileMode.Create))
                    {
                        await depotModel.File.CopyToAsync(stream);
                    }

                    // Create a new Depot instance
                    var newDepot = new Depot
                    {
                        FileName = folder + uniqueFileName,
                        AssignmentId = depotModel.AssignmentId,
                        EtudiantId = depotModel.EtudiantId,
                        TimeStamp = DateTime.Now
                        // Additional properties initialization if needed
                    };

                    // Add the new Depot to the context
                    _context.Depot.Add(newDepot);

                    // Save changes to the database
                    await _context.SaveChangesAsync();

                    // Return the newly created Depot
                    return CreatedAtAction(nameof(GetDepot), new { id = newDepot.Id }, newDepot);
                }
                else
                {
                    // Handle the case when no file is provided
                    return BadRequest("No file provided");
                }
            }
            catch (Exception ex)
            {
                // Handle any exception, log, and return an appropriate response
                return StatusCode(500, $"Internal server error: {ex.Message}");
            }
        }
        public class DepotCreateModel
        {
            public IFormFile File { get; set; }
            public int AssignmentId { get; set; }
            public int EtudiantId { get; set; }
        }

        [HttpGet("depots/{id}")]
        public async Task<ActionResult<Depot>> GetDepot(int id)
        {
            var depot = await _context.Depot.FindAsync(id);

            if (depot == null)
            {
                return NotFound();
            }

            return depot;
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

        [HttpPost("CreateFakeDepot")]
        public async Task<ActionResult<Depot>> CreateFakeDepot([FromBody] FakeDepotCreateModel depotModel)
        {
            try
            {
                // Validate the model if needed

                // Check if FileName is provided
                if (!string.IsNullOrEmpty(depotModel.FileName))
                {
                    // Create a new Depot instance
                    var newDepot = new Depot
                    {
                        FileName = "files/depots/" + depotModel.FileName,
                        AssignmentId = depotModel.AssignmentId,
                        EtudiantId = depotModel.EtudiantId,
                        TimeStamp = DateTime.Now
                        // Additional properties initialization if needed
                    };

                    // Add the new Depot to the context
                    _context.Depot.Add(newDepot);

                    // Save changes to the database
                    await _context.SaveChangesAsync();

                    // Return the newly created Depot
                    return CreatedAtAction(nameof(GetDepot), new { id = newDepot.Id }, newDepot);
                }
                else
                {
                    // Handle the case when no FileName is provided
                    return BadRequest("No FileName provided");
                }
            }
            catch (Exception ex)
            {
                // Handle any exception, log, and return an appropriate response
                return StatusCode(500, $"Internal server error: {ex.Message}");
            }
        }


        public class FakeDepotCreateModel
        {
            public string FileName { get; set; }
            public int AssignmentId { get; set; }
            public int EtudiantId { get; set; }
        }


        [HttpGet("emploi/{classeId}")]
        public IActionResult GetProfessorScheduleImageForCurrentWeek(int classeId)
        {
            // Get the schedule items from the database
            var scheduleItems = GetProfessorScheduleForCurrentWeek(classeId);

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
                if (day == DateOnly.FromDateTime(DateTime.Now).DayOfWeek)
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
                    string matiereText = $"{item.NameMatiere} {item.Salle.CodeSalle}";
                    if (item.Rattrapage == true)
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
                    string hoursClasseText = $"{item.HeureDebut} - {item.HeureFin}  ";
                    canvas.DrawText(hoursClasseText, x, y + textSizeMatiere, paint);
                }
            }
        }



        private List<dynamic> GetProfessorScheduleForCurrentWeek(int classeid)
        {
            // Your existing code for getting schedule items
            // ...
            DateOnly currentDate = DateOnly.FromDateTime(DateTime.Now);
            DateOnly startOfWeek = currentDate.AddDays(-(int)currentDate.DayOfWeek);
            DateOnly endOfWeek = startOfWeek.AddDays(6);

            // Query the database to get the schedule items for the specified professor within the current week
            var scheduleItems = _context.Enseigner
                .Where(e => e.ClasseId == classeid && e.DateSeance >= startOfWeek && e.DateSeance <= endOfWeek)
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
