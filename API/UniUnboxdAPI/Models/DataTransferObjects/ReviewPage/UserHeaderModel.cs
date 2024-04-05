namespace UniUnboxdAPI.Models.DataTransferObjects.ReviewPage
{
    //  UserHeaderModel class represents the data transfer object for the user header in the review page.
    public class UserHeaderModel
    {
        // Id property represents the unique identifier of the user.
        public required int Id { get; set; }

        // Name property represents the name of the user.
        public required string Name { get; set; }

        // Image property represents the image URL of the user.
        public required string Image { get; set; }
    }
}
