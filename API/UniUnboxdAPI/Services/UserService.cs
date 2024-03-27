﻿using UniUnboxdAPI.Models;
using UniUnboxdAPI.Repositories;

namespace UniUnboxdAPI.Services
{
    public class UserService(UserRepository userRepository, MailService mailService, PushNotificationService pushNotificationService)
    {
        /// <summary>
        /// Check whether there exists a student with the provided id.
        /// </summary>
        /// <param name="id">Provided student id.</param>
        /// <returns>Whether there exists a student with the provided id.</returns>
        public async Task<bool> DoesStudentExist(int id)
            => await userRepository.DoesStudentExist(id);

        /// <summary>
        /// Gets the Student model attached to the provided id.
        /// </summary>
        /// <param name="id">Provided student id.</param>
        /// <returns>Student model with given id.</returns>
        public async Task<Student> GetStudent(int id)
            => await userRepository.GetStudent(id);

        public async Task SetDeviceToken(int studentId, string deviceToken)
            => await userRepository.SetDeviceToken(studentId, deviceToken);

        /// <summary>
        /// Check whether student 1 already follows student 2.
        /// </summary>
        /// <param name="followingStudentId">Provided student 1 id.</param>
        /// <param name="followedStudentId">Provided student 2 id.</param>
        /// <returns>Whether student 1 follows student 2.</returns>
        public async Task<bool> DoesStudentFollowStudent(int followingStudentId, int followedStudentId)
            => await userRepository.DoesStudentFollowStudent(followingStudentId, followedStudentId);

        /// <summary>
        /// Student 1 follows student 2.
        /// </summary>
        /// <param name="followingStudent">Provided student 1.</param>
        /// <param name="followedStudent">Provided student 2.</param>
        /// <returns>No object or value is returned by this method when it completes.</returns>
        public async Task FollowStudent(Student followingStudent, Student followedStudent)
        {
            var followModel = CreateFollow(followingStudent, followedStudent);
            await userRepository.FollowStudent(followModel);
        }

        /// <summary>
        /// Notify Student 2 that Student 1 has followed them.
        /// </summary>
        /// <param name="followingStudent">Provided student 1.</param>
        /// <param name="followedStudent">Provided student 2.</param>
        /// <returns>No object or value is returned by this method when it completes.</returns>
        public void NotifyFollowedStudent(Student followingStudent, Student followedStudent)
        {
            if (followedStudent.NotificationSettings!.ReceivesNewFollowerMail)
                mailService.SendNewFollowerNotification(followingStudent, followedStudent);

            if (followedStudent.NotificationSettings!.ReceivesNewFollowerPush)
                pushNotificationService.SendNewFollowerNotification(followingStudent, followedStudent);
        }

        /// <summary>
        /// Student 1 unfollows student 2.
        /// </summary>
        /// <param name="unfollowingStudent">Provided student 1.</param>
        /// <param name="unfollowedStudent">Provided student 2.</param>
        /// <returns>No object or value is returned by this method when it completes.</returns>
        public async Task UnfollowStudent(Student unfollowingStudent, Student unfollowedStudent)
        {
            await userRepository.UnfollowStudent(unfollowingStudent.Id, unfollowedStudent.Id);
        }

        private static Follow CreateFollow(Student followingStudent, Student followedStudent)
            => new ()
            {
                FollowingStudent = followingStudent,
                FollowedStudent = followedStudent
            };
    }
}