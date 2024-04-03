using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace UniUnboxdAPI.Migrations
{
    /// <inheritdoc />
    public partial class ChangeLinkingTableName : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_CourseProfessorAssignment_Courses_ProfessorId",
                table: "CourseProfessorAssignment");

            migrationBuilder.DropForeignKey(
                name: "FK_CourseProfessorAssignment_Professors_CourseId",
                table: "CourseProfessorAssignment");

            migrationBuilder.DropPrimaryKey(
                name: "PK_CourseProfessorAssignment",
                table: "CourseProfessorAssignment");

            migrationBuilder.RenameTable(
                name: "CourseProfessorAssignment",
                newName: "CourseProfessorAssignments");

            migrationBuilder.RenameIndex(
                name: "IX_CourseProfessorAssignment_CourseId",
                table: "CourseProfessorAssignments",
                newName: "IX_CourseProfessorAssignments_CourseId");

            migrationBuilder.AddPrimaryKey(
                name: "PK_CourseProfessorAssignments",
                table: "CourseProfessorAssignments",
                columns: new[] { "ProfessorId", "CourseId" });

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

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_CourseProfessorAssignments_Courses_ProfessorId",
                table: "CourseProfessorAssignments");

            migrationBuilder.DropForeignKey(
                name: "FK_CourseProfessorAssignments_Professors_CourseId",
                table: "CourseProfessorAssignments");

            migrationBuilder.DropPrimaryKey(
                name: "PK_CourseProfessorAssignments",
                table: "CourseProfessorAssignments");

            migrationBuilder.RenameTable(
                name: "CourseProfessorAssignments",
                newName: "CourseProfessorAssignment");

            migrationBuilder.RenameIndex(
                name: "IX_CourseProfessorAssignments_CourseId",
                table: "CourseProfessorAssignment",
                newName: "IX_CourseProfessorAssignment_CourseId");

            migrationBuilder.AddPrimaryKey(
                name: "PK_CourseProfessorAssignment",
                table: "CourseProfessorAssignment",
                columns: new[] { "ProfessorId", "CourseId" });

            migrationBuilder.AddForeignKey(
                name: "FK_CourseProfessorAssignment_Courses_ProfessorId",
                table: "CourseProfessorAssignment",
                column: "ProfessorId",
                principalTable: "Courses",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);

            migrationBuilder.AddForeignKey(
                name: "FK_CourseProfessorAssignment_Professors_CourseId",
                table: "CourseProfessorAssignment",
                column: "CourseId",
                principalTable: "Professors",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);
        }
    }
}
