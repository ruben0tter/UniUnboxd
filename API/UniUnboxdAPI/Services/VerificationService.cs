using System.Diagnostics.Eventing.Reader;
using UniUnboxdAPI.Models;
using UniUnboxdAPI.Models.DataTransferObjects;
using UniUnboxdAPI.Repositories;

namespace UniUnboxdAPI.Services
{
    public class VerificationService(VerificationRepository verificationRepository, UserRepository userRepository,
        MailService mailService, PushNotificationService notificationService)
    {
        public async Task<bool> DoesUniversityExist(int universityId)
            => await userRepository.DoesUniversityExist(universityId);

        public async Task<bool?> RequestStudentVerification(VerificationModel request, int userId)
        {
            Student? user = await userRepository.GetStudent(userId);

            if (user == null)
                return null;

            University? university = await userRepository.GetUniversity(request.TargetUniversityId);

            var verificationApplication = CreateVerificationApplicationModel(request, user, university);

            await verificationRepository.AddApplication(verificationApplication);

            await userRepository.SetVerificationStatus(user, VerificationStatus.Pending);

            return true;
        }

        public async Task<bool?> RequestUniversityVerification(VerificationModel request, int userId)
        {
            University? user = await userRepository.GetUniversity(userId);

            if (user == null)
                return null;

            var verificationApplication = CreateVerificationApplicationModel(request, user, null);

            await verificationRepository.AddApplication(verificationApplication);
            await userRepository.SetVerificationStatus(user, VerificationStatus.Pending);

            return true;
        }

        public async Task<VerificationStatus?> GetVerificationStatus(int userId)
        {
            var user = await userRepository.GetUser(userId);
            
            if (user == null)
                return null;

            return user.VerificationStatus;
        }

        public async Task<PendingVerificationsModel[]> GetPendingVerificationRequests(int userId, int startID)
        {
            University? user = await userRepository.GetUniversity(userId);
            
            if (user == null)
                return [];

            VerificationApplication[] applications = await verificationRepository.GetNextApplications(user, startID, 10);

            return await Task.WhenAll(applications.Select(CreatePendingVerificationModel));
        }

        private async Task<PendingVerificationsModel> CreatePendingVerificationModel(VerificationApplication application) {
            User user = await userRepository.GetUser(application.UserId);
            string? image = await userRepository.GetImageOf(user.Id, user.UserType);

            return new() {
                VerificationData = application.VerificationData,
                UserId = application.UserId,
                Name = user.UserName,
                Image = image
            };
        }

        private async Task<bool> ResolveApplication(AcceptRejectModel request, VerificationStatus status)
        {
            var user = await userRepository.GetUser(request.UserId);

            if (user == null)
                return false;

            mailService.SendVerificationStatusChangeNotification(user);
            notificationService.SendVerificationStatusChangeNotification((Student) user);

            bool result = await verificationRepository.SetVerificationStatus(user, status);
            await verificationRepository.RemoveApplication(request.UserId);
            return result;
        }

        public async Task<bool> AcceptApplication(AcceptRejectModel request)
            => await ResolveApplication(request, VerificationStatus.Verified);

        public async Task<bool> RejectApplication(AcceptRejectModel request)
            => await ResolveApplication(request, VerificationStatus.Unverified);

        private static VerificationApplication CreateVerificationApplicationModel(VerificationModel request, User user, University? university)
            => new ()
            {
                CreationTime = DateTime.Now,
                LastModificationTime = DateTime.Now,
                VerificationData = request.VerificationData,
                UserId = user.Id,
                TargetUniversity = university
            };
    }
}
