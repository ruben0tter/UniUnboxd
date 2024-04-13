// This file contains the definition of the ProfessorEditModel class.
// This class is used as a data transfer object for editing professor profiles.

namespace UniUnboxdAPI.Models.DataTransferObjects;

public class ProfessorEditModel
{
    // The unique identifier of the professor.
    public required int Id { get; set; }

    // The name of the professor.
    public required string Name { get; set; }

    // The image URL of the professor (nullable).
    public string? Image { get; set; }
}