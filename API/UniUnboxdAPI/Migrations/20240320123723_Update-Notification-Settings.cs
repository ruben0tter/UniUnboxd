using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace UniUnboxdAPI.Migrations
{
    /// <inheritdoc />
    public partial class UpdateNotificationSettings : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "ReceivesVerificationStatusChangeMail",
                table: "NotificationSettings");

            migrationBuilder.DropColumn(
                name: "ReceivesVerificationStatusChangePush",
                table: "NotificationSettings");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<bool>(
                name: "ReceivesVerificationStatusChangeMail",
                table: "NotificationSettings",
                type: "tinyint(1)",
                nullable: false,
                defaultValue: false);

            migrationBuilder.AddColumn<bool>(
                name: "ReceivesVerificationStatusChangePush",
                table: "NotificationSettings",
                type: "tinyint(1)",
                nullable: false,
                defaultValue: false);
        }
    }
}
