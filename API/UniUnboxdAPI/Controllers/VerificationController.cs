using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Security.Claims;
using UniUnboxdAPI.Models.DataTransferObjects;
using UniUnboxdAPI.Services;
using UniUnboxdAPI.Utilities;

namespace UniUnboxdAPI.Controllers
{
    [Route("api/verify")]
    [ApiController]
    [Authorize]
    public class VerificationController(VerificationService verificationService) : ControllerBase
    {
        [HttpPost]
        [Authorize(Roles = "Student, University")]
        [Route("request")]
        public async Task<IActionResult> RequestVerification([FromBody] VerificationModel request)
        {
            int userID = JWTValidation.GetUserId(HttpContext.User.Identity as ClaimsIdentity);
            string role = JWTValidation.GetRole(HttpContext.User.Identity as ClaimsIdentity);

            Console.WriteLine("Requesting verification for user: " + userID);

            bool? result = null;

            if (role.Equals("Student"))
            {
                if (!await verificationService.DoesUniversityExist(request.TargetUniversityId))
                    return BadRequest("Given university does not exist.");

                result = await verificationService.RequestStudentVerification(request, userID);
            } else if (role.Equals("University"))
            {
                result = await verificationService.RequestUniversityVerification(request, userID);
            }

            return Ok(result);
        }

        [HttpGet]
        [Authorize(Roles = "Student, University")]
        [Route("status")]
        public async Task<IActionResult> GetUserVerificationStatus() {
            int userID = JWTValidation.GetUserId(HttpContext.User.Identity as ClaimsIdentity);

            var result = await verificationService.GetVerificationStatus(userID);

            if (result == null)
                return BadRequest();

            return Ok(result);
        }

        [HttpGet]
        [Authorize(Roles = "University")]
        [Route("pending")]
        public async Task<IActionResult> GetPendingVerificationRequests([FromQuery(Name = "startID")] int startID) {
            int uniUserID = JWTValidation.GetUserId(HttpContext.User.Identity as ClaimsIdentity);

            var result = await verificationService.GetPendingVerificationRequests(uniUserID, startID);

            if (result == null)
                return BadRequest();

            return Ok(result);
        }

        [HttpPut]
        [Authorize(Roles = "University")]
        [Route("set")]
        public async Task<IActionResult> SetVerification([FromBody] AcceptRejectModel request)
        {
            int userId = JWTValidation.GetUserId(HttpContext.User.Identity as ClaimsIdentity);

            bool result;

            if(request.AcceptedOrRejected)
                result = await verificationService.AcceptApplication(request, userId);
            else
                result = await verificationService.RejectApplication(request, userId);

            return Ok(result);
        }
    }
}
