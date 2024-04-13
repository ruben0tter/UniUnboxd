// This file contains the Base class which is an abstract class implementing the IBase interface.
// It represents the base model for all entities in the UniUnboxdAPI.
// The class has properties for Id, CreationTime, and LastModificat

namespace UniUnboxdAPI.Models;

public abstract class Base : IBase
{
    public int Id { get; set; } // Represents the unique identifier for the entity.
    public DateTime CreationTime { get; set; } // Represents the date and time when the entity was created.
    public DateTime LastModificationTime { get; set; } // Represents the date and time when the entity was last modified.
}
