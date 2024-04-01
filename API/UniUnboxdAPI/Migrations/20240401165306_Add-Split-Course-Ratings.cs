using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace UniUnboxdAPI.Migrations
{
    /// <inheritdoc />
    public partial class AddSplitCourseRatings : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.RenameColumn(
                name: "AverageRating",
                table: "Courses",
                newName: "NonanonymousRating");

            migrationBuilder.AddColumn<double>(
                name: "AnonymousRating",
                table: "Courses",
                type: "double",
                nullable: false,
                defaultValue: 0.0);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "AnonymousRating",
                table: "Courses");

            migrationBuilder.RenameColumn(
                name: "NonanonymousRating",
                table: "Courses",
                newName: "AverageRating");
        }
    }
}
