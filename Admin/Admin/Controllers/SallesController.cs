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
    public class SallesController : Controller
    {
        private readonly AppDbContext _context;

        public SallesController(AppDbContext context)
        {
            _context = context;
        }

        // GET: Salles
        public async Task<IActionResult> Index()
        {
            var appDbContext = _context.Salle.Include(s => s.Department);
            return View(await appDbContext.ToListAsync());
        }

        // GET: Salles/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var salle = await _context.Salle
                .Include(s => s.Department)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (salle == null)
            {
                return NotFound();
            }

            return View(salle);
        }

        // GET: Salles/Create
        public IActionResult Create()
        {
            ViewData["DepartmentId"] = new SelectList(_context.Department, "Id", "Name");
            return View();
        }

        // POST: Salles/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Id,CodeSalle,TypeSalle,Capacity,Projector,DepartmentId")] Salle salle)
        {
            if (ModelState.IsValid)
            {
                _context.Add(salle);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            ViewData["DepartmentId"] = new SelectList(_context.Department, "Id", "Id", salle.DepartmentId);
            return View(salle);
        }

        // GET: Salles/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var salle = await _context.Salle.FindAsync(id);
            if (salle == null)
            {
                return NotFound();
            }
            ViewData["DepartmentId"] = new SelectList(_context.Department, "Id", "Name", salle.DepartmentId);
            return View(salle);
        }

        // POST: Salles/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("Id,CodeSalle,TypeSalle,Capacity,Projector,DepartmentId")] Salle salle)
        {
            if (id != salle.Id)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    _context.Update(salle);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!SalleExists(salle.Id))
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
            ViewData["DepartmentId"] = new SelectList(_context.Department, "Id", "Id", salle.DepartmentId);
            return View(salle);
        }

        // GET: Salles/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var salle = await _context.Salle
                .Include(s => s.Department)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (salle == null)
            {
                return NotFound();
            }

            return View(salle);
        }

        // POST: Salles/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var salle = await _context.Salle.FindAsync(id);
            if (salle != null)
            {
                _context.Salle.Remove(salle);
            }

            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool SalleExists(int id)
        {
            return _context.Salle.Any(e => e.Id == id);
        }
    }
}
