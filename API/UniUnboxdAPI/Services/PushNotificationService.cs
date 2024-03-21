using FirebaseAdmin.Messaging;
using System.Net.Http.Headers;
using UniUnboxdAPI.Models;
using UniUnboxdAPI.Utilities;

namespace UniUnboxdAPI.Services
{
    public class PushNotificationService : INotificationService
    { 
        public void SendNewFollowerNotification(Student studentFollowing, Student studentFollowed)
        {
            try
            {
                var message = new Message()
                {
                    Notification = new Notification
                    {
                        Title = "You have a new follower!",
                        Body = NotificationBodyGenerator.NewFollowerNotificationBody(studentFollowing)
                    },
                    Token = studentFollowed.DeviceToken
                };

                SendNotification(message);
            } 
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
            }
        }

        public void SendNewReviewNotification(Student receiver, Review review)
        {
            try
            {
                var message = new Message()
                {
                    Notification = new Notification
                    {
                        Title = "Someone you follow has posted a review!",
                        Body = NotificationBodyGenerator.NewReviewNotificationBody(review)
                    },
                    Token = receiver.DeviceToken
                };

                SendNotification(message);
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
            }
        }

        public void SendNewReplyNotification(Reply reply)
        {
            try
            {
                var message = new Message()
                {
                    Notification = new Notification
                    {
                        Title = "Someone has replied to your review!",
                        Body = NotificationBodyGenerator.NewReplyNotificationBody(reply)
                    },
                    Token = reply.Review.Student.DeviceToken
                };

                SendNotification(message);
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
            }
        }

        private async Task SendNotification(Message message)
        {
            var messaging = FirebaseMessaging.DefaultInstance;
            await messaging.SendAsync(message);
        }
    }
}
