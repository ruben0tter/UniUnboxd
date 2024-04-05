using Microsoft.AspNetCore.Identity;
using UniUnboxdAPI.Models;

// User class represents a user in the system
// It inherits from IdentityUser<int> and implements IBase interface
public class User : IdentityUser<int>, IBase
{
    public DateTime CreationTime { get; set; } // The creation time of the user
    public DateTime LastModificationTime { get; set; } // The last modification time of the user
    public UserType UserType { get; set; } // The type of user (Student, University, Professor)
    public VerificationStatus VerificationStatus { get; set; } // The verification status of the user (Unverified, Pending, Verified)
    public int UniversityId { get; set; } // The ID of the university associated with the user
}

// UserType enum represents the type of user
public enum UserType
{
    Student, // Represents a student user
    University, // Represents a university user
    Professor // Represents a professor user
}

// VerificationStatus enum represents the verification status of a user
public enum VerificationStatus
{
    Unverified, // User is not verified
    Pending, // Verification is pending for the user
    Verified, // User is verified
}
