// Namespace declaration for the AcceptRejectModel class
namespace UniUnboxdAPI.Models.DataTransferObjects;

// Definition of the AcceptRejectModel class
public class AcceptRejectModel
{
    // Property representing the user ID
    public required int UserId { get; set; }

    // Property representing whether the request is accepted or rejected
    public required bool AcceptedOrRejected { get; set; }
}
