using UniUnboxdAPI.Models;

namespace UniUnboxdAPI.Services
{
    public interface INotificationService
    {
        public void SendNewFollowerNotification(Student studentFollowing, Student studentFollowed);
        public void SendNewReviewNotification(Student receiver, Review review);
        public void SendNewReplyNotification(Reply reply);
    }
}
