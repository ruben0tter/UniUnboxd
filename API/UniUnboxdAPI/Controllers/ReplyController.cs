﻿using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Security.Claims;
using UniUnboxdAPI.Models;
using UniUnboxdAPI.Services;
using UniUnboxdAPI.Utilities;
using UniUnboxdAPI.Models.DataTransferObjects.ReviewPage;

namespace UniUnboxdAPI.Controllers
{
    /// <summary>
    /// Controller to create replies within the database,
    /// this can be done by both a student and a professor user.
    /// </summary>
    /// <param name="replyService"></param>
    [Route("api/[controller]")]
    [ApiController]
    public class ReplyController(ReplyService replyService) : ControllerBase
    {
        /// <summary>
        /// Creates a reply to a review.
        /// </summary>
        /// <param name="model">The reply model containing the details of the reply.</param>
        /// <returns>Returns a HTTP response with the result of the operation.</returns>
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

                replyService.NotifyReviewAuthor(reply);

                var DTO = replyService.CreateReviewReplyModel(reply);

                return Ok(DTO);
            }
            catch (Exception ex)
            {
                return BadRequest("Something went wrong when creating a reply.\nThe following exception was thrown:\n" + ex.Message);
            }
        }
    }
}