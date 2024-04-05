namespace UniUnboxdAPI.Models.DataTransferObjects
{
    // Define the BasicCourseRetrievalHome class
    public class BasicCourseRetrievalHome
    {
        // Define the Id property of type int
        public required int Id { get; set; }

        // Define the CourseImage property of type string
        public required string? CourseImage { get; set; }

        // Define the CourseName property of type string
        public required string? CourseName { get; set; }
    }
}
