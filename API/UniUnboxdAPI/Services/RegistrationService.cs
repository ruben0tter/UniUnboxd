using Microsoft.AspNetCore.Identity;
using System.Net.Mail;
using UniUnboxdAPI.Models;
using UniUnboxdAPI.Models.DataTransferObjects;

namespace UniUnboxdAPI.Services
{
    /// <summary>
    /// Service for the RegistrationController.
    /// Handles the registration logic.
    /// </summary>
    public class RegistrationService
    {
        private readonly UserManager<User> userManager;

        public RegistrationService(UserManager<User> userManager)
        {
            this.userManager = userManager;
        }

        /// <summary>
        /// Creates a User object with the given RegisterModel, 
        /// of the correct type based on their role.
        /// </summary>
        /// <param name="model">User registration information.</param>
        /// <returns>Created User object.</returns>
        public User CreateUser(RegisterModel model)
        {
            return model.Type switch
            {
                UserType.University => CreateUserModel<University>(model),
                _ => CreateUserModel<Student>(model)
            };
        }

        /// <summary>
        /// Checks whether the provided email is a valid email.
        /// </summary>
        /// <param name="email">Provided email.</param>
        /// <returns>Whether the email is valid.</returns>
        public bool IsEmailValid(string email)
        {
            try {
                var addr = new MailAddress(email);
                return addr.Address == email;
            } catch {
                return false;
            }
        }

        /// <summary>
        /// Checks whether there already is a user with the provided email.
        /// </summary>
        /// <param name="email">Provided email.</param>
        /// <returns>Whether the email is unused.</returns>
        public async Task<bool> DoesEmailExist(string email)
        {
            var user = await userManager.FindByEmailAsync(email);
            return user != null;
        }

        /// <summary>
        /// Creates a User database model using the Identity library.
        /// </summary>
        /// <param name="user">Provided User object.</param>
        /// <param name="password">Provided password.</param>
        /// <returns>Whether the user was succesfully created in the database.</returns>
        public async Task<bool> CreateAccount(User user, string password)
        {
            var result = await userManager.CreateAsync(user, password);
            return result.Succeeded;
        }

        /// <summary>
        /// Creates a subclass of User object with the given RegisterModel.
        /// </summary>
        /// <typeparam name="T">Subclass type that implements User.</typeparam>
        /// <param name="model">RegisterModel with the given information.</param>
        /// <returns>The created User model.</returns>
        private static T CreateUserModel<T>(RegisterModel model) where T : User, new()
            => new()
            {
                Email = model.Email,
                UserName = CreateUserName(model.Email),
                CreationTime = DateTime.Now,
                LastModificationTime = DateTime.Now,
                UserType = model.Type,
                SecurityStamp = Guid.NewGuid().ToString()
            };

        /// <summary>
        /// Creates a username based on the provided email.
        /// </summary>
        /// <param name="email">Provided email.</param>
        /// <returns>Everything before the char '@'.</returns>
        private static string CreateUserName(string email)
        {
            int splitIndex = email.IndexOf('@');
            return email[..splitIndex];
        }
    }
}
