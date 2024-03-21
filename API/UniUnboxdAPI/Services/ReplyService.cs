using UniUnboxdAPI.Models;
using UniUnboxdAPI.Models.DataTransferObjects;
using UniUnboxdAPI.Models.DataTransferObjects.ReviewPage;
using UniUnboxdAPI.Repositories;

namespace UniUnboxdAPI.Services
{
    public class ReplyService
    {
        private readonly ReplyRepository replyRepository;
        private readonly ReviewRepository reviewRepository;
        private readonly UserRepository userRepository;
        private readonly MailService mailService;

        public ReplyService(ReplyRepository replyRepository, ReviewRepository reviewRepository, UserRepository userRepository, MailService mailService) 
        { 
            this.replyRepository = replyRepository;
            this.reviewRepository = reviewRepository;
            this.userRepository = userRepository;
            this.mailService = mailService;
        }

        /// <summary>
        /// Check whether there exists a student with the provided id.
        /// </summary>
        /// <param name="studentId">Provided student id.</param>
        /// <returns>Whether there exists a student with the provided id.</returns>
        public async Task<bool> DoesUserExist(int userId)
            => await userRepository.DoesStudentExist(userId) ||
                await userRepository.DoesProfessorExist(userId);

        /// <summary>
        /// Check whether there exists a review with the provided id.
        /// </summary>
        /// <param name="reviewId">Provided review id.</param>
        /// <returns>Whether there exists a review with the provided id.</returns>
        public async Task<bool> DoesReviewExist(int reviewId)
            => await reviewRepository.DoesReviewExist(reviewId);

        /// <summary>
        /// Creates a Reply object with the given ReplyModel.
        /// </summary>
        /// <param name="model">Reply information.</param>
        /// <param name="userId">Id of attached user.</param>
        /// <returns>Created Reply object.</returns>
        public async Task<Reply> CreateReply(ReplyModel model, int userId)
            => new()
            {
                CreationTime = DateTime.Now,
                LastModificationTime = DateTime.Now,
                Text = model.Text,
                User = await userRepository.GetUser(userId),
                Review = await reviewRepository.GetReview(model.ReviewId)
            };

        /// <summary>
        /// Posts the provided reply.
        /// </summary>
        /// <param name="reply">Provided reply.</param>
        /// <returns>No object or value is returned by this method when it completes.</returns>
        public async Task PostReply(Reply reply)
            => await replyRepository.PostReply(reply);

        /// <summary>
        /// Notifies user who made a review that a reply has been posted to it. 
        /// </summary>
        /// <param name="reply">Provided reply.</param>
        /// <returns>No object or value is returned by this method when it completes.</returns>
        public async Task NotifyReviewAuthor(Reply reply)
        {
            if (reply.Review.Student.NotificationSettings.ReceivesNewReplyMail)
            {
                mailService.NewReplyMail(reply);
            }
        }


        /// <summary>
        /// Creates a ReviewReplyModel object with the given Reply.
        /// </summary>
        /// <param name="model">Reply information.</param>
        /// <returns>Created Reply object.</returns>
        public ReviewReplyModel CreateReviewReplyModel(Reply model)
            => new()
            {
                Text = model.Text,
                UserHeader = new UserHeaderModel()
                {
                    Id = model.User.Id,
                    Name = model.User.UserName,
                    Image = model.User is Student ? (model.User as Student).Image :
                            (model.User as Professor).Image
                }
            };
    }
}
