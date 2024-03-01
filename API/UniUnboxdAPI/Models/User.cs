using Microsoft.AspNetCore.Identity;

namespace UniUnboxdAPI.Models;

public class User : IdentityUser<int>, IBase
{
    public DateTime CreationTime { get; set; }
    public DateTime LastModificationTime { get; set; }
}