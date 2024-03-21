using Microsoft.IdentityModel.Tokens;
using System.ComponentModel;
using System.Security.Claims;
using UniUnboxdAPI.Models;
using UniUnboxdAPI.Models.DataTransferObjects;
using UniUnboxdAPI.Repositories;
using UniUnboxdAPI.Utilities;

namespace UniUnboxdAPI.Services
{
    public class SearchService(SearchRepository searchRepository, UserRepository userRepository)
    {

        public async Task<List<CourseSearchModel>> GetCourses(SearchOptions options) {
            List<Course> courses = [];

            if (options.Start.HasValue && options.Count.HasValue) {
                courses = await searchRepository.GetCourses(options.Search, options.Start.Value, options.Count.Value);
            } else {
                courses = await searchRepository.GetAllCourses(options.Search);
            }

            return courses.Select(CreateCourseSearchModel).ToList();
        }

        public async Task<List<UserSearchModel>> GetUsers(SearchOptions options) {
            List<User> users = [];
            if (options.Start.HasValue && options.Count.HasValue) {
                users = await searchRepository.GetUsers(options.Search, options.Start.Value, options.Count.Value);
            } else {
                users = await searchRepository.GetAllUsers(options.Search);
            }

            return users.Select(CreateUserSearchModel).ToList();
        }

        private CourseSearchModel CreateCourseSearchModel(Course course)
        {
            return new CourseSearchModel
            {
                Id = course.Id,
                Name = course.Name,
                Code = course.Code,
                University = course.University?.UserName ?? "",
                UniversityId = course.University?.Id ?? -1,
                Professor = course.Professor,
                Image = course.Image,
                AverageRating = course.AverageRating
            };
        }

        private UserSearchModel CreateUserSearchModel(User user)
        {
            return new UserSearchModel
            {
                Id = user.Id,
                UserName = user.UserName,
                // Image = user.Image
                UserType = user.UserType,
            };
        }
    }
}
