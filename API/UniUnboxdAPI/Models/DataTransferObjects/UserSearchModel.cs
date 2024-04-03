namespace UniUnboxdAPI.Models.DataTransferObjects
{
    public class UserSearchModel
    {
        public required int Id { get; set; }
        public required string UserName { get; set; }
        public string? Image { get; set; }
        public required UserType UserType { get; set; }
    }
}
