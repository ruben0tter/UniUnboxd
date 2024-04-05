using Microsoft.AspNetCore.Identity;
using UniUnboxdAPI.Models;
using UniUnboxdAPI.Models.DataTransferObjects;
using UniUnboxdAPI.Repositories;
using UniUnboxdAPI.Utilities;

namespace UniUnboxdAPI.Services
{
    /// <summary>
    /// Service for the AuthenticationController.
    /// Handles the authentication logic.
    /// </summary>
    public class AuthenticationService(UserManager<User> userManager, UserRepository userRepository)
    {

        /// <summary>
        /// Check whether there exists a user in the database with the provided email.
        /// </summary>
        /// <param name="email">Provided email.</param>
        /// <returns>Whether there exists a user with the provided email.</returns>
        public async Task<bool> DoesEmailExist(string email)
            => await userManager.FindByEmailAsync(email) != null;

        /// <summary>
        /// Authenticates a user by checking the provided password with user.
        /// Then it creates a JWT to be returned as payload.
        /// </summary>
        /// <param name="model">Provided email and password.</param>
        /// <returns>Created JWT based on user if authentication succeeded, otherwise null.</returns>
        public async Task<string?> Authenticate(AuthenticationModel model)
        {
            var user = await userManager.FindByEmailAsync(model.Email);
            var isPasswordCorrect = await userManager.CheckPasswordAsync(user!, model.Password);

            if (isPasswordCorrect)
                return JWTConfiguration.GenerateJWT(user!);

            return null;
        }

        public async Task<User> GetUser(int id)
            => await userRepository.GetUser(id);

        public string UpdateToken(User user)
            => JWTConfiguration.GenerateJWT(user);
    }
}
