namespace UniUnboxdAPI.Models.DataTransferObjects;

public class ProfessorEditModel
{
    public required int Id { get; set; }
    public required string Name { get; set; }
    public string? Image { get; set; }
}