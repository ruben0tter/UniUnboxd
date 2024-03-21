using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Hosting;
using UniUnboxdAPI.Models;

namespace UniUnboxdAPI.Data
{
    public class UniUnboxdDbContext : DbContext
    {
        public UniUnboxdDbContext(DbContextOptions<UniUnboxdDbContext> options) : base(options) { }

        public DbSet<User> Users { get; set; }
        public DbSet<Student> Students { get; set; }
        public DbSet<University> Universities { get; set; }
        public DbSet<Professor> Professors { get; set; }
        public DbSet<Course> Courses { get; set; }
        public DbSet<Review> Reviews { get; set; }
        public DbSet<Reply> Replies { get; set; }
        public DbSet<VerificationApplication> Applications { get; set; }
        public DbSet<Follow> Follows { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<User>().Ignore(i => i.EmailConfirmed);
            modelBuilder.Entity<User>().Ignore(i => i.ConcurrencyStamp);
            modelBuilder.Entity<User>().Ignore(i => i.PhoneNumber);
            modelBuilder.Entity<User>().Ignore(i => i.PhoneNumberConfirmed);
            modelBuilder.Entity<User>().Ignore(i => i.TwoFactorEnabled);
            modelBuilder.Entity<User>().Ignore(i => i.LockoutEnd);
            modelBuilder.Entity<User>().Ignore(i => i.LockoutEnabled);
            modelBuilder.Entity<User>().Ignore(i => i.AccessFailedCount);

            modelBuilder.Entity<User>().UseTptMappingStrategy();

            modelBuilder.Entity<Follow>()
               .HasKey(i => new { i.FollowingStudentId, i.FollowedStudentId });

            modelBuilder.Entity<Follow>()
                .HasOne(i => i.FollowingStudent)
                .WithMany(i => i.Following)
                .HasForeignKey(i => i.FollowingStudentId)
                .OnDelete(DeleteBehavior.Cascade);

            modelBuilder.Entity<Follow>()
                .HasOne(i => i.FollowedStudent)
                .WithMany(i => i.Followers)
                .HasForeignKey(i => i.FollowedStudentId)
                .OnDelete(DeleteBehavior.Cascade);
        }
    }
}
