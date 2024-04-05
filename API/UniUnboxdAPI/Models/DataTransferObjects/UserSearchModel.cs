namespace UniUnboxdAPI.Models.DataTransferObjects
{
    // UserSearchModel represents a data transfer object for searching users.
    public class UserSearchModel
    {
        // Id represents the unique identifier of the user.
        public required int Id { get; set; }

        // UserName represents the username of the user.
        public required string UserName { get; set; }

        // Image represents the image URL of the user. It can be null.
        public string? Image { get; set; }

        // UserType represents the type of the user.
        public required UserType UserType { get; set; }
    }
}
