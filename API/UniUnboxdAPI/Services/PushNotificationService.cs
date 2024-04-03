using FirebaseAdmin.Messaging;
using MimeKit;
using System.Net.Http.Headers;
using UniUnboxdAPI.Models;
using UniUnboxdAPI.Utilities;

namespace UniUnboxdAPI.Services
{
    public class PushNotificationService : INotificationService
    { 
        public void SendNewFollowerNotification(Student studentFollowing, Student studentFollowed)
        {
            var message = new Message()
            {
                Notification = new Notification
                {
                    Title = "You have a new follower!",
                    Body = NotificationBodyGenerator.NewFollowerNotificationBody(studentFollowing.UserName!)
                },
                Token = studentFollowed.DeviceToken
            };

            SendNotification(message);
        }

        public void SendNewReviewNotification(Student receiver, Review review)
        {
            var message = new Message()
            {
                Notification = new Notification
                {
                    Title = "Someone you follow has posted a review!",
                    Body = NotificationBodyGenerator.NewReviewNotificationBody(review.Student.UserName!, review.Course.Name)
                },
                Token = receiver.DeviceToken
            };

            SendNotification(message);
        }

        public void SendNewReplyNotification(Reply reply)
        {
            var message = new Message()
            {
                Notification = new Notification
                {
                    Title = "Someone has replied to your review!",
                    Body = NotificationBodyGenerator.NewReplyNotificationBody(reply.User.UserName!, reply.Review.Course.Name)
                },
                Token = reply.Review.Student.DeviceToken
            };

            SendNotification(message);
        }

        //TODO: Implement function
        public void SendVerificationStatusChangeNotification(Student student)
        {
            var message = new Message()
            {
                Notification = new Notification
                {
                    Title = "The status of your verification application has changed.",
                    Body = NotificationBodyGenerator.VerificationStatusChangeBody(student.VerificationStatus)
                },
                Token = student.DeviceToken
            };

            SendNotification(message);
        }

        private async Task SendNotification(Message message)
        {
            try
            {
                var messaging = FirebaseMessaging.DefaultInstance;
                await messaging.SendAsync(message);
            }
            catch { }
        }
    }
}
