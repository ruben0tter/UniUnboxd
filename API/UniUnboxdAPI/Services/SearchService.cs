using UniUnboxdAPI.Models;
using UniUnboxdAPI.Models.DataTransferObjects;
using UniUnboxdAPI.Repositories;

namespace UniUnboxdAPI.Services
{
    public class SearchService(SearchRepository searchRepository, UserRepository userRepository)
    {

        public async Task<List<CourseSearchModel>> GetCourses(SearchOptions options) {
            Func<SearchOptions, Task<List<Course>>> GetRelevantCourses;

            if (options.UniversityId.HasValue) {
                GetRelevantCourses = searchRepository.GetCoursesFromUni;
            } else {
                GetRelevantCourses = searchRepository.GetCourses;
            }

            List<Course> courses = await GetRelevantCourses(options);

            return courses.Select(CreateCourseSearchModel).ToList();
        }

        public async Task<List<UserSearchModel>> GetUsers(SearchOptions options) {
            var users = await searchRepository.GetUsers(options);

            var result = new List<UserSearchModel>();
            foreach (User user in users)
                result.Add(await CreateUserSearchModel(user));

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
                AnonymousRating = course.AnonymousRating,
                NonanonymousRating = course.NonanonymousRating
            };
        }

        private async Task<UserSearchModel> CreateUserSearchModel(User user)
        {
            string? image = await userRepository.GetImageOf(user.Id, user.UserType);

            return new UserSearchModel
            {
                Id = user.Id,
                UserName = user.UserName!,
                Image = image,
                UserType = user.UserType,
            };
        }
    }
}
