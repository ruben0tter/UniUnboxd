using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System.Security.Claims;
using UniUnboxdAPI.Models;
using UniUnboxdAPI.Models.DataTransferObjects.Notifications;
using UniUnboxdAPI.Services;
using UniUnboxdAPI.Utilities;

namespace UniUnboxdAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UserController(UserService userService) : ControllerBase
    {
        [HttpPut("set-device-token")]
        [Authorize(Roles = "Student")]
        public async Task<IActionResult> SetDeviceToken([FromBody] DeviceTokenModel model)
        {
            if (!ModelState.IsValid)
                return BadRequest("Not all required fields have been filled in.");

            if (string.IsNullOrWhiteSpace(model.DeviceToken))
                return BadRequest("No device token was provided.");

            int studentId = JWTValidation.GetUserId(HttpContext.User.Identity as ClaimsIdentity);

            await userService.SetDeviceToken(studentId, model.DeviceToken);

            return Ok();
        }

        [HttpPut("follow")]
        [Authorize(Roles = "Student")]
        public async Task<IActionResult> Follow([FromQuery(Name = "id")] int followedStudentId)
        {
            if (!await userService.DoesStudentExist(followedStudentId))
                return BadRequest("Given student does not exist.");

            int followingStudentId = JWTValidation.GetUserId(HttpContext.User.Identity as ClaimsIdentity);

            if (followingStudentId == followedStudentId)
                return BadRequest("You can not follow yourself.");

            if (await userService.DoesStudentFollowStudent(followingStudentId, followedStudentId))
                return BadRequest("Given student is already followed.");

            Student followingStudent = await userService.GetStudent(followingStudentId);
            Student followedStudent = await userService.GetStudent(followedStudentId);

            await userService.FollowStudent(followingStudent, followedStudent);
            userService.NotifyFollowedStudent(followingStudent, followedStudent);

            return Ok();
        }

        [HttpPut("unfollow")]
        [Authorize(Roles = "Student")]
        public async Task<IActionResult> Unfollow([FromQuery(Name = "id")] int unfollowedStudentId)
        {
            if (!await userService.DoesStudentExist(unfollowedStudentId))
                return BadRequest("Given student does not exist.");

            int unfollowingStudentId = JWTValidation.GetUserId(HttpContext.User.Identity as ClaimsIdentity);

            if (unfollowingStudentId == unfollowedStudentId)
                return BadRequest("You can not unfollow yourself.");

            if (!await userService.DoesStudentFollowStudent(unfollowingStudentId, unfollowedStudentId))
                return BadRequest("Given student is not followed.");

            Student unfollowingStudent = await userService.GetStudent(unfollowingStudentId);
            Student unfollowedStudent = await userService.GetStudent(unfollowedStudentId);

            await userService.UnfollowStudent(unfollowingStudent, unfollowedStudent);
            return Ok();
        }
    }
}
