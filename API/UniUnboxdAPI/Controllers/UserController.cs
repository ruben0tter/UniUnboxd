using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Security.Claims;
using UniUnboxdAPI.Models;
using UniUnboxdAPI.Models.DataTransferObjects;
﻿using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System.Security.Claims;
using Azure.Core.Extensions;
using Microsoft.IdentityModel.Abstractions;
using UniUnboxdAPI.Models;
using UniUnboxdAPI.Models.DataTransferObjects.Notifications;
using UniUnboxdAPI.Services;
using UniUnboxdAPI.Utilities;

namespace UniUnboxdAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    [Authorize]
    public class UserController(UserService userService) : ControllerBase
    {
        [HttpGet("student")]
        [Authorize(Roles = "Student, Professor")]
        public async Task<IActionResult> GetStudent([FromQuery] int id)
        {
            StudentProfileModel student = await userService.GetStudentAndConnectedData(id);
            
            return Ok(student);
        }
        
        [HttpGet("professor")]
        [Authorize(Roles = "Student, Professor")]
        public async Task<IActionResult> GetProfessor([FromQuery] int id)
        {
            ProfessorProfileModel professor = await userService.GetProfessorAndConnectedData(id);
            
            return Ok(professor);
        }

        [HttpGet("get-universities")]
        [Authorize(Roles = "Student")]
        public async Task<IActionResult> GetUniversities()
        {
            try
            {
                List<UniversityNameModel> universities = await userService.GetUniversities();
                return Ok(universities);
            }
            catch
            {
             return BadRequest("Could not get universities.");
            }

        }

        [HttpGet("get-assigned-professor")]
        [Authorize(Roles = "University")]
        public async Task<IActionResult> GetAssignedProfessorByEmail([FromQuery(Name = "email")] string email)
        {
            if (!await userService.DoesProfessorExist(email)) 
                return BadRequest("Professor does not exist.");
            AssignedProfessorModel model = await userService.getAssignedProfessor(email);
            return Ok(model);
        }
        
        [HttpGet("get-assigned-professors")]
        [Authorize(Roles = "University")]
        public async Task<IActionResult> GetAssignedProfessors([FromQuery(Name = "id")] int courseId)
        {
            if (!await userService.DoesCourseExist(courseId))
                return BadRequest("Course does not exist.");

            var professors = await userService.GetAssignedProfessors(courseId);

            return Ok(professors);
        }

        [HttpGet("followed-students")]
        [Authorize(Roles = "Student")]
        public async Task<IActionResult> GetFollowedStudents()
        {
            int userId = JWTValidation.GetUserId(HttpContext.User.Identity as ClaimsIdentity);
            try
            {
                var models = await userService.GetFollowedStudents(userId);
                return Ok(models);
            }
            catch
            {
                return BadRequest("Could not get followed students.");
            }
        }
        
        [HttpGet("followers")]
        [Authorize(Roles = "Student")]
        public async Task<IActionResult> GetFollowers()
        {
            int userId = JWTValidation.GetUserId(HttpContext.User.Identity as ClaimsIdentity);
            try
            {
                var models = await userService.GetFollowers(userId);
                return Ok(models);
            }
            catch
            {
                return BadRequest("Could not get followed students.");
            }
        }

        [HttpGet("student-list-item")]
        [Authorize(Roles = "Student")]
        public async Task<IActionResult> GetStudentListItem([FromQuery(Name = "id")] int id)
        {
            try
            {
                var model = await userService.GetStudentListItem(id);
                return Ok(model);
            }
            catch
            {
                return BadRequest("Could not get user.");
            }
        }
        
        [HttpPut("student")]
        [Authorize(Roles = "Student")]
        public async Task<IActionResult> PutStudent([FromBody] StudentEditModel model)
        {
            if (!await userService.DoesStudentExist(model.Id))
                return BadRequest("This student does not exist.");
            
            if (!JWTValidation.IsUserValidated(HttpContext.User.Identity as ClaimsIdentity, model.Id))
                return BadRequest("Incorrect user.");

            Student student = await userService.GetStudent(model.Id);
            userService.UpdateStudent(student, model);
            await userService.PutStudent(student);
            
            return Ok("Student successfully updated");
        }
        
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

        [HttpPut("professor")]
        [Authorize(Roles = "Professor")]
        public async Task<IActionResult> PutProfessor([FromBody] ProfessorEditModel model)
        {
            if (!await userService.DoesProfessorExist(model.Id))
                return BadRequest("This professor user does not exist.");
            
            if (!JWTValidation.IsUserValidated(HttpContext.User.Identity as ClaimsIdentity, model.Id))
                return BadRequest("Incorrect user.");

            Professor professor = await userService.GetProfessor(model.Id);

            userService.UpdateProfessor(professor, model);
            await userService.PutProfessor(professor);

            return Ok("Professor was updated successfully.");
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
                return BadRequest("You cannot unfollow yourself.");

            if (!await userService.DoesStudentFollowStudent(unfollowingStudentId, unfollowedStudentId))
                return BadRequest("Given student is not followed.");

            Student unfollowingStudent = await userService.GetStudent(unfollowingStudentId);
            Student unfollowedStudent = await userService.GetStudent(unfollowedStudentId);

            await userService.UnfollowStudent(unfollowingStudent, unfollowedStudent);
            return Ok();
        }
    }
}
