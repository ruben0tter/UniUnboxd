// This file contains the definition of the VerificationApplication class.
// The VerificationApplication class inherits from the Base class.
// It represents a verification application with properties for verification data, user ID, and target university.

namespace UniUnboxdAPI.Models;
public class VerificationApplication : Base {
    public required string[] VerificationData { get; set; } // Array of verification data.
    public required int UserId { get; set; } // User ID associated with the verification application.
    public University? TargetUniversity { get; set; } // Target university for the verification application.
}
