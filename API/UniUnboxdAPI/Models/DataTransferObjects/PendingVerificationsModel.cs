namespace UniUnboxdAPI.Models.DataTransferObjects
{
    public class PendingVerificationsModel
    {
        public string[]? VerificationData { get; set; }

        public int UserId { get; set; }
        public string Email { get; set; }
        public string Image { get; set; }
    }
}
