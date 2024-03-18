using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;

namespace UniUnboxdAPI.Utilities
{
    public static class JWTValidation
    {
        /// <summary>
        /// Checks whether the id contained in the JWT ligns up with the provided id.
        /// </summary>
        /// <param name="identity">Claims contained in the JWT</param>
        /// <param name="id">Provided id</param>
        /// <returns>Whether the id contained in the JWT is equal to the provided id.</returns>
        public static bool IsUserValidated(ClaimsIdentity? identity, int id)
        {
            if (identity == null) return false;

            var claim = identity.FindFirst(ClaimTypes.NameIdentifier);

            if (claim == null) return false;

            var userId = claim.Value;

            return userId.Equals(id.ToString());
        }

        /// <summary>
        /// Get the id contained in the provided Claims from the JWT.
        /// </summary>
        /// <param name="identity">Claims contained in the JWT</param>
        /// <returns>The id of the user.</returns>
        public static int GetUserId(ClaimsIdentity? identity)
        {
            if (identity == null) return 0;

            var claim = identity.FindFirst(ClaimTypes.NameIdentifier);

            if (claim == null) return 0;

            return int.Parse(claim.Value);
        }
    }
}
