namespace UniUnboxdAPI.Models.DataTransferObjects
{
    // The VerificationModel class represents the data transfer object for verification.
    public class VerificationModel
    {
        // The VerificationData property represents the verification data.
        public string[]? VerificationData { get; set; }

        // The TargetUniversityId property represents the target university ID.
        public int TargetUniversityId { get; set; }
    }
}
