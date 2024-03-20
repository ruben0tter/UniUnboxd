using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace UniUnboxdAPI.Migrations
{
    /// <inheritdoc />
    public partial class AlterUserUniversityRelation : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Professors_Universities_UniversityId",
                table: "Professors");

            migrationBuilder.DropForeignKey(
                name: "FK_Students_Universities_UniversityId",
                table: "Students");

            migrationBuilder.DropForeignKey(
                name: "FK_Universities_Universities_UniversityId",
                table: "Universities");

            migrationBuilder.DropIndex(
                name: "IX_Universities_UniversityId",
                table: "Universities");

            migrationBuilder.DropIndex(
                name: "IX_Students_UniversityId",
                table: "Students");

            migrationBuilder.DropIndex(
                name: "IX_Professors_UniversityId",
                table: "Professors");

            migrationBuilder.DropColumn(
                name: "UniversityId",
                table: "Universities");

            migrationBuilder.DropColumn(
                name: "UniversityId",
                table: "Students");

            migrationBuilder.DropColumn(
                name: "UniversityId",
                table: "Professors");

            migrationBuilder.AddColumn<int>(
                name: "UniversityId",
                table: "Users",
                type: "int",
                nullable: false,
                defaultValue: 0);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "UniversityId",
                table: "Users");

            migrationBuilder.AddColumn<int>(
                name: "UniversityId",
                table: "Universities",
                type: "int",
                nullable: true);

            migrationBuilder.AddColumn<int>(
                name: "UniversityId",
                table: "Students",
                type: "int",
                nullable: true);

            migrationBuilder.AddColumn<int>(
                name: "UniversityId",
                table: "Professors",
                type: "int",
                nullable: true);

            migrationBuilder.CreateIndex(
                name: "IX_Universities_UniversityId",
                table: "Universities",
                column: "UniversityId");

            migrationBuilder.CreateIndex(
                name: "IX_Students_UniversityId",
                table: "Students",
                column: "UniversityId");

            migrationBuilder.CreateIndex(
                name: "IX_Professors_UniversityId",
                table: "Professors",
                column: "UniversityId");

            migrationBuilder.AddForeignKey(
                name: "FK_Professors_Universities_UniversityId",
                table: "Professors",
                column: "UniversityId",
                principalTable: "Universities",
                principalColumn: "Id");

            migrationBuilder.AddForeignKey(
                name: "FK_Students_Universities_UniversityId",
                table: "Students",
                column: "UniversityId",
                principalTable: "Universities",
                principalColumn: "Id");

            migrationBuilder.AddForeignKey(
                name: "FK_Universities_Universities_UniversityId",
                table: "Universities",
                column: "UniversityId",
                principalTable: "Universities",
                principalColumn: "Id");
        }
    }
}
