﻿using Microsoft.EntityFrameworkCore;
using System.Reflection.Metadata;
using UniUnboxdAPI.Models;

namespace UniUnboxdAPI.Data
{
    public class UniUnboxdDbContext : DbContext
    {
        public UniUnboxdDbContext(DbContextOptions<UniUnboxdDbContext> options) : base(options) { }

        public DbSet<User> Users { get; set; }
        public DbSet<Student> Students { get; set; }
        public DbSet<University> Universities { get; set; }
        public DbSet<Course> Courses { get; set; }
        public DbSet<Review> Reviews { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<User>().Ignore(i => i.NormalizedUserName);
            modelBuilder.Entity<User>().Ignore(i => i.NormalizedEmail);
            modelBuilder.Entity<User>().Ignore(i => i.EmailConfirmed);
            modelBuilder.Entity<User>().Ignore(i => i.SecurityStamp);
            modelBuilder.Entity<User>().Ignore(i => i.ConcurrencyStamp);
            modelBuilder.Entity<User>().Ignore(i => i.PhoneNumber);
            modelBuilder.Entity<User>().Ignore(i => i.PhoneNumberConfirmed);
            modelBuilder.Entity<User>().Ignore(i => i.TwoFactorEnabled);
            modelBuilder.Entity<User>().Ignore(i => i.LockoutEnd);
            modelBuilder.Entity<User>().Ignore(i => i.LockoutEnabled);
            modelBuilder.Entity<User>().Ignore(i => i.AccessFailedCount);

            modelBuilder.Entity<User>().UseTptMappingStrategy();
        }
    }
}