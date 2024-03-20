using MailKit.Net.Smtp;
using MailKit.Security;
using Microsoft.AspNetCore.Mvc.Controllers;
using Microsoft.Extensions.Azure;
using MimeKit;
using System.Net.Mail;
using System.Reflection.Emit;
using UniUnboxdAPI.Models;
using UniUnboxdAPI.Utilities;
using SmtpClient = MailKit.Net.Smtp.SmtpClient;

namespace UniUnboxdAPI.Services
{
    public class MailService
    {
        private readonly MailboxAddress uniUnboxdEmail = new("UniUnboxd", "uniunboxd@gmail.com");
        private readonly string uniUnboxdPassword;

        public MailService(IConfiguration configuration)
        {
            uniUnboxdPassword = configuration["UniUnboxdPassword"];
        }

        public void NewFollowerMail(Student studentFollowed, Student studentFollowing)
        {
            var newFollowerMail = new MimeMessage();

            newFollowerMail.From.Add(uniUnboxdEmail);
            newFollowerMail.To.Add(new MailboxAddress(studentFollowed.UserName, studentFollowed.Email));
            newFollowerMail.Subject = "You have a new follower!";

            var builder = new BodyBuilder
            {
                HtmlBody = MailBodyGenerator.NewFollowerMailBody(studentFollowing)
            };
            newFollowerMail.Body = builder.ToMessageBody();

            SendEmail(newFollowerMail);

        }

        public void NewReviewMail(Student receiver, Review review)
        {
            var newReviewMail = new MimeMessage();

            newReviewMail.From.Add(uniUnboxdEmail);
            newReviewMail.To.Add(new MailboxAddress(receiver.UserName, receiver.Email));
            newReviewMail.Subject = "Someone you follow has posted a review!";

            var builder = new BodyBuilder
            {
                HtmlBody = MailBodyGenerator.NewReviewMailBody(review)
            };
            newReviewMail.Body = builder.ToMessageBody();

            SendEmail(newReviewMail);
        }

        public void NewReplyMail(Reply reply) {

            var newReplyMail = new MimeMessage();

            newReplyMail.From.Add(uniUnboxdEmail);
            newReplyMail.To.Add(new MailboxAddress(reply.Review.Student.UserName, reply.Review.Student.Email));
            newReplyMail.Subject = "Someone has replied to your review!";

            var builder = new BodyBuilder
            {
                HtmlBody = MailBodyGenerator.NewReplyMailBody(reply)
            };
            newReplyMail.Body = builder.ToMessageBody();

            SendEmail(newReplyMail);
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
