using Google;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;
using Moq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace UniUnboxdAPITests.TestUtilities
{
    internal class DatabaseUtil
    {
        public static UniUnboxdDbContext CreateDbContext()
            => new(new DbContextOptionsBuilder<UniUnboxdDbContext>().UseInMemoryDatabase("UniUnboxd").Options);

        // Source: https://github.com/dotnet/aspnetcore/blob/main/src/Identity/test/Shared/MockHelpers.cs
        public static UserManager<User> CreateUserManager(UniUnboxdDbContext db)
        {
            var store = new UserStore<User, IdentityRole<int>, UniUnboxdDbContext, int>(db);
            var options = new Mock<IOptions<IdentityOptions>>();
            var idOptions = new IdentityOptions();
            idOptions.Lockout.AllowedForNewUsers = false;
            options.Setup(o => o.Value).Returns(idOptions);
            var userValidators = new List<IUserValidator<User>>();
            var validator = new Mock<IUserValidator<User>>();
            userValidators.Add(validator.Object);
            var pwdValidators = new List<PasswordValidator<User>>();
            var userManager = new UserManager<User>(store, options.Object, new PasswordHasher<User>(),
                userValidators, pwdValidators, MockLookupNormalizer()!,
                new IdentityErrorDescriber(), null!,
                new Mock<ILogger<UserManager<User>>>().Object);
            validator.Setup(v => v.ValidateAsync(userManager, It.IsAny<User>()))
                .Returns(Task.FromResult(IdentityResult.Success)).Verifiable();
            return userManager;
        }

        private static ILookupNormalizer? MockLookupNormalizer()
        {
            var normalizerFunc = new Func<string, string>(i =>
            {
                if (i == null)
                    return null!;
                else
                    return Convert.ToBase64String(Encoding.UTF8.GetBytes(i)).ToUpperInvariant();
            });
            var lookupNormalizer = new Mock<ILookupNormalizer>();
            lookupNormalizer.Setup(i => i.NormalizeName(It.IsAny<string>())).Returns(normalizerFunc);
            lookupNormalizer.Setup(i => i.NormalizeEmail(It.IsAny<string>())).Returns(normalizerFunc);
            return lookupNormalizer.Object;
        }
    }
}
