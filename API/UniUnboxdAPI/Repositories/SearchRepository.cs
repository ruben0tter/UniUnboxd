﻿using Microsoft.EntityFrameworkCore;
using System.Drawing;
using UniUnboxdAPI.Data;
using UniUnboxdAPI.Models;

namespace UniUnboxdAPI.Repositories
{
    /// <summary>
    /// Handles all calls to database in regard to Course model.
    /// </summary>
    public class SearchRepository(UniUnboxdDbContext dbContext)
    {
        public async Task<List<Course>> GetCourses(SearchOptions options)
            => await dbContext.Courses.Where(i => i.Name.Contains(options.Search) || i.Code == options.Search)
                                    .Skip(options.Start ?? throw new ArgumentNullException(nameof(options.Start)))
                                    .Take(options.Count ?? throw new ArgumentNullException(nameof(options.Count)))
                                    .ToListAsync();

        public async Task<List<Course>> GetAllCourses(SearchOptions options)
            => await dbContext.Courses.Where(i => i.Name.Contains(options.Search) || i.Code == options.Search)
                                    .ToListAsync();

        public async Task<List<Course>> GetCoursesFromUni(SearchOptions options)
            => await dbContext.Courses.Where(i => i.University.Id == options.UniversityId && (i.Name.Contains(options.Search) || i.Code == options.Search))
                                    .Skip(options.Start ?? throw new ArgumentNullException(nameof(options.Start)))
                                    .Take(options.Count ?? throw new ArgumentNullException(nameof(options.Count)))
                                    .ToListAsync();

        public async Task<List<Course>> GetAllCoursesFromUni(SearchOptions options)
            => await dbContext.Courses.Where(i => i.University.Id == options.UniversityId && (i.Name.Contains(options.Search) || i.Code == options.Search))
                                    .ToListAsync();


        public async Task<List<User>> GetUsers(SearchOptions options)
            => await dbContext.Users.Where(i => i.UserName.Contains(options.Search))
                                    .Skip(options.Start ?? throw new ArgumentNullException(nameof(options.Start)))
                                    .Take(options.Count ?? throw new ArgumentNullException(nameof(options.Count)))
                                    .ToListAsync();

        public async Task<List<User>> GetAllUsers(SearchOptions options)
            => await dbContext.Users.Where(i => i.UserName.Contains(options.Search))
                                    .ToListAsync();

        public async Task<Course> GetCourse(int id)
            => await dbContext.Courses.Where(i => i.Id == id).FirstAsync();
    }
}