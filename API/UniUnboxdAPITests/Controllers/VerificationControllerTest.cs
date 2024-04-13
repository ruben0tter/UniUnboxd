using Microsoft.AspNetCore.Mvc;

namespace UniUnboxdAPITests.Controllers;

[TestClass]
public class VerificationControllerTest
{
    private readonly UniUnboxdDbContext dbContext;
    private readonly VerificationController verificationController;
    
    public VerificationControllerTest()
    {
        dbContext = DatabaseUtil.CreateDbContext("VerificationController");
        var verificationRepository = new VerificationRepository(dbContext);
        var userRepository = new UserRepository(dbContext);
        var mailService = new MailService(ConfigurationUtil.CreateConfiguration());
        var pushNotificationService = new PushNotificationService();
        var verificationService = new VerificationService(verificationRepository, userRepository, mailService, pushNotificationService);
        verificationController = new VerificationController(verificationService);
    }
    
    
    [ClassInitialize]
    public static void Init(TestContext context)
    {
        var dbContext = DatabaseUtil.CreateDbContext("VerificationController");

        var university = new University { Id = 1, Email = "university@gmail.com" };
        var student = new Student { Id = 2, Email = "student@gmail.com", NotificationSettings = new NotificationSettings() };
        var student1 = new Student { Id = 3, Email = "student1@gmail.com", NotificationSettings = new NotificationSettings() };
        var application = new VerificationApplication { UserId = 1, VerificationData = ["data"] };
        var application1 = new VerificationApplication { UserId = 2, VerificationData = ["data"], TargetUniversity = university };
        var application2 = new VerificationApplication { UserId = 2, VerificationData = ["data"], TargetUniversity = university };

        dbContext.Users.Add(university);
        dbContext.Users.Add(student);
        dbContext.Users.Add(student1);
        dbContext.Applications.Add(application);
        dbContext.Applications.Add(application1);
        dbContext.Applications.Add(application2);
        dbContext.SaveChanges();
    }
    
    [TestMethod]
    public async Task RequestVerificationTestWithInvalidUniversityId()
    {
        ConfigurationUtil.SetHttpContext(verificationController, 2, UserType.Student);
        var model = new VerificationModel() { VerificationData = ["hey"], TargetUniversityId = 999};
        ObjectResult result = (ObjectResult)await verificationController.RequestVerification(model);
        
        Assert.AreEqual("Given university does not exist.", result.Value);
        Assert.AreEqual(400, result.StatusCode);
    }

    [TestMethod]
    public async Task RequestVerificationTestWithUni()
    {
        ConfigurationUtil.SetHttpContext(verificationController, 1, UserType.University);
        var model = new VerificationModel() { VerificationData = ["hey"]};
        ObjectResult result = (ObjectResult)await verificationController.RequestVerification(model);
        
        Assert.IsTrue(dbContext.Applications.Any(i => i.VerificationData == model.VerificationData));
        Assert.AreEqual(200, result.StatusCode);
    }
    
    [TestMethod]
    public async Task RequestVerificationTestWithStudent()
    {
        ConfigurationUtil.SetHttpContext(verificationController, 2, UserType.Student);
        var model = new VerificationModel() { VerificationData = ["hey"], TargetUniversityId = 1};
        ObjectResult result = (ObjectResult)await verificationController.RequestVerification(model);
        
        Assert.AreEqual(200, result.StatusCode);
        Assert.IsTrue(dbContext.Applications.Any(i => i.VerificationData == model.VerificationData && i.VerificationData.Contains("hey")));
    }
    
    [TestMethod]
    public async Task GetUserVerificationStatusTestWithStudent()
    {
        ConfigurationUtil.SetHttpContext(verificationController, 2, UserType.Student);
        ObjectResult result = (ObjectResult)await verificationController.GetUserVerificationStatus();
        
        Assert.AreEqual(200, result.StatusCode);
    }
    
    [TestMethod]
    public async Task GetPendingVerificationRequestsTestWithUni()
    {
        ConfigurationUtil.SetHttpContext(verificationController, 1, UserType.University);
        ObjectResult result = (ObjectResult)await verificationController.GetPendingVerificationRequests(1);
        
        Assert.AreEqual(200, result.StatusCode);
    }
    
    [TestMethod]
    public async Task SetVerificationTestTrue()
    {
        ConfigurationUtil.SetHttpContext(verificationController, 1, UserType.University);
        var model = new AcceptRejectModel(){UserId = 2, AcceptedOrRejected = true};
        var result = (ObjectResult)await verificationController.SetVerification(model);
        
        Assert.IsTrue((bool)result.Value!);
        Assert.AreEqual(200, result.StatusCode);
    }
    
    [TestMethod]
    public async Task SetVerificationTestFalse()
    {
        ConfigurationUtil.SetHttpContext(verificationController, 1, UserType.University);
        var model = new AcceptRejectModel(){UserId = 2, AcceptedOrRejected = false};
        var result = (ObjectResult)await verificationController.SetVerification(model);
        
        Assert.AreEqual(200, result.StatusCode);
        
        
    }
    
}