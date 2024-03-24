using Newtonsoft.Json;

namespace UniUnboxdAPI.Models.DataTransferObjects.Notifications
{
    public class NotificationModel
    {
        public class DataPayload
        {
            [JsonProperty("title")]
            public string? Title { get; set; }
            [JsonProperty("body")]
            public string? Body { get; set; }
        }
        [JsonProperty("notification")]
        public DataPayload? Notification { get; set; }
        [JsonProperty("token")]
        public string? DeviceToken { get; set; }
    }
}
