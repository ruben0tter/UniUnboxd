// This file contains the definition of the StudentEditModel class.
// The StudentEditModel class represents the data transfer object for editing a student's information.

namespace UniUnboxdAPI.Models.DataTransferObjects;

public class StudentEditModel
{
    public required int Id { get; set; } // The ID of the student.
    public required string Name { get; set; } // The name of the student.
    public string? Image { get; set; } // The image of the student (nullable).
    
    public required VerificationStatus VerificationStatus { get; set; } // The verification status of the student.
    public required NotificationSettingsModel NotificationSettings { get; set; } // The notification settings of the student.
    
}