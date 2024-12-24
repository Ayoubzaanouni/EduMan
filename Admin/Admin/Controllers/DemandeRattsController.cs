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
    public class DemandeRattsController : Controller
    {
        private readonly AppDbContext _context;

        public DemandeRattsController(AppDbContext context)
        {
            _context = context;
        }

        // GET: DemandeRatts
        public async Task<IActionResult> Index()
        {
            var appDbContext = _context.DemandeRatt.Include(d => d.Prof);
            return View(await appDbContext.ToListAsync());
        }

        // GET: DemandeRatts/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var demandeRatt = await _context.DemandeRatt
                .Include(d => d.Prof)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (demandeRatt == null)
            {
                return NotFound();
            }

            return View(demandeRatt);
        }

        // GET: DemandeRatts/Create
        public IActionResult Create()
        {
            ViewData["ProfId"] = new SelectList(_context.Prof, "Id", "NomComplet");
            ViewData["MatiereId"] = new SelectList(_context.Matiere, "NameMatiere", "NameMatiere");
            ViewData["ClasseId"] = new SelectList(_context.Classe, "CodeClasse", "CodeClasse");
            return View();
        }

        // POST: DemandeRatts/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Id,Type,DateRatt,HeureDebut,HeureFin,Matiere,Classe,ProfId")] DemandeRatt demandeRatt)
        {
            if (ModelState.IsValid)
            {
                _context.Add(demandeRatt);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            ViewData["ProfId"] = new SelectList(_context.Prof, "Id", "Cin", demandeRatt.ProfId);
            return View(demandeRatt);
        }

        // GET: DemandeRatts/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var demandeRatt = await _context.DemandeRatt.FindAsync(id);
            if (demandeRatt == null)
            {
                return NotFound();
            }
            ViewData["ProfId"] = new SelectList(_context.Prof, "Id", "NomComplet", demandeRatt.ProfId);
            ViewData["ClasseId"] = new SelectList(_context.Classe, "CodeClasse", "CodeClasse", demandeRatt.Classe);
            ViewData["MatiereId"] = new SelectList(_context.Matiere, "NameMatiere", "NameMatiere", demandeRatt.Matiere);
            return View(demandeRatt);
        }

        // POST: DemandeRatts/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("Id,Type,DateRatt,HeureDebut,HeureFin,Matiere,Classe,ProfId")] DemandeRatt demandeRatt)
        {
            if (id != demandeRatt.Id)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    _context.Update(demandeRatt);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!DemandeRattExists(demandeRatt.Id))
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
            ViewData["ProfId"] = new SelectList(_context.Prof, "Id", "Cin", demandeRatt.ProfId);
            return View(demandeRatt);
        }

        // GET: DemandeRatts/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var demandeRatt = await _context.DemandeRatt
                .Include(d => d.Prof)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (demandeRatt == null)
            {
                return NotFound();
            }

            return View(demandeRatt);
        }

        // POST: DemandeRatts/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var demandeRatt = await _context.DemandeRatt.FindAsync(id);
            if (demandeRatt != null)
            {
                _context.DemandeRatt.Remove(demandeRatt);
            }

            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool DemandeRattExists(int id)
        {
            return _context.DemandeRatt.Any(e => e.Id == id);
        }
    }
}
