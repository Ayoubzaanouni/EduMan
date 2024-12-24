using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using Admin.Data;
using Admin.Models;
using System.Diagnostics;

namespace Admin.Controllers
{
    public class AssignmentsController : Controller
    {
        private readonly AppDbContext _context;
        private readonly IWebHostEnvironment _hostingEnvironment;

        public AssignmentsController(AppDbContext context, IWebHostEnvironment hostingEnvironment)
        {
            _context = context;
            _hostingEnvironment = hostingEnvironment;
        }

        // GET: Assignments
        public async Task<IActionResult> Index()
        {
            var appDbContext = _context.Assignment.Include(a => a.Matiere).Include(a => a.Prof);

            return View(await appDbContext.ToListAsync());
        }

        // GET: Assignments/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var assignment = await _context.Assignment
                .Include(a => a.Matiere)
                .Include(a => a.Prof)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (assignment == null)
            {
                return NotFound();
            }

            return View(assignment);
        }

        // GET: Assignments/Create
        public IActionResult Create()
        {
            ViewData["MatiereId"] = new SelectList(_context.Matiere, "Id", "NameMatiere");
            ViewData["ProfId"] = new SelectList(_context.Prof, "Id", "NomComplet");
            ViewData["ClasseId"] = new SelectList(_context.Classe, "Id", "CodeClasse");

            return View();
        }

        // POST: Assignments/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Id,Title,Description,Deadline,FileName,MatiereId,ProfId,File,SelectedClasses,AssClasses")] Assignment assignment)
        {
            if (ModelState.IsValid)
            {
                //loop selected classes
               

                if (assignment.File != null)
                {
                    string folder = "files/assignments/";
                    folder += Guid.NewGuid().ToString() + "_" + assignment.File.FileName;
                    assignment.FileName = folder;
                    string serverFolder = Path.Combine(_hostingEnvironment.WebRootPath, folder);
                    await assignment.File.CopyToAsync(new FileStream(serverFolder, FileMode.Create));
                }
                _context.Add(assignment);
                await _context.SaveChangesAsync();
                foreach (var item in assignment.SelectedClasses)
                {
                    Debug.WriteLine(item.ToString());
                    AssClasse assClasse = new AssClasse();
                    assClasse.AssignmentId = assignment.Id;
                    assClasse.ClasseId = item;
                    Debug.WriteLine("id: "+assClasse.Id+"     classeid: "+assClasse.ClasseId+ "assid:"+assClasse.AssignmentId);
                    _context.Add(assClasse);

                }
                await _context.SaveChangesAsync();

                return RedirectToAction(nameof(Index));
            }
            ViewData["MatiereId"] = new SelectList(_context.Matiere, "Id", "NameMatiere", assignment.MatiereId);
            ViewData["ProfId"] = new SelectList(_context.Prof, "Id", "Cin", assignment.ProfId);
            ViewData["ClasseId"] = new SelectList(_context.Classe, "Id", "CodeClasse");
            return View(assignment);
        }

        // GET: Assignments/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var assignment = await _context.Assignment.FindAsync(id);
            if (assignment == null)
            {
                return NotFound();
            }
            ViewData["MatiereId"] = new SelectList(_context.Matiere, "Id", "NameMatiere", assignment.MatiereId);
            ViewData["ProfId"] = new SelectList(_context.Prof, "Id", "NomComplet", assignment.ProfId);
            return View(assignment);
        }

        // POST: Assignments/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("Id,Title,Description,Deadline,FileName,MatiereId,ProfId")] Assignment assignment)
        {
            if (id != assignment.Id)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    _context.Update(assignment);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!AssignmentExists(assignment.Id))
                    {
                        return NotFound();
                    }
                    else
                    {
                        throw;
                    }
                }
                return RedirectToAction(nameof(Index));
            }
            ViewData["MatiereId"] = new SelectList(_context.Matiere, "Id", "NameMatiere", assignment.MatiereId);
            ViewData["ProfId"] = new SelectList(_context.Prof, "Id", "Cin", assignment.ProfId);
            return View(assignment);
        }

        // GET: Assignments/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var assignment = await _context.Assignment
                .Include(a => a.Matiere)
                .Include(a => a.Prof)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (assignment == null)
            {
                return NotFound();
            }

            return View(assignment);
        }

        // POST: Assignments/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var assignment = await _context.Assignment.FindAsync(id);
            if (assignment != null)
            {
                _context.Assignment.Remove(assignment);
            }

            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool AssignmentExists(int id)
        {
            return _context.Assignment.Any(e => e.Id == id);
        }
    }
}
