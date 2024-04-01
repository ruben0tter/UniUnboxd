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
                if (options.Start.HasValue && options.Count.HasValue) {
                    GetRelevantCourses = searchRepository.GetCoursesFromUni;
                } else {
                    GetRelevantCourses = searchRepository.GetAllCoursesFromUni;
                }
            } else {
                if (options.Start.HasValue && options.Count.HasValue) {
                    GetRelevantCourses = searchRepository.GetCourses;
                } else {
                    GetRelevantCourses = searchRepository.GetAllCourses;
                }
            }

            List<Course> courses = await GetRelevantCourses(options);

            return courses.Select(CreateCourseSearchModel).ToList();
        }

        public async Task<List<UserSearchModel>> GetUsers(SearchOptions options) {
            List<User> users;
            if (options.Start.HasValue && options.Count.HasValue) {
                users = await searchRepository.GetUsers(options);
            } else {
                users = await searchRepository.GetAllUsers(options);
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
                AnonymousRating = course.AnonymousRating,
                NonanonymousRating = course.NonanonymousRating
            };
        }

        private async Task<UserSearchModel> CreateUserSearchModel(User user)
        {
            String? image = await userRepository.GetImageOf(user.Id, user.UserType);

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
