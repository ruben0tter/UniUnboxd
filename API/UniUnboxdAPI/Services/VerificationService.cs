using UniUnboxdAPI.Models;
using UniUnboxdAPI.Models.DataTransferObjects;
using UniUnboxdAPI.Repositories;

namespace UniUnboxdAPI.Services
{
    public class VerificationService(VerificationRepository verificationRepository, UserRepository userRepository,
        MailService mailService, PushNotificationService notificationService)
    {
        /// <summary>
        /// Checks if a university exists.
        /// </summary>
        /// <param name="universityId">The university ID.</param>
        /// <returns>True if the university exists, else false.</returns>
        public async Task<bool> DoesUniversityExist(int universityId)
            => await userRepository.DoesUniversityExist(universityId);

        /// <summary>
        /// Requests verification for a student.
        /// </summary>
        /// <param name="request">The verification request details.</param>
        /// <param name="userId">The user ID of the requesting student.</param>
        /// <returns>True if successful, null if the user does not exist.</returns>
        public async Task<bool?> RequestStudentVerification(VerificationModel request, int userId)
        {
            Student? user = await userRepository.GetStudent(userId);

            if (user == null) return null;

            University? university = await userRepository.GetUniversity(request.TargetUniversityId);
            var verificationApplication = CreateVerificationApplicationModel(request, user, university);

            await verificationRepository.AddApplication(verificationApplication);
            await userRepository.SetVerificationStatus(user, VerificationStatus.Pending);

            return true;
        }

        /// <summary>
        /// Requests verification for a university.
        /// </summary>
        /// <param name="request">The verification request details.</param>
        /// <param name="userId">The university user ID.</param>
        /// <returns>True if successful, null if the user does not exist.</returns>
        public async Task<bool?> RequestUniversityVerification(VerificationModel request, int userId)
        {
            University? user = await userRepository.GetUniversity(userId);

            if (user == null) return null;

            var verificationApplication = CreateVerificationApplicationModel(request, user, null);
            await verificationRepository.AddApplication(verificationApplication);
            await userRepository.SetVerificationStatus(user, VerificationStatus.Pending);

            return true;
        }

        /// <summary>
        /// Gets the verification status of a user.
        /// </summary>
        /// <param name="userId">The user ID.</param>
        /// <returns>The verification status, or null if the user does not exist.</returns>
        public async Task<VerificationStatus?> GetVerificationStatus(int userId)
        {
            var user = await userRepository.GetUser(userId);
            
            if (user == null)
                return null;

            return user.VerificationStatus;
        }

        /// <summary>
        /// Gets pending verification requests.
        /// </summary>
        /// <param name="userId">The university's user ID.</param>
        /// <param name="startID">The ID to start fetching from.</param>
        /// <returns>An array of pending verification models.</returns>
        public async Task<PendingVerificationsModel[]> GetPendingVerificationRequests(int userId, int startID)
        {
            University? user = await userRepository.GetUniversity(userId);
            if (user == null) return new PendingVerificationsModel[] { };

            VerificationApplication[] applications = await verificationRepository.GetNextApplications(user, startID, 10);
            List<PendingVerificationsModel> result = new List<PendingVerificationsModel>();

            foreach (VerificationApplication app in applications)
            {
                result.Add(await CreatePendingVerificationModel(app));
            }

            return result.ToArray();
        }

        /// <summary>
        /// Accepts a verification application.
        /// </summary>
        /// <param name="request">The accept/reject model.</param>
        /// <param name="universityId">The university ID for verification.</param>
        /// <returns>True if successful.</returns>
        public async Task<bool> AcceptApplication(AcceptRejectModel request, int universityId)
            => await ResolveApplication(request, VerificationStatus.Verified, universityId);

        /// <summary>
        /// Rejects a verification application.
        /// </summary>
        /// <param name="request">The accept/reject model.</param>
        /// <param name="universityId">The university ID, if applicable.</param>
        /// <returns>True if successful.</returns>
        public async Task<bool> RejectApplication(AcceptRejectModel request, int universityId)
            => await ResolveApplication(request, VerificationStatus.Unverified, universityId);

        /// <summary>
        /// Creates a pending verification model from an application.
        /// </summary>
        /// <param name="application">The verification application.</param>
        /// <returns>The pending verification model.</returns>
        private async Task<PendingVerificationsModel> CreatePendingVerificationModel(VerificationApplication application)
        {
            User user = await userRepository.GetUser(application.UserId);
            string? image = await userRepository.GetImageOf(user.Id, user.UserType);

            return new PendingVerificationsModel
            {
                VerificationData = application.VerificationData,
                UserId = application.UserId,
                Email = user.Email!,
                Image = image ?? string.Empty
            };
        }

        /// <summary>
        /// Resolves (accepts or rejects) a verification application.
        /// </summary>
        /// <param name="request">The request details.</param>
        /// <param name="status">The new verification status.</param>
        /// <param name="universityId">The university ID, for setting in case of acceptance.</param>
        /// <returns>True if the operation was successful.</returns>
        private async Task<bool> ResolveApplication(AcceptRejectModel request, VerificationStatus status, int universityId)
        {
            var user = await userRepository.GetUser(request.UserId);
            if (user == null) return false;

            bool result = await userRepository.SetVerificationStatus(user, status);
            if (status == VerificationStatus.Verified) await verificationRepository.SetUniversity(user, universityId);
            await verificationRepository.RemoveApplication(request.UserId);

            mailService.SendVerificationStatusChangeNotification(user);
            notificationService.SendVerificationStatusChangeNotification((Student)user);

            return result;
        }

        /// <summary>
        /// Creates a verification application model.
        /// </summary>
        /// <param name="request">The verification request details.</param>
        /// <param name="user">The user requesting verification.</param>
        /// <param name="university">The target university, if applicable.</param>
        /// <returns>A new verification application model.</returns>
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
