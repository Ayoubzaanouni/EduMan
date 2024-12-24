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
    public class DepotsController : Controller
    {
        private readonly AppDbContext _context;
        private readonly IWebHostEnvironment _hostingEnvironment;


        public DepotsController(AppDbContext context, IWebHostEnvironment hostingEnvironment)
        {
            _context = context;
            _hostingEnvironment = hostingEnvironment;
        }

        // GET: Depots
        public async Task<IActionResult> Index()
        {
            var appDbContext = _context.Depot.Include(d => d.Assignment).Include(d => d.Etudiant);
            return View(await appDbContext.ToListAsync());
        }

        // GET: Depots/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var depot = await _context.Depot
                .Include(d => d.Assignment)
                .Include(d => d.Etudiant)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (depot == null)
            {
                return NotFound();
            }

            return View(depot);
        }

        // GET: Depots/Create
        public IActionResult Create()
        {
            ViewData["AssignmentId"] = new SelectList(_context.Set<Assignment>(), "Id", "Title");
            ViewData["EtudiantId"] = new SelectList(_context.Etudiant, "Id", "Cin");
            return View();
        }

        // POST: Depots/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Id,TimeStamp,FileName,File,AssignmentId,EtudiantId")] Depot depot)
        {
            if (ModelState.IsValid)
            {
                if (depot.File != null)
                {
                    string folder = "files/depots/";
                    folder += Guid.NewGuid().ToString() + "_" + depot.File.FileName;
                    depot.FileName = folder;
                    string serverFolder = Path.Combine(_hostingEnvironment.WebRootPath, folder);
                    await depot.File.CopyToAsync(new FileStream(serverFolder, FileMode.Create));
                }
                depot.TimeStamp = DateTime.Now;
                _context.Add(depot);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            ViewData["AssignmentId"] = new SelectList(_context.Set<Assignment>(), "Id", "Id", depot.AssignmentId);
            ViewData["EtudiantId"] = new SelectList(_context.Etudiant, "Id", "Cin", depot.EtudiantId);
            return View(depot);
        }

        // GET: Depots/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var depot = await _context.Depot.FindAsync(id);
            if (depot == null)
            {
                return NotFound();
            }
            ViewData["AssignmentId"] = new SelectList(_context.Set<Assignment>(), "Id", "Title", depot.AssignmentId);
            ViewData["EtudiantId"] = new SelectList(_context.Etudiant, "Id", "Cin", depot.EtudiantId);
            return View(depot);
        }

        // POST: Depots/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("Id,TimeStamp,FileName,AssignmentId,EtudiantId")] Depot depot)
        {
            if (id != depot.Id)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    _context.Update(depot);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!DepotExists(depot.Id))
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
            ViewData["AssignmentId"] = new SelectList(_context.Set<Assignment>(), "Id", "Id", depot.AssignmentId);
            ViewData["EtudiantId"] = new SelectList(_context.Etudiant, "Id", "Cin", depot.EtudiantId);
            return View(depot);
        }

        // GET: Depots/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var depot = await _context.Depot
                .Include(d => d.Assignment)
                .Include(d => d.Etudiant)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (depot == null)
            {
                return NotFound();
            }

            return View(depot);
        }

        // POST: Depots/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var depot = await _context.Depot.FindAsync(id);
            if (depot != null)
            {
                _context.Depot.Remove(depot);
            }

            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool DepotExists(int id)
        {
            return _context.Depot.Any(e => e.Id == id);
        }
    }
}
