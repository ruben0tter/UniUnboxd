using System.Security.Claims;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using UniUnboxdAPI.Models;
using UniUnboxdAPI.Models.DataTransferObjects;
using UniUnboxdAPI.Services;

namespace UniUnboxdAPI.Controllers
{
    [Route("api/verify")]
    [ApiController]
    [Authorize]
    public class VerificationController(VerificationService verificationService) : ControllerBase
    {
        private readonly VerificationService verificationService = verificationService;

        [HttpPost]
        [Authorize(Roles = "Student, University")]
        [Route("request")]
        public async Task<IActionResult> RequestVerification([FromBody] VerificationModel request)
        {
            // get the user id from the request's jwt token
            int userID = int.Parse(this.User.Claims.First(i => i.Type == "UserID").Value);
            string role = this.User.Claims.First(i => i.Type == "UserRole").Value;

            var result = await verificationService.RequestVerification(request, userID);

            if (result == null)
            {
                return BadRequest();
            }
            return Ok(result);
        }

        [Authorize(Roles = "Student, University")]
        [HttpGet]
        [Route("status")]
        public async Task<IActionResult> GetUserVerificationStatus() {
            int userID = int.Parse(this.User.Claims.First(i => i.Type == "UserID").Value);
            var result = await verificationService.GetVerificationStatus(userID);
            if (result == null) {
                return BadRequest();
            }
            return Ok(result);
        }

        [Authorize(Roles = "University")]
        [HttpGet]
        [Route("pending")]
        public async Task<IActionResult> GetPendingVerificationRequests([FromQuery(Name = "startID")] int startID) {
            int uniUserID = int.Parse(this.User.Claims.First(i => i.Type == "UserID").Value);
            var result = await verificationService.GetPendingVerificationRequests(uniUserID, startID);
            if (result == null) {
                return BadRequest();
            }
            return Ok(result);
        }

        [Authorize(Roles = "University")]
        [HttpPut]
        [Route("set")]
        public async Task<IActionResult> SetVerification([FromBody] AcceptReject request)
        {
            // get the user id from the request's jwt token
            int userID = int.Parse(this.User.Claims.First(i => i.Type == "UserID").Value);
            // string role = this.User.Claims.First(i => i.Type == "UserRole").Value;
            
            
            bool result;
            if(request.AcceptedOrRejected) {
                result = await verificationService.AcceptApplication(request);
            } else {
                result = await verificationService.RejectApplication(request);
            }

            if (result == false)
            {
                return BadRequest();
            }
            return Ok(result);
        }
    }
}
