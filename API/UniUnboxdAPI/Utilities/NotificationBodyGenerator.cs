using UniUnboxdAPI.Models;

namespace UniUnboxdAPI.Utilities
{
    public static class NotificationBodyGenerator
    {
        public static string NewFollowerNotificationBody(Student studentFollowing)
        {
            return "Good news :), you have a new follower! Say hello to " + studentFollowing.UserName;
        }

        public static string NewReviewNotificationBody(Review review)
        {
            return review.Student.UserName + " has posted a review for the course " + review.Course.Name + ". Go check it out :)";
        }
        public static string NewReplyNotificationBody(Reply reply)
        {
            return reply.User.UserName + " has replied to you review for the course " + reply.Review.Course.Name;
        }
    }
}
