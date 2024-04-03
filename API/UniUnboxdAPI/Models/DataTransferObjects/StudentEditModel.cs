namespace UniUnboxdAPI.Models.DataTransferObjects;

public class StudentEditModel
{
    public required int Id { get; set; }
    public required string Name { get; set; }
    public string? Image { get; set; }
    
    public required VerificationStatus VerificationStatus { get; set; }
    public required NotificationSettingsModel NotificationSettings { get; set; }
    
}