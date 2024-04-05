// This file contains the definition of the UniversityNameModel class.
// The UniversityNameModel class represents a data transfer object for university names.

namespace UniUnboxdAPI.Models.DataTransferObjects
{
    // The UniversityNameModel class represents a university name.
    public class UniversityNameModel
    {
        // The Id property represents the unique identifier of the university.
        public required int Id { get; set; }

        // The Name property represents the name of the university.
        public required String Name { get; set; }
    }
}