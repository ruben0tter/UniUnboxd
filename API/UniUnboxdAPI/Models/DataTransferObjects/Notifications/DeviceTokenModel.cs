// This file contains the definition of the DeviceTokenModel class, which is used 
// for storing device tokens for notifications.

namespace UniUnboxdAPI.Models.DataTransferObjects.Notifications
{
    // DeviceTokenModel represents a device token used for notifications.
    public class DeviceTokenModel
    {
        // The device token.
        public required string DeviceToken { get; set; }
    }
}
