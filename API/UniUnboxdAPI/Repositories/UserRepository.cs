﻿using Microsoft.EntityFrameworkCore;
using UniUnboxdAPI.Data;
using UniUnboxdAPI.Models;

namespace UniUnboxdAPI.Repositories;

/// <summary>
/// Handles all calls to database in regard to User model.
/// </summary>
public class UserRepository {
        private readonly UniUnboxdDbContext dbContext;

        public UserRepository(UniUnboxdDbContext dbContext)
        {
            this.dbContext = dbContext;
        }

        public async Task<User> GetUser(int id)
            => await dbContext.Users.Where(i => i.Id == id).FirstAsync();

        public async Task<User> GetUser(string email)
            => await dbContext.Users.Where(i => i.Email.Equals(email)).FirstAsync();

        public async Task<bool> DoesStudentExist(int id)
            => await dbContext.Students.AnyAsync(c => c.Id == id);

        public async Task<Student> GetStudent(int id)
            => await dbContext.Students.Where(i => i.Id == id).FirstAsync();

        public async Task<bool> DoesUniversityExist(int id)
            => await dbContext.Universities.AnyAsync(c => c.Id == id);

        public async Task<University> GetUniversity(int id)
            => await dbContext.Universities.Where(i => i.Id == id).FirstAsync();

        public async Task<bool> DoesProfessorExist(int id)
            => await dbContext.Professors.AnyAsync(c => c.Id == id);
        
        public async Task<Student> GetStudentAndConnectedData(int id)
            => await dbContext.Students.Where(i => i.Id == id)
                .Include(i => i.Reviews)!
                .ThenInclude(i => i.Course)
                .FirstAsync(); 
        
        public async Task<Professor> GetProfessorAndConnectedData(int id)
            => await dbContext.Professors.Where(i => i.Id == id)
                .FirstAsync(); 

        public async Task<bool> DoesStudentFollowStudent(int followingStudentId, int followedStudentId)
        {
            var followingStudent = await dbContext.Students.Where(i => i.Id == followingStudentId).Include(i => i.Following).FirstAsync();
            return followingStudent.Following!.Any(i => i.FollowedStudentId == followedStudentId);
        }

        public async Task FollowStudent(Follow followRelation)
        {
            await dbContext.Follows.AddAsync(followRelation);
            await dbContext.SaveChangesAsync();
        }

        public async Task UnfollowStudent(Follow followRelation)
        {
            dbContext.Follows.Remove(followRelation);
            await dbContext.SaveChangesAsync();
        }
    }
