using System.ComponentModel.DataAnnotations;

namespace UniUnboxdAPI.Models.DataTransferObjects
{
    public abstract class VerificationModel
    {
        public required string[] VerificationData { get; set; }

        public University? TargetUniversity { get; set; }
    }
}
