namespace UniUnboxdAPI.Models;

public abstract class Base : IBase
{
    public int Id { get; set; }
    public DateTime CreationTime { get; set; }
    public DateTime LastModificationTime { get; set; }
}
