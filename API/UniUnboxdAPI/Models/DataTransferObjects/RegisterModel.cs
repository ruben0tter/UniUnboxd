using System.Text.Json.Serialization;

namespace UniUnboxdAPI.Models.DataTransferObjects
{
    // RegisterModel class represents the data transfer object for user registration.
    public class RegisterModel
    {
        // Email property represents the email address of the user.
        public required string Email { get; set; }

        // Password property represents the password of the user.
        public required string Password { get; set; }

        // Type property represents the type of user (e.g., student, professor).
        [JsonConverter(typeof(JsonStringEnumConverter))] 
        public required UserType Type { get; set; }
    }
}
