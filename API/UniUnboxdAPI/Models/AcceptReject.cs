using Microsoft.AspNetCore.Identity;

namespace UniUnboxdAPI.Models;

public class AcceptReject : Base
{
    public required int UserId { get; set; }
    public required bool AcceptedOrRejected { get; set; }
}
