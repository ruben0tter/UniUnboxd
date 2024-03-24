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
        
        
        [HttpGet("get-assigned-professors")]
        [Authorize(Roles = "University")]
        public async Task<IActionResult> GetAssignedProfessors([FromQuery(Name = "courseId")] int courseId)
        {
            if (!await userService.DoesCourseExist(courseId))
                return BadRequest("Course does not exist.");

            var professors = await userService.GetAssignedProfessors(courseId);

            return Ok(professors);
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

        [HttpPut("assign-professor")]
        [Authorize(Roles = "University")]
        public async Task<IActionResult> AssignProfessor([FromQuery(Name = "professor")] int professorId, [FromQuery(Name="course")] int courseId)
        {
            
            if (!await userService.DoesCourseExist(courseId))
                return BadRequest("Course does not exist.");

            Course course = await userService.GetCourse(courseId);
            
            if (!await userService.DoesProfessorExist(professorId))
                    return BadRequest("Professor does not exist.");
        
            Professor professor = await userService.GetProfessor(professorId);

            if (await userService.DoesCourseProfessorAssignmentExist(courseId, professorId))
                return BadRequest("Professor is already assigned to this course.");
            
            await userService.AssignProfessor(professor, course);

            return Ok();
        }
        
        [HttpPut("dismiss-professor")]
        [Authorize(Roles = "University")]
        public async Task<IActionResult> DismissProfessor([FromQuery(Name = "professor")] int professorId, [FromQuery(Name="course")] int courseId)
        {
            
            if (!await userService.DoesCourseExist(courseId))
                return BadRequest("Course does not exist.");

            Course course = await userService.GetCourse(courseId);
            
            if (!await userService.DoesProfessorExist(professorId))
                return BadRequest("Professor does not exist.");
        
            Professor professor = await userService.GetProfessor(professorId);

            if (!await userService.DoesCourseProfessorAssignmentExist(courseId, professorId))
                return BadRequest("Professor is not assigned to this course.");
            
            await userService.DismissProfessor(professor, course);

            return Ok();
        }
    }
}
