using Microsoft.EntityFrameworkCore;
using UniUnboxdAPI.Models;

namespace UniUnboxdAPI.Data
{
    /// <summary>
    /// Represents the session with the database allowing query and save instances of entities.
    /// </summary>
    public class UniUnboxdDbContext(DbContextOptions<UniUnboxdDbContext> options) : DbContext(options)
    {
        // Database tables.
        public DbSet<User> Users { get; set; }
        public DbSet<Student> Students { get; set; }
        public DbSet<University> Universities { get; set; }
        public DbSet<Professor> Professors { get; set; }
        public DbSet<Course> Courses { get; set; }
        public DbSet<Review> Reviews { get; set; }
        public DbSet<Reply> Replies { get; set; }
        public DbSet<VerificationApplication> Applications { get; set; }
        public DbSet<Follow> Follows { get; set; }
        public DbSet<CourseProfessorAssignment> CourseProfessorAssignments { get; set; }
        public DbSet<NotificationSettings> NotificationSettings { get; set; }
        public DbSet<Like> Likes { get; set; }

        /// <summary>
        /// Configures the schema needed for the model when the model is created.
        /// </summary>
        /// <param name="modelBuilder">Provides a simple API surface for configuring a model that maps to a database.</param>
        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            // Ignore properties in User inherited from IdentityUser that are not used
            modelBuilder.Entity<User>().Ignore(i => i.EmailConfirmed).Ignore(i => i.ConcurrencyStamp)
                .Ignore(i => i.PhoneNumber).Ignore(i => i.PhoneNumberConfirmed)
                .Ignore(i => i.TwoFactorEnabled).Ignore(i => i.LockoutEnd)
                .Ignore(i => i.LockoutEnabled).Ignore(i => i.AccessFailedCount);

            // Use TPT mapping strategy for User inheritance
            modelBuilder.Entity<User>().UseTptMappingStrategy();

            // Configuring many-to-many relationship for Follows
            // Specify key
            modelBuilder.Entity<Follow>()
               .HasKey(i => new { i.FollowingStudentId, i.FollowedStudentId });

            // Following
            modelBuilder.Entity<Follow>()
                .HasOne(i => i.FollowingStudent)
                .WithMany(i => i.Following)
                .HasForeignKey(i => i.FollowingStudentId)
                .OnDelete(DeleteBehavior.Cascade);

            // Followers
            modelBuilder.Entity<Follow>()
                .HasOne(i => i.FollowedStudent)
                .WithMany(i => i.Followers)
                .HasForeignKey(i => i.FollowedStudentId)
                .OnDelete(DeleteBehavior.Cascade);

            // Configuring many-to-many relationship for CourseProfessorAssignment
            // Specify key
            modelBuilder.Entity<CourseProfessorAssignment>()
                .HasKey(i => new { i.ProfessorId, i.CourseId });

            // Professor-Side
            modelBuilder.Entity<CourseProfessorAssignment>()
                .HasOne(i => i.Professor)
                .WithMany(i => i.AssignedCourses)
                .HasForeignKey(i => i.ProfessorId)
                .OnDelete(DeleteBehavior.Cascade);

            // Course-Side
            modelBuilder.Entity<CourseProfessorAssignment>()
                .HasOne(i => i.Course)
                .WithMany(i => i.AssignedProfessors)
                .HasForeignKey(i => i.CourseId);

            // Configuring one-to-one relationship for NotificationSettings
            // Specify key
            modelBuilder.Entity<NotificationSettings>()
               .HasKey(i => i.StudentId);

            // Student-side
            modelBuilder.Entity<NotificationSettings>()
                .HasOne(i => i.Student)
                .WithOne(i => i.NotificationSettings)
                .HasForeignKey<NotificationSettings>(i => i.StudentId)
                .OnDelete(DeleteBehavior.Cascade);

            // Configuring many-to-many relationship for Likes
            modelBuilder.Entity<Like>()
                .HasKey(i => new { i.ReviewId, i.StudentId });

            // Review-side
            modelBuilder.Entity<Like>()
                .HasOne(i => i.Review)
                .WithMany(i => i.Likes)
                .HasForeignKey(i => i.ReviewId)
                .OnDelete(DeleteBehavior.Cascade);

            // Student-side
            modelBuilder.Entity<Like>()
                .HasOne(i => i.Student)
                .WithMany(i => i.Likes)
                .HasForeignKey(i => i.StudentId)
                .OnDelete(DeleteBehavior.Cascade);
        }
    }
}
