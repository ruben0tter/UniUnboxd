// This file contains the definition of the NotificationModel class, which is 
// used for representing notification data transfer objects.

using Newtonsoft.Json;

namespace UniUnboxdAPI.Models.DataTransferObjects.Notifications
{
    // The NotificationModel class represents a notification payload.
    public class NotificationModel
    {
        // The DataPayload class represents the data payload of a notification.
        public class DataPayload
        {
            [JsonProperty("title")]
            public string? Title { get; set; } // The title of the notification.

            [JsonProperty("body")]
            public string? Body { get; set; } // The body of the notification.
        }

        [JsonProperty("notification")]
        public DataPayload? Notification { get; set; } // The notification payload.

        [JsonProperty("token")]
        public string? DeviceToken { get; set; } // The device token for sending the notification.
    }
}
