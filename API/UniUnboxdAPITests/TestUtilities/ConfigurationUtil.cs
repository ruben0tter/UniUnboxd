using Microsoft.Extensions.Configuration;

namespace UniUnboxdAPITests.TestUtilities
{
    internal class ConfigurationUtil
    {
        public static IConfiguration CreateConfiguration()
        {
            var config = new List<KeyValuePair<string, string?>>
            {
                new("JWTKey", Guid.NewGuid().ToString())
            };

            return new ConfigurationBuilder()
                .AddInMemoryCollection(config)
                .Build();
        }
    }
}
