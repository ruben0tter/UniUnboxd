using Microsoft.IdentityModel.Tokens;
using System.Security.Claims;
using UniUnboxdAPI.Models;
using UniUnboxdAPI.Models.DataTransferObjects;
using UniUnboxdAPI.Repositories;
using UniUnboxdAPI.Utilities;

namespace UniUnboxdAPI.Services
{
    public class CourseService
    {
        private readonly CourseRepository courseRepository;
        private readonly UserRepository userRepository;

        public CourseService(CourseRepository courseRepository, UserRepository userRepository)
        {
            this.courseRepository = courseRepository;
            this.userRepository = userRepository;
        }
        
        /// <summary>
        /// Checks whether the id contained in the JWT ligns up with the provided id.
        /// </summary>
        /// <param name="identity">Claims contained in the JWT</param>
        /// <param name="id">Provided id</param>
        /// <returns>Whether the id contained in the JWT is equal to the provided id.</returns>
        public bool IsUserValidated(ClaimsIdentity? identity, int id)
            => JWTValidation.IsUserValidated(identity, id);
        
        /// <summary>
        /// Check whether there exists a student with the provided id.
        /// </summary>
        /// <param name="universityId">Provided university Id.</param>
        /// <returns>Whether there exists a student with the provided id.</returns>
        public async Task<bool> DoesUniversityExist(int universityId)
            => await userRepository.DoesUniversityExist(universityId);

        /// <summary>
        /// Add a course to the database.
        /// </summary>
        /// <param name="course"> The course to be added.</param>
        public async Task PostCourse(Course course)
        {
            await courseRepository.PostCourse(course);
        }

        /// <summary>
        /// Creates a Course object from the given CourseCreationModel creationModel.
        /// </summary>
        /// <param name="creationModel"> Course information.</param>
        /// <returns>The created Course object.</returns>
        public async Task<Course> CreateCourse(CourseCreationModel creationModel)
            => new()
            {
                Name = creationModel.Name,
                CreationTime = DateTime.Now,
                LastModificationTime = DateTime.Now,
                Code = creationModel.Code,
                Description = creationModel.Description,
                Professor = creationModel.Professor,
                Image = creationModel.Image,
                Banner = creationModel.Banner,
                University = await userRepository.GetUniversity(creationModel.UniversityId),
                Reviews = null
            };

        /// <summary>
        /// Get a CourseRetrievalModel by id of a course. It has (partially) the data connected to it. 
        /// </summary>
        /// <param name="id"> The id of the course to be adapted and returned.</param>
        /// <returns>An adapted object, corresponding to the course id provided.</returns>
        public async Task<CourseRetrievalModel> GetCourseRetrievalModelById(int id)
        {
            var course = await courseRepository.GetCourseAndConnectedData(id, 1);

            var courseRetrievalModel = CreateCourseRetrievalModel(course);

            if (course.Reviews.IsNullOrEmpty()) return courseRetrievalModel;
            
            foreach (var review in course.Reviews)
                courseRetrievalModel.Reviews.Add(CreateCourseReviewModel(review, course.Id));

            return courseRetrievalModel;
        }

        private static CourseRetrievalModel CreateCourseRetrievalModel(Course course)
            => new ()
            {
                Id = course.Id,
                Code = course.Code,
                Banner = course.Banner,
                Description = course.Description,
                Image = course.Image,
                Name = course.Name,
                Professor = course.Professor,
                Reviews = new List<CourseReviewModel>(),
                UniversityId = course.University.Id,
                UniversityName = course.University.UserName
            };
        
        private static CourseReviewModel CreateCourseReviewModel(Review review, int courseId) 
            => new ()
            {
                Id = review.Id,
                CourseId = courseId,
                Comment = review.Comment,
                IsAnonymous = review.IsAnonymous,
                Rating = review.Rating,
                Poster = CreateReviewPosterStudentModel(review)
            };

        private static ReviewPosterStudentModel? CreateReviewPosterStudentModel(Review review)
            => review.IsAnonymous
                ? null
                : new ReviewPosterStudentModel()
                {
                    Id = review.Student.Id,
                    Image = review.Student.Image,
                    UserName = review.Student.UserName
                };

        public async Task<bool> DoesCourseExist(int id)
            => await courseRepository.DoesCourseExist(id);
    }
}
