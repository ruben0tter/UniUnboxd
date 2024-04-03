namespace UniUnboxdAPI.Models.DataTransferObjects;

public class AcceptRejectModel
{
    public required int UserId { get; set; }
    public required bool AcceptedOrRejected { get; set; }
}
