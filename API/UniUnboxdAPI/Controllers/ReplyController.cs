using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System.Security.Claims;
using UniUnboxdAPI.Models.DataTransferObjects;
using UniUnboxdAPI.Models;
using UniUnboxdAPI.Services;
using UniUnboxdAPI.Utilities;
using UniUnboxdAPI.Models.DataTransferObjects.ReviewPage;

namespace UniUnboxdAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ReplyController(ReplyService replyService) : ControllerBase
    {
        [HttpPost]
        [Authorize(Roles = "Student, Professor")]
        public async Task<IActionResult> PostReply([FromBody] ReplyModel model)
        {
            if (!ModelState.IsValid)
                return BadRequest("Not all required fields have been filled in.");

            int userId = JWTValidation.GetUserId(HttpContext.User.Identity as ClaimsIdentity);

            if (!await replyService.DoesUserExist(userId))
                return BadRequest("Given user does not exist.");

            if (!await replyService.DoesReviewExist(model.ReviewId))
                return BadRequest("Given review does not exist.");

            try
            {
                Reply reply = await replyService.CreateReply(model, userId);

                await replyService.PostReply(reply);

                return Ok("Succesfully created reply.");
            }
            catch (Exception ex)
            {
                return BadRequest("Something went wrong when creating a reply.\nThe following exception was thrown:\n" + ex.Message);
            }
        }
    }
}
