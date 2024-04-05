using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace UniUnboxdAPITests.Services
{
    [TestClass]
    public class ReplyServiceTest
    {
        private readonly UniUnboxdDbContext dbContext;
        private readonly ReplyService replyService;

        public ReplyServiceTest()
        {
            dbContext = DatabaseUtil.CreateDbContext("ReplyService");
            var replyRepository = new ReplyRepository(dbContext);
            var reviewRepository = new ReviewRepository(dbContext);
            var userRepository = new UserRepository(dbContext);
            var mailService = new MailService(ConfigurationUtil.CreateConfiguration());
            var pushNotificationService = new PushNotificationService();
            replyService = new ReplyService(replyRepository, reviewRepository, userRepository, mailService, pushNotificationService);
        }

        [ClassInitialize]
        public static void Init(TestContext context)
        {

        }
    }
}
