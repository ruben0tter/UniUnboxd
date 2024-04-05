namespace UniUnboxdAPI.Models.DataTransferObjects
{
    // AuthenticationModel class for handling authentication data transfer
    public class AuthenticationModel
    {
        // Email property for storing the user's email address
        public required string Email { get; set; }

        // Password property for storing the user's password
        public required string Password { get; set; }
    }
}
