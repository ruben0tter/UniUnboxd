using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace UniUnboxdAPI.Migrations
{
    /// <inheritdoc />
    public partial class SwapCourseAndProfessorInAssignmentTable : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_CourseProfessorAssignments_Courses_ProfessorId",
                table: "CourseProfessorAssignments");

            migrationBuilder.DropForeignKey(
                name: "FK_CourseProfessorAssignments_Professors_CourseId",
                table: "CourseProfessorAssignments");

            migrationBuilder.AddForeignKey(
                name: "FK_CourseProfessorAssignments_Courses_CourseId",
                table: "CourseProfessorAssignments",
                column: "CourseId",
                principalTable: "Courses",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);

            migrationBuilder.AddForeignKey(
                name: "FK_CourseProfessorAssignments_Professors_ProfessorId",
                table: "CourseProfessorAssignments",
                column: "ProfessorId",
                principalTable: "Professors",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_CourseProfessorAssignments_Courses_CourseId",
                table: "CourseProfessorAssignments");

            migrationBuilder.DropForeignKey(
                name: "FK_CourseProfessorAssignments_Professors_ProfessorId",
                table: "CourseProfessorAssignments");

            migrationBuilder.AddForeignKey(
                name: "FK_CourseProfessorAssignments_Courses_ProfessorId",
                table: "CourseProfessorAssignments",
                column: "ProfessorId",
                principalTable: "Courses",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);

            migrationBuilder.AddForeignKey(
                name: "FK_CourseProfessorAssignments_Professors_CourseId",
                table: "CourseProfessorAssignments",
                column: "CourseId",
                principalTable: "Professors",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);
        }
    }
}
