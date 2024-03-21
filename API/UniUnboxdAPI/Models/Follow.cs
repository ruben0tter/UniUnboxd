namespace UniUnboxdAPI.Models
{
    public class Follow
    {
        public int FollowingStudentId { get; set; }
        public int FollowedStudentId { get; set; }
        public required Student FollowingStudent { get; set; }
        public required Student FollowedStudent { get; set; }
    }
}
