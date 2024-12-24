using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using Admin.Data;
using Admin.Models;

namespace Admin.Controllers
{
    public class EnseignersController : Controller
    {
        private readonly AppDbContext _context;

        public EnseignersController(AppDbContext context)
        {
            _context = context;
        }

        // GET: Enseigners
        public async Task<IActionResult> Index()
        {
            var appDbContext = _context.Enseigner.Include(e => e.Classe).Include(e => e.Matiere).Include(e => e.Prof).Include(e => e.Salle);
            return View(await appDbContext.ToListAsync());
        }

        // GET: Enseigners/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var enseigner = await _context.Enseigner
                .Include(e => e.Classe)
                .Include(e => e.Matiere)
                .Include(e => e.Prof)
                .Include(e => e.Salle)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (enseigner == null)
            {
                return NotFound();
            }

            return View(enseigner);
        }

        // GET: Enseigners/Create
        public IActionResult Create()
        {
            ViewData["ClasseId"] = new SelectList(_context.Classe, "Id", "NameClasse");
            ViewData["MatiereId"] = new SelectList(_context.Matiere, "Id", "NameMatiere");
            ViewData["ProfId"] = new SelectList(_context.Prof, "Id", "NomComplet");
            ViewData["SalleId"] = new SelectList(_context.Salle, "Id", "CodeSalle");
            return View();
        }

        // POST: Enseigners/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Id,ClasseId,DateSeance,HeureDebut,HeureFin,Rattrapage,SalleId,ProfId,MatiereId")] Enseigner enseigner)
        {
            if (ModelState.IsValid)
            {
                _context.Add(enseigner);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            ViewData["ClasseId"] = new SelectList(_context.Classe, "Id", "Id", enseigner.ClasseId);
            ViewData["MatiereId"] = new SelectList(_context.Matiere, "Id", "NameMatiere", enseigner.MatiereId);
            ViewData["ProfId"] = new SelectList(_context.Prof, "Id", "Cin", enseigner.ProfId);
            ViewData["SalleId"] = new SelectList(_context.Salle, "Id", "CodeSalle", enseigner.SalleId);
            return View(enseigner);
        }

        // GET: Enseigners/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var enseigner = await _context.Enseigner.FindAsync(id);
            if (enseigner == null)
            {
                return NotFound();
            }
            ViewData["ClasseId"] = new SelectList(_context.Classe, "Id", "NameClasse", enseigner.ClasseId);
            ViewData["MatiereId"] = new SelectList(_context.Matiere, "Id", "NameMatiere", enseigner.MatiereId);
            ViewData["ProfId"] = new SelectList(_context.Prof, "Id", "NomComplet", enseigner.ProfId);
            ViewData["SalleId"] = new SelectList(_context.Salle, "Id", "CodeSalle", enseigner.SalleId);
            return View(enseigner);
        }

        // POST: Enseigners/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("Id,ClasseId,DateSeance,HeureDebut,HeureFin,Rattrapage,SalleId,ProfId,MatiereId")] Enseigner enseigner)
        {
            if (id != enseigner.Id)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    _context.Update(enseigner);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!EnseignerExists(enseigner.Id))
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
            ViewData["ClasseId"] = new SelectList(_context.Classe, "Id", "Id", enseigner.ClasseId);
            ViewData["MatiereId"] = new SelectList(_context.Matiere, "Id", "NameMatiere", enseigner.MatiereId);
            ViewData["ProfId"] = new SelectList(_context.Prof, "Id", "Cin", enseigner.ProfId);
            ViewData["SalleId"] = new SelectList(_context.Salle, "Id", "CodeSalle", enseigner.SalleId);
            return View(enseigner);
        }

        // GET: Enseigners/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var enseigner = await _context.Enseigner
                .Include(e => e.Classe)
                .Include(e => e.Matiere)
                .Include(e => e.Prof)
                .Include(e => e.Salle)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (enseigner == null)
            {
                return NotFound();
            }

            return View(enseigner);
        }

        // POST: Enseigners/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var enseigner = await _context.Enseigner.FindAsync(id);
            if (enseigner != null)
            {
                _context.Enseigner.Remove(enseigner);
            }

            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool EnseignerExists(int id)
        {
            return _context.Enseigner.Any(e => e.Id == id);
        }
    }
}
