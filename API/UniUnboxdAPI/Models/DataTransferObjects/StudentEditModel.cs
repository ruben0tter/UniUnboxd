namespace UniUnboxdAPI.Models.DataTransferObjects;

public class StudentEditModel
{
    public required int Id { get; set; }
    public required string Name { get; set; }
    public required string Image { get; set; }
    
    //TODO: add notification settings
    //TODO: add verification application
}