namespace UniUnboxdAPI.Models.DataTransferObjects.UniversityHomePage
{
    public class CourseOverviewModel
    {
        public required int Id { get; set; }
        public required string Name { get; set; }
        public required string Code { get; set; }
        public required string Professor { get; set; }
        public string? Image { get; set; }
    }
}
