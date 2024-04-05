namespace UniUnboxdAPI.Models.DataTransferObjects
{
    // Represents the data transfer object for pending verifications.
    public class PendingVerificationsModel
    {
        // Gets or sets the verification data.
        public string[]? VerificationData { get; set; }

        // Gets or sets the user ID.
        public int UserId { get; set; }

        // Gets or sets the email.
        public string Email { get; set; }

        // Gets or sets the image.
        public string Image { get; set; }
    }
}
