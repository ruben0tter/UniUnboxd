using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Security.Claims;
using UniUnboxdAPI.Models;
using UniUnboxdAPI.Models.DataTransferObjects;
﻿using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System.Security.Claims;
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
        [Authorize(Roles = "Student")]
        public async Task<IActionResult> GetStudent()
        {
            int id = JWTValidation.GetUserId(HttpContext.User.Identity as ClaimsIdentity);
            
            StudentProfileModel student = await userService.GetStudentAndConnectedData(id);
            
            return Ok(student);
        }
        
        [HttpGet("professor")]
        [Authorize(Roles = "Professor")]
        public async Task<IActionResult> GetProfessor()
        {
            int id = JWTValidation.GetUserId(HttpContext.User.Identity as ClaimsIdentity);
            
            
            ProfessorProfileModel professor = await userService.GetProfessorAndConnectedData(id);
            
            
            return Ok(professor);
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
