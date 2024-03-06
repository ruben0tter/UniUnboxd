using System;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http.HttpResults;
using UniUnboxdAPI.Data;
using UniUnboxdAPI.Models;
using UniUnboxdAPI.Models.DataTransferObjects;

namespace UniUnboxdAPI.Services
{
    public class VerificationService(UniUnboxdDbContext context)
    {
        public async Task<VerificationApplication?> RequestVerification(VerificationModel request, int userId)
        {
            Student? user = await context.Students.FindAsync(userId);
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
            context.Applications.Add(verificationApplication);
            await context.SaveChangesAsync();
            return verificationApplication;
        }

        public async Task<VerificationStatus?> GetVerificationStatus(int userId)
        {
            var user = await context.Users.FindAsync(userId);
            if (user == null)
            {
                return null;
            }
            return user.VerificationStatus;
        }

        public async Task<VerificationApplication[]> GetPendingVerificationRequests(int userId, int startID)
        {
            University? user = await context.Universities.FindAsync(userId);
            if (user == null)
            {
                return [];
            }
        
            return context.Applications.Where(a => a.TargetUniversity == user && a.Id > startID).Take(10).ToArray();
        }

        private async Task<bool> ResolveApplication(AcceptReject request, VerificationStatus status)
        {
            var user = await context.Users.FindAsync(request.UserId);
            if (user == null)
            {
                return false;
            }
            user.VerificationStatus = status;
            context.SaveChanges();
            return true;
        }

        public async Task<bool> AcceptApplication(AcceptReject request) {
            return await ResolveApplication(request, VerificationStatus.Verified);
        }

        public async Task<bool> RejectApplication(AcceptReject request) {
            return await ResolveApplication(request, VerificationStatus.Unverified);
        }
    }
}
