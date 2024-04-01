using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace UniUnboxdAPI.Migrations
{
    /// <inheritdoc />
    public partial class UserID : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Applications_Users_UserToBeVerifiedId",
                table: "Applications");

            migrationBuilder.DropIndex(
                name: "IX_Applications_UserToBeVerifiedId",
                table: "Applications");

            migrationBuilder.RenameColumn(
                name: "UserToBeVerifiedId",
                table: "Applications",
                newName: "UserId");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.RenameColumn(
                name: "UserId",
                table: "Applications",
                newName: "UserToBeVerifiedId");

            migrationBuilder.CreateIndex(
                name: "IX_Applications_UserToBeVerifiedId",
                table: "Applications",
                column: "UserToBeVerifiedId");

            migrationBuilder.AddForeignKey(
                name: "FK_Applications_Users_UserToBeVerifiedId",
                table: "Applications",
                column: "UserToBeVerifiedId",
                principalTable: "Users",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);
        }
    }
}
