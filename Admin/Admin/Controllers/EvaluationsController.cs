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
    public class EvaluationsController : Controller
    {
        private readonly AppDbContext _context;

        public EvaluationsController(AppDbContext context)
        {
            _context = context;
        }

        // GET: Evaluations
        public async Task<IActionResult> Index()
        {
            var appDbContext = _context.Evaluation.Include(e => e.Etudiant).Include(e => e.Matiere);
            return View(await appDbContext.ToListAsync());
        }

        // GET: Evaluations/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var evaluation = await _context.Evaluation
                .Include(e => e.Etudiant)
                .Include(e => e.Matiere)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (evaluation == null)
            {
                return NotFound();
            }

            return View(evaluation);
        }

        // GET: Evaluations/Create
        public IActionResult Create()
        {
            ViewData["EtudiantId"] = new SelectList(_context.Etudiant, "Id", "NomComplet");
            ViewData["MatiereId"] = new SelectList(_context.Matiere, "Id", "NameMatiere");
            return View();
        }

        // POST: Evaluations/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Id,Type,Date,Note,MatiereId,EtudiantId")] Evaluation evaluation)
        {
            if (ModelState.IsValid)
            {
                _context.Add(evaluation);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            ViewData["EtudiantId"] = new SelectList(_context.Etudiant, "Id", "Cin", evaluation.EtudiantId);
            ViewData["MatiereId"] = new SelectList(_context.Matiere, "Id", "NameMatiere", evaluation.MatiereId);
            return View(evaluation);
        }

        // GET: Evaluations/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var evaluation = await _context.Evaluation.FindAsync(id);
            if (evaluation == null)
            {
                return NotFound();
            }
            ViewData["EtudiantId"] = new SelectList(_context.Etudiant, "Id", "NomComplet", evaluation.EtudiantId);
            ViewData["MatiereId"] = new SelectList(_context.Matiere, "Id", "NameMatiere", evaluation.MatiereId);
            return View(evaluation);
        }

        // POST: Evaluations/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("Id,Type,Date,Note,MatiereId,EtudiantId")] Evaluation evaluation)
        {
            if (id != evaluation.Id)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    _context.Update(evaluation);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!EvaluationExists(evaluation.Id))
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
            ViewData["EtudiantId"] = new SelectList(_context.Etudiant, "Id", "Cin", evaluation.EtudiantId);
            ViewData["MatiereId"] = new SelectList(_context.Matiere, "Id", "NameMatiere", evaluation.MatiereId);
            return View(evaluation);
        }

        // GET: Evaluations/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var evaluation = await _context.Evaluation
                .Include(e => e.Etudiant)
                .Include(e => e.Matiere)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (evaluation == null)
            {
                return NotFound();
            }

            return View(evaluation);
        }

        // POST: Evaluations/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var evaluation = await _context.Evaluation.FindAsync(id);
            if (evaluation != null)
            {
                _context.Evaluation.Remove(evaluation);
            }

            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool EvaluationExists(int id)
        {
            return _context.Evaluation.Any(e => e.Id == id);
        }
    }
}
