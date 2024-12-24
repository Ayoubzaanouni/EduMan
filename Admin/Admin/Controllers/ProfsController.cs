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
    public class ProfsController : Controller
    {
        private readonly AppDbContext _context;
        private readonly IWebHostEnvironment _hostingEnvironment;

        public ProfsController(AppDbContext context, IWebHostEnvironment hostingEnvironment)
        {
            _context = context;
            _hostingEnvironment = hostingEnvironment;
        }

        // GET: Profs
        public async Task<IActionResult> Index()
        {
            var appDbContext = _context.Prof.Include(p => p.Department);
            return View(await appDbContext.ToListAsync());
        }

        // GET: Profs/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var prof = await _context.Prof
                .Include(p => p.Department)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (prof == null)
            {
                return NotFound();
            }

            return View(prof);
        }

        // GET: Profs/Create
        public IActionResult Create()
        {
            ViewData["DepartmentId"] = new SelectList(_context.Department, "Id", "Name");
            return View();
        }

        // POST: Profs/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Id,Nom,Prenom,Cin,Email,Password,ConfirmPassword,Grade,DepartmentId,Image")] Prof prof)
        {
            if (ModelState.IsValid)
            {
                if (prof.Image != null)
                {
                    string folder = "files/assignments/";
                    folder += Guid.NewGuid().ToString() + "_" + prof.Image.FileName;
                    prof.ImageName = folder;
                    string serverFolder = Path.Combine(_hostingEnvironment.WebRootPath, folder);
                    await prof.Image.CopyToAsync(new FileStream(serverFolder, FileMode.Create));
                }
                prof.Password = BCrypt.Net.BCrypt.HashPassword(prof.Password);
                prof.ConfirmPassword = BCrypt.Net.BCrypt.HashPassword(prof.ConfirmPassword);
                _context.Add(prof);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            ViewData["DepartmentId"] = new SelectList(_context.Department, "Id", "Id", prof.DepartmentId);
            return View(prof);
        }

        // GET: Profs/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var prof = await _context.Prof.FindAsync(id);
            if (prof == null)
            {
                return NotFound();
            }
            ViewData["DepartmentId"] = new SelectList(_context.Department, "Id", "Name", prof.DepartmentId);
            return View(prof);
        }

        // POST: Profs/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("Id,Nom,Prenom,Cin,Email,Password,ConfirmPassword,Grade,DepartmentId")] Prof prof)
        {
            if (id != prof.Id)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    _context.Update(prof);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!ProfExists(prof.Id))
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
            ViewData["DepartmentId"] = new SelectList(_context.Department, "Id", "Id", prof.DepartmentId);
            return View(prof);
        }

        // GET: Profs/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var prof = await _context.Prof
                .Include(p => p.Department)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (prof == null)
            {
                return NotFound();
            }

            return View(prof);
        }

        // POST: Profs/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var prof = await _context.Prof.FindAsync(id);
            if (prof != null)
            {
                _context.Prof.Remove(prof);
            }

            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool ProfExists(int id)
        {
            return _context.Prof.Any(e => e.Id == id);
        }
    }
}
