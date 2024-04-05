package com.example.uniunboxd.models.student;

public class StudentEditModel {
    public final int Id;
    public String Name;
    public String Image;
    public NotificationSettings NotificationSettings;
    public int VerificationStatus;

    /**
     * Constructor for the StudentEditModel class.
     *
     * @param student The student's profile model.
     */
    public StudentEditModel(StudentProfileModel student) {
        Id = student.Id;
        Image = student.Image;
        Name = student.Name;
        NotificationSettings = student.NotificationSettings;
        VerificationStatus = student.VerificationStatus;
    }
}
