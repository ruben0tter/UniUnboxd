using Microsoft.IdentityModel.Tokens;
using System.Security.Claims;
using UniUnboxdAPI.Models;
using UniUnboxdAPI.Models.DataTransferObjects;
using UniUnboxdAPI.Repositories;
using System.Collections.Generic;
using Microsoft.Extensions.Azure;
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
                Reviews = new List<Review>()
            };

        /// <summary>
        /// Get a CourseRetrievalModel by id of a course. It has (partially) the data connected to it. 
        /// </summary>
        /// <param name="id"> The id of the course to be adapted and returned.</param>
        /// <returns>An adapted object, corresponding to the course id provided.</returns>
        public async Task<CourseRetrievalModel> GetCourseRetrievalModelById(int id, int numOfReviews)
        {
            var course = await courseRepository.GetCourseAndConnectedData(id, numOfReviews);

            var courseRetrievalModel = CreateCourseRetrievalModel(course);

            if (course.Reviews.IsNullOrEmpty()) return courseRetrievalModel;
            
            foreach (var review in course.Reviews)
                courseRetrievalModel.Reviews.Add(CreateCourseReviewModel(review, course.Id));

            return courseRetrievalModel;
        }

        /// <summary>
        /// Gets the popular courses amongst all universities of the last 7 days.
        /// </summary>
        /// <returns>The popular courses of last week of all universities.</returns>
        public async Task<ICollection<CourseGridModel>> GetPopularCoursesOfLastWeek()
        {
            ICollection<Course> courses = await courseRepository.GetPopularCourseOfLastWeek();
            return CreateCourseGridModelCollection(courses);
        }


        /// <summary>
        /// Gets the popular courses amongst a specific university of the last 7 days.
        /// </summary>
        /// <param name="id"> The id of the university.</param>
        /// <returns>The popular courses of last week of the provided university.</returns>
        public async Task<ICollection<CourseGridModel>> GetPopularCoursesOfLastWeekByUniversity(int id)
        {
            ICollection<Course> courses = await courseRepository.GetPopularCourseOfLastWeekByUniversity(id);
            return CreateCourseGridModelCollection(courses);
        }

        private CourseRetrievalModel CreateCourseRetrievalModel(Course course)
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
        
        private CourseReviewModel CreateCourseReviewModel(Review review, int courseId) 
            => new ()
            {
                Id = review.Id,
                CourseId = courseId,
                Comment = review.Comment,
                IsAnonymous = review.IsAnonymous,
                Rating = review.Rating,
                Poster = CreateReviewPosterStudentModel(review)
            };

        private ReviewPosterStudentModel? CreateReviewPosterStudentModel(Review review)
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
            
        private ICollection<CourseGridModel> CreateCourseGridModelCollection(ICollection<Course> courses)
            => courses.Select(i => new CourseGridModel()
                {
                    Id = i.Id,
                    Name = i.Name,
                    Image = i.Image
                }).ToList();

        public async Task DeleteCourse(Course course)
            => await courseRepository.DeleteCourse(course);

        public async Task<Course> GetCourse(int id)
            => await courseRepository.GetCourse(id);

        public async Task UpdateCourse(Course course, CourseEditModel model)
        {
            course.Name = model.Name;
            course.Code = model.Code;
            course.Description = model.Description;
            course.Professor = model.Professor;
            course.Image = model.Image;
            course.Banner = model.Banner;
        }
        public async Task PutCourse(Course course)
            => await courseRepository.PutCourse(course);
        public async Task<bool> DoesProfessorExist(int professorId)
            => await userRepository.DoesProfessorExist(professorId);

        public async Task<ICollection<CourseGridModel>> GetAssignedCourses(int professorId)
        {
            ICollection<Course> courses = await courseRepository.GetAssignedCourses(professorId);
            return CreateCourseGridModelCollection(courses);
        }
    }
}
