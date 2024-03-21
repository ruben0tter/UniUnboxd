using UniUnboxdAPI.Models;

namespace UniUnboxdAPI.Utilities
{
    public static class MailBodyGenerator
    {
        public static string NewFollowerMailBody(Student studentFollowing)
        {
            return "Good news :), you have a new follower! Say hello to " + studentFollowing.UserName;
        }

        public static string NewReviewMailBody(Review review)
        {
            return review.Student + " has posted a review for the course " + review.Course.Name + ". Go check it out :)";
        }
        public static string NewReplyMailBody(Reply reply)
        {
            return reply.User.UserName + " has replied to you review for the course " + reply.Review.Course.Name;
        }
    }
}
