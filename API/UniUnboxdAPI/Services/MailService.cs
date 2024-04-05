using MailKit.Security;
using MimeKit;
using UniUnboxdAPI.Models;
using UniUnboxdAPI.Models.DataTransferObjects.ReviewPage;
using UniUnboxdAPI.Utilities;
using SmtpClient = MailKit.Net.Smtp.SmtpClient;

namespace UniUnboxdAPI.Services
{
    public class MailService(IConfiguration configuration) : INotificationService
    {
        private readonly MailboxAddress uniUnboxdEmail = new("UniUnboxd", "uniunboxd@gmail.com");
        private readonly string uniUnboxdPassword = configuration["UniUnboxdPassword"]!;

        public void SendWelcomeNotification(User user)
        {
            var welcomeMail = new MimeMessage();

            welcomeMail.From.Add(uniUnboxdEmail);
            welcomeMail.To.Add(new MailboxAddress(user.UserName, user.Email));
            welcomeMail.Subject = "Welcome to UniUnboxd!";

            var builder = new BodyBuilder();

            switch (user.UserType)
            {
                case UserType.Student:
                    builder.HtmlBody = NotificationBodyGenerator.WelcomeStudentNotificationBody();
                    break;
                case UserType.University:
                    builder.TextBody = NotificationBodyGenerator.WelcomeUniversityNotificationBody();
                    break;
                case UserType.Professor:
                    builder.HtmlBody = NotificationBodyGenerator.WelcomeProfessorNotificationBody();
                    break;
            }

            welcomeMail.Body = builder.ToMessageBody();

            SendEmail(welcomeMail);
        }

        public void SendNewFollowerNotification(Student studentFollowing, Student studentFollowed)
        {
            var newFollowerMail = new MimeMessage();

            newFollowerMail.From.Add(uniUnboxdEmail);
            newFollowerMail.To.Add(new MailboxAddress(studentFollowed.UserName, studentFollowed.Email));
            newFollowerMail.Subject = "You have a new follower!";

            var builder = new BodyBuilder
            {
                HtmlBody = NotificationBodyGenerator.NewFollowerNotificationBody(studentFollowing.UserName!)
            };
            newFollowerMail.Body = builder.ToMessageBody();

            SendEmail(newFollowerMail);

        }

        public void SendNewReviewNotification(Student receiver, Review review)
        {
            var newReviewMail = new MimeMessage();

            newReviewMail.From.Add(uniUnboxdEmail);
            newReviewMail.To.Add(new MailboxAddress(receiver.UserName, receiver.Email));
            newReviewMail.Subject = "Someone you follow has posted a review!";

            var builder = new BodyBuilder
            {
                HtmlBody = NotificationBodyGenerator.NewReviewNotificationBody(review.Student.UserName!, review.Course.Name)
            };
            newReviewMail.Body = builder.ToMessageBody();

            SendEmail(newReviewMail);
        }

        public void SendNewReplyNotification(Reply reply) 
        {

            var newReplyMail = new MimeMessage();

            newReplyMail.From.Add(uniUnboxdEmail);
            newReplyMail.To.Add(new MailboxAddress(reply.Review.Student.UserName, reply.Review.Student.Email));
            newReplyMail.Subject = "Someone has replied to your review!";

            var builder = new BodyBuilder
            {
                HtmlBody = NotificationBodyGenerator.NewReplyNotificationBody(reply.User.UserName!, reply.Review.Course.Name)
            };
            newReplyMail.Body = builder.ToMessageBody();

            SendEmail(newReplyMail);
        }

        public void SendVerificationStatusChangeNotification(Student student)
            => SendVerificationStatusChangeNotification((User) student);

        public void SendVerificationStatusChangeNotification(User user)
        {
            var verificationStatusChangedMail = new MimeMessage();

            verificationStatusChangedMail.From.Add(uniUnboxdEmail);
            verificationStatusChangedMail.To.Add(new MailboxAddress(user.UserName, user.Email));
            verificationStatusChangedMail.Subject = "The status of your verification application has changed.";

            var builder = new BodyBuilder
            {
                HtmlBody = NotificationBodyGenerator.VerificationStatusChangeBody(user.VerificationStatus)
            };
            verificationStatusChangedMail.Body = builder.ToMessageBody();

            SendEmail(verificationStatusChangedMail);
        }

        public void sendFlagReviewNotification(FlagReviewModel model, int userId)
        {
            var flagReviewEmail = new MimeMessage();

            flagReviewEmail.From.Add(uniUnboxdEmail);
            flagReviewEmail.To.Add(uniUnboxdEmail);
            flagReviewEmail.Subject = "A review has been flagged.";

            var builder = new BodyBuilder
            {
                HtmlBody = NotificationBodyGenerator.FlagReviewNotificationBody(model, userId)
            };
            flagReviewEmail.Body = builder.ToMessageBody();

            SendEmail(flagReviewEmail);
        }

        private async Task SendEmail(MimeMessage email)
        {
            try
            {
                using var client = new SmtpClient();
                await client.ConnectAsync("smtp.gmail.com", 465, SecureSocketOptions.Auto);
                client.AuthenticationMechanisms.Remove("XOAUTH2");
                await client.AuthenticateAsync(uniUnboxdEmail.Address, uniUnboxdPassword);
                await client.SendAsync(email);
                await client.DisconnectAsync(true);
            }
            catch { }
        }
    }
}
