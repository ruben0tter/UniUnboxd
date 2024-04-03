using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace UniUnboxdAPI.Migrations
{
    /// <inheritdoc />
    public partial class AddCourseProfessorAssignment : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "CourseProfessor");

            migrationBuilder.CreateTable(
                name: "CourseProfessorAssignment",
                columns: table => new
                {
                    ProfessorId = table.Column<int>(type: "int", nullable: false),
                    CourseId = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_CourseProfessorAssignment", x => new { x.ProfessorId, x.CourseId });
                    table.ForeignKey(
                        name: "FK_CourseProfessorAssignment_Courses_ProfessorId",
                        column: x => x.ProfessorId,
                        principalTable: "Courses",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_CourseProfessorAssignment_Professors_CourseId",
                        column: x => x.CourseId,
                        principalTable: "Professors",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                })
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.CreateIndex(
                name: "IX_CourseProfessorAssignment_CourseId",
                table: "CourseProfessorAssignment",
                column: "CourseId");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "CourseProfessorAssignment");

            migrationBuilder.CreateTable(
                name: "CourseProfessor",
                columns: table => new
                {
                    AssignedCoursesId = table.Column<int>(type: "int", nullable: false),
                    AssignedProfessorsId = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_CourseProfessor", x => new { x.AssignedCoursesId, x.AssignedProfessorsId });
                    table.ForeignKey(
                        name: "FK_CourseProfessor_Courses_AssignedCoursesId",
                        column: x => x.AssignedCoursesId,
                        principalTable: "Courses",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_CourseProfessor_Professors_AssignedProfessorsId",
                        column: x => x.AssignedProfessorsId,
                        principalTable: "Professors",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                })
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.CreateIndex(
                name: "IX_CourseProfessor_AssignedProfessorsId",
                table: "CourseProfessor",
                column: "AssignedProfessorsId");
        }
    }
}
