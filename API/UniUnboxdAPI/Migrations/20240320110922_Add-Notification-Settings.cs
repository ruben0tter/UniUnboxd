using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace UniUnboxdAPI.Migrations
{
    /// <inheritdoc />
    public partial class AddNotificationSettings : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "NotificationSettings",
                columns: table => new
                {
                    StudentId = table.Column<int>(type: "int", nullable: false),
                    ReceivesVerificationStatusChangeMail = table.Column<bool>(type: "tinyint(1)", nullable: false),
                    ReceivesVerificationStatusChangePush = table.Column<bool>(type: "tinyint(1)", nullable: false),
                    ReceivesFollowersReviewMail = table.Column<bool>(type: "tinyint(1)", nullable: false),
                    ReceivesFollowersReviewPush = table.Column<bool>(type: "tinyint(1)", nullable: false),
                    ReceivesNewReplyMail = table.Column<bool>(type: "tinyint(1)", nullable: false),
                    ReceivesNewReplyPush = table.Column<bool>(type: "tinyint(1)", nullable: false),
                    ReceivesNewFollowerMail = table.Column<bool>(type: "tinyint(1)", nullable: false),
                    ReceivesNewFollowerPush = table.Column<bool>(type: "tinyint(1)", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_NotificationSettings", x => x.StudentId);
                    table.ForeignKey(
                        name: "FK_NotificationSettings_Students_StudentId",
                        column: x => x.StudentId,
                        principalTable: "Students",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                })
                .Annotation("MySql:CharSet", "utf8mb4");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "NotificationSettings");
        }
    }
}
