using UniUnboxdAPI.Models;
using UniUnboxdAPI.Models.DataTransferObjects;
using UniUnboxdAPI.Repositories;

namespace UniUnboxdAPI.Services
{
    public class VerificationService(VerificationRepository verificationRepository, UserRepository userRepository)
    {
        public async Task<bool> DoesUniversityExist(int universityId)
            => await userRepository.DoesUniversityExist(universityId);

        public async Task<VerificationApplication?> RequestVerification(VerificationModel request, int userId)
        {
            Student? user = await userRepository.GetStudent(userId);

            if (user == null)
                return null;

            University? university = await userRepository.GetUniversity(request.TargetUniversityId);

            var verificationApplication = CreateVerificationApplicationModel(request, user, university);

            return await verificationRepository.AddApplication(verificationApplication);
        }

        public async Task<VerificationStatus?> GetVerificationStatus(int userId)
        {
            var user = await userRepository.GetUser(userId);
            
            if (user == null)
                return null;

            return user.VerificationStatus;
        }

        public async Task<VerificationApplication[]> GetPendingVerificationRequests(int userId, int startID)
        {
            University? user = await userRepository.GetUniversity(userId);
            
            if (user == null)
                return [];

            return await verificationRepository.GetNextApplications(user, startID, 10);
        }

        private async Task<bool> ResolveApplication(AcceptRejectModel request, VerificationStatus status)
        {
            var user = await userRepository.GetUser(request.UserId);

            if (user == null)
                return false;

            return await verificationRepository.SetVerificationStatus(user, status);

            // TODO: Set Student to verified in case of status == verified.
        }

        public async Task<bool> AcceptApplication(AcceptRejectModel request)
            => await ResolveApplication(request, VerificationStatus.Verified);

        public async Task<bool> RejectApplication(AcceptRejectModel request)
            => await ResolveApplication(request, VerificationStatus.Unverified);

        private static VerificationApplication CreateVerificationApplicationModel(VerificationModel request, Student user, University university)
            => new ()
            {
                CreationTime = DateTime.Now,
                LastModificationTime = DateTime.Now,
                VerificationData = request.VerificationData,
                UserToBeVerified = user,
                TargetUniversity = university
            };
    }
}
