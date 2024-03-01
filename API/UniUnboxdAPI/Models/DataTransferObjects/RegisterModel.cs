using System.Text.Json.Serialization;

namespace UniUnboxdAPI.Models.DataTransferObjects
{
    public class RegisterModel
    {
        public required string Email { get; set; }

        public required string Password { get; set; }

        [JsonConverter(typeof(JsonStringEnumConverter))] 
        public required UserType Type { get; set; }
    }
}
