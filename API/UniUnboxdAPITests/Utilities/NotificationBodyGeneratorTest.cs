using Microsoft.IdentityModel.Tokens;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UniUnboxdAPI.Models.DataTransferObjects.ReviewPage;

namespace UniUnboxdAPITests.Utilities
{
    [TestClass]
    public class NotificationBodyGeneratorTest
    {
        [TestMethod]
        public void WelcomeStudentNotificationBodyTest()
        {
            var result = NotificationBodyGenerator.WelcomeStudentNotificationBody();

            Assert.IsFalse(result.IsNullOrEmpty());
        }

        [TestMethod]
        public void WelcomeUniversityNotificationBodyTest()
        {
            var result = NotificationBodyGenerator.WelcomeUniversityNotificationBody();

            Assert.IsFalse(result.IsNullOrEmpty());
        }

        [TestMethod]
        public void WelcomeProfessorNotificationBodyTest()
        {
            var result = NotificationBodyGenerator.WelcomeProfessorNotificationBody();

            Assert.IsFalse(result.IsNullOrEmpty());
        }

        [TestMethod]
        public void NewFollowerNotificationBodyTest()
        {
            var name = "test";
            var result = NotificationBodyGenerator.NewFollowerNotificationBody("test");

            Assert.IsFalse(result.IsNullOrEmpty());
            Assert.IsTrue(result.Contains(name));
        }

        [TestMethod]
        public void NewReviewNotificationBodyTest()
        {
            var studentName = "student";
            var courseName = "test";
            var result = NotificationBodyGenerator.NewReviewNotificationBody(studentName, courseName);

            Assert.IsFalse(result.IsNullOrEmpty());
            Assert.IsTrue(result.Contains(studentName));
            Assert.IsTrue(result.Contains(courseName));
        }

        [TestMethod]
        public void NewReplyNotificationBodyTest()
        {
            var userName = "user";
            var courseName = "test";
            var result = NotificationBodyGenerator.NewReplyNotificationBody(userName, courseName);

            Assert.IsFalse(result.IsNullOrEmpty());
            Assert.IsTrue(result.Contains(userName));
            Assert.IsTrue(result.Contains(courseName));
        }

        [TestMethod]
        public void FlagReviewNotificationBodyTestWithMessage()
        {
            var model = new FlagReviewModel { ReviewId = 111, Message = "flag-review-message" };
            var userId = 999;
            var result = NotificationBodyGenerator.FlagReviewNotificationBody(model, userId);

            Assert.IsFalse(result.IsNullOrEmpty());
            Assert.IsTrue(result.Contains(model.ReviewId.ToString()));
            Assert.IsTrue(result.Contains(model.Message));
            Assert.IsTrue(result.Contains(userId.ToString()));
        }

        [TestMethod]
        public void FlagReviewNotificationBodyTestWithoutMessage()
        {
            var model = new FlagReviewModel { ReviewId = 111, Message = string.Empty };
            var userId = 999;
            var result = NotificationBodyGenerator.FlagReviewNotificationBody(model, userId);

            Assert.IsFalse(result.IsNullOrEmpty());
            Assert.IsTrue(result.Contains(model.ReviewId.ToString()));
            Assert.IsTrue(result.Contains(userId.ToString()));
            Assert.IsFalse(result.Contains("The following message was given:"));
        }
    }
}
