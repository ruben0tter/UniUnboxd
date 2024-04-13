// This file contains the definition of the IBase interface.
// The IBase interface represents the base contract for all entities in the UniUnboxdAPI.Models namespace.
// It defines properties for Id, CreationTime, and LastModificationTime.

namespace UniUnboxdAPI.Models;

public interface IBase
{
    public int Id { get; set; } // Represents the unique identifier of the entity.
    public DateTime CreationTime { get; set; } // Represents the date and time when the entity was created.
    public DateTime LastModificationTime { get; set; } // Represents the date and time when the entity was last modified.
}