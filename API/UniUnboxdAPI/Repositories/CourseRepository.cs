﻿using Microsoft.EntityFrameworkCore;
using UniUnboxdAPI.Data;
using UniUnboxdAPI.Models;

namespace UniUnboxdAPI.Repositories
{
    /// <summary>
    /// Handles all calls to database in regard to Course model.
    /// </summary>
    public class CourseRepository
    {
        private readonly UniUnboxdDbContext dbContext;

        public CourseRepository(UniUnboxdDbContext dbContext)
        {
            this.dbContext = dbContext;
        }

        public async Task<bool> DoesCourseExist(int id)
            => await dbContext.Courses.AnyAsync(c => c.Id == id);

        public async Task<Course> GetCourse(int id)
            => await dbContext.Courses.Where(i => i.Id == id).FirstAsync();

        public async Task UpdateAverageRating(int id, double addedRating)
        {
            var course = await GetCourse(id);
            var reviewCount = await dbContext.Reviews.Where(i => i.Course.Id == id).CountAsync();
            course.AverageRating = ((reviewCount - 1) * course.AverageRating + addedRating) / reviewCount;
            await dbContext.SaveChangesAsync();
        }
    }
}