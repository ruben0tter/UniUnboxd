using UniUnboxdAPI.Models;
using UniUnboxdAPI.Models.DataTransferObjects;
using UniUnboxdAPI.Repositories;

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
            List<User> users;
            if (options.Start.HasValue && options.Count.HasValue) {
                users = await searchRepository.GetUsers(options.Search, options.Start.Value, options.Count.Value);
            } else {
                users = await searchRepository.GetAllUsers(options.Search);
            }

            List<UserSearchModel> result = [];
            foreach (User user in users)
            {
                result.Add(await CreateUserSearchModel(user));
            }
            return result;
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

        private async Task<UserSearchModel> CreateUserSearchModel(User user)
        {
            String image = await searchRepository.GetImageOf(user.Id, user.UserType);

            return new UserSearchModel
            {
                Id = user.Id,
                UserName = user.UserName,
                Image = image,
                UserType = user.UserType,
            };
        }
    }
}
