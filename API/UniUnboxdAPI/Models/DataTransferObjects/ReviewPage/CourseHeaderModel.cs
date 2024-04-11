namespace UniUnboxdAPI.Models.DataTransferObjects.ReviewPage
{
    // Represents the model for the header of a course in the review page.
    public class CourseHeaderModel
    {
        // The unique identifier of the course.
        public required int Id { get; set; }

        // The name of the course.
        public required string Name { get; set; }

        // The code of the course.
        public required string Code { get; set; }

        // The image associated with the course.
        public required string Image { get; set; }

        // The banner image associated with the course.
        public required string Banner { get; set; }
    }
}
