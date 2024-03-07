using System;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.EntityFrameworkCore;
using UniUnboxdAPI.Data;
using UniUnboxdAPI.Models;
using UniUnboxdAPI.Models.DataTransferObjects;
using UniUnboxdAPI.Repositories;

namespace UniUnboxdAPI.Services
{
    public class VerificationService(VerificationRepository verificationRepository, UserRepository userRepository)
    {
        public async Task<VerificationApplication?> RequestVerification(VerificationModel request, int userId)
        {
            Student? user = await userRepository.GetStudent(userId);
            if (user == null)
            {
                return null;
            }

            var verificationApplication = new VerificationApplication
            {
                CreationTime = DateTime.Now,
                LastModificationTime = DateTime.Now,
                VerificationData = request.VerificationData,
                UserToBeVerified = user,
                TargetUniversity = request.TargetUniversity
            };
            return await verificationRepository.AddApplication(verificationApplication);
        }

        public async Task<VerificationStatus?> GetVerificationStatus(int userId)
        {
            var user = await userRepository.GetUser(userId);
            if (user == null)
            {
                return null;
            }
            return user.VerificationStatus;
        }

        public async Task<VerificationApplication[]> GetPendingVerificationRequests(int userId, int startID)
        {
            University? user = await userRepository.GetUniversity(userId);
            if (user == null)
            {
                return [];
            }

            return await verificationRepository.GetNextApplications(user, startID, 10);
        }

        private async Task<bool> ResolveApplication(AcceptReject request, VerificationStatus status)
        {
            var user = await userRepository.GetUser(request.UserId);
            if (user == null)
            {
                return false;
            }
            return await verificationRepository.SetVerificationStatus(user, status);
        }

        public async Task<bool> AcceptApplication(AcceptReject request)
        {
            return await ResolveApplication(request, VerificationStatus.Verified);
        }

        public async Task<bool> RejectApplication(AcceptReject request)
        {
            return await ResolveApplication(request, VerificationStatus.Unverified);
        }
    }
}
