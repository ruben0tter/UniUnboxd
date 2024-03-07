using Microsoft.AspNetCore.Identity;

namespace UniUnboxdAPI.Models;

public class AcceptReject
{
    public required int UserId { get; set; }
    public required bool AcceptedOrRejected { get; set; }
}
