using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace UniUnboxdAPI.Migrations
{
    /// <inheritdoc />
    public partial class AddUniversitytoUser : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<int>(
                name: "UniversityId",
                table: "Universities",
                type: "int",
                nullable: true);

            migrationBuilder.UpdateData(
                table: "Reviews",
                keyColumn: "Comment",
                keyValue: null,
                column: "Comment",
                value: "");

            migrationBuilder.AlterColumn<string>(
                name: "Comment",
                table: "Reviews",
                type: "longtext",
                nullable: false,
                oldClrType: typeof(string),
                oldType: "longtext",
                oldNullable: true)
                .Annotation("MySql:CharSet", "utf8mb4")
                .OldAnnotation("MySql:CharSet", "utf8mb4");

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
                name: "FK_Universities_Universities_UniversityId",
                table: "Universities",
                column: "UniversityId",
                principalTable: "Universities",
                principalColumn: "Id");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Professors_Universities_UniversityId",
                table: "Professors");

            migrationBuilder.DropForeignKey(
                name: "FK_Universities_Universities_UniversityId",
                table: "Universities");

            migrationBuilder.DropIndex(
                name: "IX_Universities_UniversityId",
                table: "Universities");

            migrationBuilder.DropIndex(
                name: "IX_Professors_UniversityId",
                table: "Professors");

            migrationBuilder.DropColumn(
                name: "UniversityId",
                table: "Universities");

            migrationBuilder.DropColumn(
                name: "UniversityId",
                table: "Professors");

            migrationBuilder.AlterColumn<string>(
                name: "Comment",
                table: "Reviews",
                type: "longtext",
                nullable: true,
                oldClrType: typeof(string),
                oldType: "longtext")
                .Annotation("MySql:CharSet", "utf8mb4")
                .OldAnnotation("MySql:CharSet", "utf8mb4");
        }
    }
}
