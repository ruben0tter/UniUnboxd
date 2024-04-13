namespace UniUnboxdAPI.Models.DataTransferObjects;

public class StudentGridModel
{
    // Represents the StudentGridModel class used for transferring student data to the client.
    
    public required int Id { get; set; } // Represents the unique identifier of the student.
    
    public required string Name { get; set; } // Represents the name of the student.
    
    public string? Image { get; set; } // Represents the image URL of the student, nullable.
}