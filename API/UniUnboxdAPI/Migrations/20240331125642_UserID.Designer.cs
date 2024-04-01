﻿// <auto-generated />
using System;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Infrastructure;
using Microsoft.EntityFrameworkCore.Metadata;
using Microsoft.EntityFrameworkCore.Migrations;
using Microsoft.EntityFrameworkCore.Storage.ValueConversion;
using UniUnboxdAPI.Data;

#nullable disable

namespace UniUnboxdAPI.Migrations
{
    [DbContext(typeof(UniUnboxdDbContext))]
    [Migration("20240331125642_UserID")]
    partial class UserID
    {
        /// <inheritdoc />
        protected override void BuildTargetModel(ModelBuilder modelBuilder)
        {
#pragma warning disable 612, 618
            modelBuilder
                .HasAnnotation("ProductVersion", "8.0.2")
                .HasAnnotation("Relational:MaxIdentifierLength", 64);

            MySqlModelBuilderExtensions.AutoIncrementColumns(modelBuilder);

            modelBuilder.Entity("UniUnboxdAPI.Models.Course", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int");

                    MySqlPropertyBuilderExtensions.UseMySqlIdentityColumn(b.Property<int>("Id"));

                    b.Property<double>("AverageRating")
                        .HasColumnType("double");

                    b.Property<string>("Banner")
                        .HasColumnType("longtext");

                    b.Property<string>("Code")
                        .IsRequired()
                        .HasColumnType("longtext");

                    b.Property<DateTime>("CreationTime")
                        .HasColumnType("datetime(6)");

                    b.Property<string>("Description")
                        .IsRequired()
                        .HasColumnType("longtext");

                    b.Property<string>("Image")
                        .HasColumnType("longtext");

                    b.Property<DateTime>("LastModificationTime")
                        .HasColumnType("datetime(6)");

                    b.Property<string>("Name")
                        .IsRequired()
                        .HasColumnType("longtext");

                    b.Property<string>("Professor")
                        .IsRequired()
                        .HasColumnType("longtext");

                    b.Property<int?>("ProfessorId")
                        .HasColumnType("int");

                    b.Property<int>("UniversityId")
                        .HasColumnType("int");

                    b.HasKey("Id");

                    b.HasIndex("ProfessorId");

                    b.HasIndex("UniversityId");

                    b.ToTable("Courses");
                });

            modelBuilder.Entity("UniUnboxdAPI.Models.Follow", b =>
                {
                    b.Property<int>("FollowingStudentId")
                        .HasColumnType("int");

                    b.Property<int>("FollowedStudentId")
                        .HasColumnType("int");

                    b.HasKey("FollowingStudentId", "FollowedStudentId");

                    b.HasIndex("FollowedStudentId");

                    b.ToTable("Follows");
                });

            modelBuilder.Entity("UniUnboxdAPI.Models.Like", b =>
                {
                    b.Property<int>("ReviewId")
                        .HasColumnType("int");

                    b.Property<int>("StudentId")
                        .HasColumnType("int");

                    b.HasKey("ReviewId", "StudentId");

                    b.HasIndex("StudentId");

                    b.ToTable("Likes");
                });

            modelBuilder.Entity("UniUnboxdAPI.Models.NotificationSettings", b =>
                {
                    b.Property<int>("StudentId")
                        .HasColumnType("int");

                    b.Property<bool>("ReceivesFollowersReviewMail")
                        .HasColumnType("tinyint(1)");

                    b.Property<bool>("ReceivesFollowersReviewPush")
                        .HasColumnType("tinyint(1)");

                    b.Property<bool>("ReceivesNewFollowerMail")
                        .HasColumnType("tinyint(1)");

                    b.Property<bool>("ReceivesNewFollowerPush")
                        .HasColumnType("tinyint(1)");

                    b.Property<bool>("ReceivesNewReplyMail")
                        .HasColumnType("tinyint(1)");

                    b.Property<bool>("ReceivesNewReplyPush")
                        .HasColumnType("tinyint(1)");

                    b.HasKey("StudentId");

                    b.ToTable("NotificationSettings");
                });

            modelBuilder.Entity("UniUnboxdAPI.Models.Reply", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int");

                    MySqlPropertyBuilderExtensions.UseMySqlIdentityColumn(b.Property<int>("Id"));

                    b.Property<DateTime>("CreationTime")
                        .HasColumnType("datetime(6)");

                    b.Property<DateTime>("LastModificationTime")
                        .HasColumnType("datetime(6)");

                    b.Property<int?>("ProfessorId")
                        .HasColumnType("int");

                    b.Property<int>("ReviewId")
                        .HasColumnType("int");

                    b.Property<int?>("StudentId")
                        .HasColumnType("int");

                    b.Property<string>("Text")
                        .IsRequired()
                        .HasColumnType("longtext");

                    b.Property<int>("UserId")
                        .HasColumnType("int");

                    b.HasKey("Id");

                    b.HasIndex("ProfessorId");

                    b.HasIndex("ReviewId");

                    b.HasIndex("StudentId");

                    b.HasIndex("UserId");

                    b.ToTable("Replies");
                });

