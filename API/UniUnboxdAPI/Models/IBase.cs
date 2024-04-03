namespace UniUnboxdAPI.Models;

public interface IBase
{
    public int Id { get; set; }
    public DateTime CreationTime { get; set; }
    public DateTime LastModificationTime { get; set; }
}