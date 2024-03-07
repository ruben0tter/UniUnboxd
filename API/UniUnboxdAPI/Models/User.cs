using Microsoft.AspNetCore.Identity;

namespace UniUnboxdAPI.Models;

public class User : IdentityUser<int>, IBase
{
    public DateTime CreationTime { get; set; }
    public DateTime LastModificationTime { get; set; }
    public UserType UserType { get; set; }
    public VerificationStatus VerificationStatus { get; set; }
}

public enum UserType
{
    Student,
    University
}

public enum VerificationStatus
{
    Unverified,
    Pending,
    Verified,
}