            modelBuilder.Entity("UniUnboxdAPI.Models.Review", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int");

                    MySqlPropertyBuilderExtensions.UseMySqlIdentityColumn(b.Property<int>("Id"));

                    b.Property<string>("Comment")
                        .IsRequired()
                        .HasColumnType("longtext");

                    b.Property<int>("CourseId")
                        .HasColumnType("int");

                    b.Property<DateTime>("CreationTime")
                        .HasColumnType("datetime(6)");

                    b.Property<bool>("IsAnonymous")
                        .HasColumnType("tinyint(1)");

                    b.Property<DateTime>("LastModificationTime")
                        .HasColumnType("datetime(6)");

                    b.Property<double>("Rating")
                        .HasColumnType("double");

                    b.Property<int>("StudentId")
                        .HasColumnType("int");

                    b.HasKey("Id");

                    b.HasIndex("CourseId");

                    b.HasIndex("StudentId");

                    b.ToTable("Reviews");
                });

            modelBuilder.Entity("UniUnboxdAPI.Models.User", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int");

                    MySqlPropertyBuilderExtensions.UseMySqlIdentityColumn(b.Property<int>("Id"));

                    b.Property<DateTime>("CreationTime")
                        .HasColumnType("datetime(6)");

                    b.Property<string>("Email")
                        .HasColumnType("longtext");

                    b.Property<DateTime>("LastModificationTime")
                        .HasColumnType("datetime(6)");

                    b.Property<string>("NormalizedEmail")
                        .HasColumnType("longtext");

                    b.Property<string>("NormalizedUserName")
                        .HasColumnType("longtext");

                    b.Property<string>("PasswordHash")
                        .HasColumnType("longtext");

                    b.Property<string>("SecurityStamp")
                        .HasColumnType("longtext");

                    b.Property<int>("UniversityId")
                        .HasColumnType("int");

                    b.Property<string>("UserName")
                        .HasColumnType("longtext");

                    b.Property<int>("UserType")
                        .HasColumnType("int");

                    b.Property<int>("VerificationStatus")
                        .HasColumnType("int");

                    b.HasKey("Id");

                    b.ToTable("Users");

                    b.UseTptMappingStrategy();
                });

            modelBuilder.Entity("UniUnboxdAPI.Models.VerificationApplication", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("int");

                    MySqlPropertyBuilderExtensions.UseMySqlIdentityColumn(b.Property<int>("Id"));

                    b.Property<DateTime>("CreationTime")
                        .HasColumnType("datetime(6)");

                    b.Property<DateTime>("LastModificationTime")
                        .HasColumnType("datetime(6)");

                    b.Property<int?>("TargetUniversityId")
                        .HasColumnType("int");

                    b.Property<int>("UserId")
                        .HasColumnType("int");

                    b.Property<string>("VerificationData")
                        .IsRequired()
                        .HasColumnType("longtext");

                    b.HasKey("Id");

                    b.HasIndex("TargetUniversityId");

                    b.ToTable("Applications");
                });

            modelBuilder.Entity("UniUnboxdAPI.Models.Professor", b =>
                {
                    b.HasBaseType("UniUnboxdAPI.Models.User");

                    b.Property<string>("Image")
                        .HasColumnType("longtext");

                    b.ToTable("Professors");
                });

            modelBuilder.Entity("UniUnboxdAPI.Models.Student", b =>
                {
                    b.HasBaseType("UniUnboxdAPI.Models.User");

                    b.Property<string>("DeviceToken")
                        .HasColumnType("longtext");

                    b.Property<string>("Image")
                        .HasColumnType("longtext");

                    b.HasIndex("UniversityId");

                    b.ToTable("Students");
                });

            modelBuilder.Entity("UniUnboxdAPI.Models.University", b =>
                {
                    b.HasBaseType("UniUnboxdAPI.Models.User");

                    b.ToTable("Universities");
                });

            modelBuilder.Entity("UniUnboxdAPI.Models.Course", b =>
                {
                    b.HasOne("UniUnboxdAPI.Models.Professor", null)
                        .WithMany("AssignedCourses")
                        .HasForeignKey("ProfessorId");

                    b.HasOne("UniUnboxdAPI.Models.University", "University")
                        .WithMany("Courses")
                        .HasForeignKey("UniversityId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("University");
                });

            modelBuilder.Entity("UniUnboxdAPI.Models.Follow", b =>
                {
                    b.HasOne("UniUnboxdAPI.Models.Student", "FollowedStudent")
                        .WithMany("Followers")
                        .HasForeignKey("FollowedStudentId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("UniUnboxdAPI.Models.Student", "FollowingStudent")
                        .WithMany("Following")
                        .HasForeignKey("FollowingStudentId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("FollowedStudent");

                    b.Navigation("FollowingStudent");
                });

            modelBuilder.Entity("UniUnboxdAPI.Models.Like", b =>
                {
                    b.HasOne("UniUnboxdAPI.Models.Review", "Review")
                        .WithMany("Likes")
                        .HasForeignKey("ReviewId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("UniUnboxdAPI.Models.Student", "Student")
                        .WithMany("Likes")
                        .HasForeignKey("StudentId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("Review");

                    b.Navigation("Student");
                });

            modelBuilder.Entity("UniUnboxdAPI.Models.NotificationSettings", b =>
                {
                    b.HasOne("UniUnboxdAPI.Models.Student", "Student")
                        .WithOne("NotificationSettings")
                        .HasForeignKey("UniUnboxdAPI.Models.NotificationSettings", "StudentId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("Student");
                });

            modelBuilder.Entity("UniUnboxdAPI.Models.Reply", b =>
                {
                    b.HasOne("UniUnboxdAPI.Models.Professor", null)
                        .WithMany("Replies")
                        .HasForeignKey("ProfessorId");

                    b.HasOne("UniUnboxdAPI.Models.Review", "Review")
                        .WithMany("Replies")
                        .HasForeignKey("ReviewId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("UniUnboxdAPI.Models.Student", null)
                        .WithMany("Replies")
                        .HasForeignKey("StudentId");

                    b.HasOne("UniUnboxdAPI.Models.User", "User")
                        .WithMany()
                        .HasForeignKey("UserId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("Review");

                    b.Navigation("User");
                });

            modelBuilder.Entity("UniUnboxdAPI.Models.Review", b =>
                {
                    b.HasOne("UniUnboxdAPI.Models.Course", "Course")
                        .WithMany("Reviews")
                        .HasForeignKey("CourseId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("UniUnboxdAPI.Models.Student", "Student")
                        .WithMany("Reviews")
                        .HasForeignKey("StudentId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("Course");

                    b.Navigation("Student");
                });

            modelBuilder.Entity("UniUnboxdAPI.Models.VerificationApplication", b =>
                {
                    b.HasOne("UniUnboxdAPI.Models.University", "TargetUniversity")
                        .WithMany()
                        .HasForeignKey("TargetUniversityId");

                    b.Navigation("TargetUniversity");
                });

            modelBuilder.Entity("UniUnboxdAPI.Models.Professor", b =>
                {
                    b.HasOne("UniUnboxdAPI.Models.User", null)
                        .WithOne()
                        .HasForeignKey("UniUnboxdAPI.Models.Professor", "Id")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();
                });

            modelBuilder.Entity("UniUnboxdAPI.Models.Student", b =>
                {
                    b.HasOne("UniUnboxdAPI.Models.User", null)
                        .WithOne()
                        .HasForeignKey("UniUnboxdAPI.Models.Student", "Id")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("UniUnboxdAPI.Models.University", null)
                        .WithMany("Students")
                        .HasForeignKey("UniversityId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();
                });

            modelBuilder.Entity("UniUnboxdAPI.Models.University", b =>
                {
                    b.HasOne("UniUnboxdAPI.Models.User", null)
                        .WithOne()
                        .HasForeignKey("UniUnboxdAPI.Models.University", "Id")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();
                });

            modelBuilder.Entity("UniUnboxdAPI.Models.Course", b =>
                {
                    b.Navigation("Reviews");
                });

            modelBuilder.Entity("UniUnboxdAPI.Models.Review", b =>
                {
                    b.Navigation("Likes");

                    b.Navigation("Replies");
                });

            modelBuilder.Entity("UniUnboxdAPI.Models.Professor", b =>
                {
                    b.Navigation("AssignedCourses");

                    b.Navigation("Replies");
                });

            modelBuilder.Entity("UniUnboxdAPI.Models.Student", b =>
                {
                    b.Navigation("Followers");

                    b.Navigation("Following");

                    b.Navigation("Likes");

                    b.Navigation("NotificationSettings");

                    b.Navigation("Replies");

                    b.Navigation("Reviews");
                });

            modelBuilder.Entity("UniUnboxdAPI.Models.University", b =>
                {
                    b.Navigation("Courses");

                    b.Navigation("Students");
                });
#pragma warning restore 612, 618
        }
    }
}
