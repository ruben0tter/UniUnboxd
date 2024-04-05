using FirebaseAdmin.Messaging;
using UniUnboxdAPI.Models;
using UniUnboxdAPI.Utilities;

namespace UniUnboxdAPI.Services
{
    public class PushNotificationService : INotificationService
    {
        /// <summary>
        /// Sends a notification to a student that they have a new follower.
        /// </summary>
        /// <param name="studentFollowing">The student who started following.</param>
        /// <param name="studentFollowed">The student who was followed.</param>
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

        /// <summary>
        /// Sends a notification about a new review from someone the student follows.
        /// </summary>
        /// <param name="receiver">The student receiving the notification.</param>
        /// <param name="review">The review that was posted.</param>
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

        /// <summary>
        /// Sends a notification to the author of a review that someone has replied.
        /// </summary>
        /// <param name="reply">The reply that was made.</param>
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

        /// <summary>
        /// Sends a notification to a student about a change in their verification status.
        /// </summary>
        /// <param name="student">The student whose verification status has changed.</param>
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

        /// <summary>
        /// Sends a notification using Firebase Cloud Messaging.
        /// </summary>
        /// <param name="message">The message to send.</param>
        private async Task SendNotification(Message message)
        {
            try
            {
                var messaging = FirebaseMessaging.DefaultInstance;
                await messaging.SendAsync(message);
            }
            catch
            {
                // Log or handle failure to send notification
            }
        }
    }
}