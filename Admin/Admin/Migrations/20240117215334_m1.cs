using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Admin.Migrations
{
    /// <inheritdoc />
    public partial class m1 : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<string>(
                name: "Classe",
                table: "DemandeRatt",
                type: "nvarchar(max)",
                nullable: true);

            migrationBuilder.AddColumn<string>(
                name: "HeureDebut",
                table: "DemandeRatt",
                type: "nvarchar(max)",
                nullable: true);

            migrationBuilder.AddColumn<string>(
                name: "HeureFin",
                table: "DemandeRatt",
                type: "nvarchar(max)",
                nullable: true);

            migrationBuilder.AddColumn<string>(
                name: "Matiere",
                table: "DemandeRatt",
                type: "nvarchar(max)",
                nullable: true);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "Classe",
                table: "DemandeRatt");

            migrationBuilder.DropColumn(
                name: "HeureDebut",
                table: "DemandeRatt");

            migrationBuilder.DropColumn(
                name: "HeureFin",
                table: "DemandeRatt");

            migrationBuilder.DropColumn(
                name: "Matiere",
                table: "DemandeRatt");
        }
    }
}
