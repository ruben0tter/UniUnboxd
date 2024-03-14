namespace UniUnboxdAPI.Models.DataTransferObjects
{
    public abstract class VerificationModel
    {
        public string[] VerificationData { get; set; }

        public int TargetUniversityId { get; set; }
    }
}
