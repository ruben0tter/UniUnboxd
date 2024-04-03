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
    public class AuthenticationService
    {
        private readonly UserManager<User> userManager;
        private readonly SignInManager<User> signInManager;
        private readonly UserRepository userRepository;

        public AuthenticationService(UserManager<User> userManager, SignInManager<User> signInManager, UserRepository userRepository)
        {
            this.userManager = userManager;
            this.signInManager = signInManager;
            this.userRepository = userRepository;
        }

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
            var result = await signInManager.CheckPasswordSignInAsync(user, model.Password, false);

            if (result.Succeeded)
                return JWTConfiguration.GenerateJWT(user);

            return null;
        }

        public async Task<User> GetUser(int id)
            => await userRepository.GetUser(id);

        public string UpdateToken(User user)
            => JWTConfiguration.GenerateJWT(user);
    }
}
