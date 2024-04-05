using UniUnboxdAPI.Models;

namespace UniUnboxdAPI.Services
{
    public interface INotificationService
    {
        /// <summary>
        /// Sends a notification to a student when they gain a new follower.
        /// </summary>
        /// <param name="studentFollowing">The student who started following.</param>
        /// <param name="studentFollowed">The student who was followed.</param>
        void SendNewFollowerNotification(Student studentFollowing, Student studentFollowed);

        /// <summary>
        /// Sends a notification to a student when a new review is posted for a course they are interested in.
        /// </summary>
        /// <param name="receiver">The student receiving the notification.</param>
        /// <param name="review">The review that triggers the notification.</param>
        void SendNewReviewNotification(Student receiver, Review review);

        /// <summary>
        /// Sends a notification when a new reply is made to a review.
        /// </summary>
        /// <param name="reply">The reply that triggers the notification.</param>
        void SendNewReplyNotification(Reply reply);

        /// <summary>
        /// Notifies a student about the change in their verification status.
        /// </summary>
        /// <param name="student">The student whose verification status has changed.</param>
        void SendVerificationStatusChangeNotification(Student student);
    }
}
