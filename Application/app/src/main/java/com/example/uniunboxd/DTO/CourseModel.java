package com.example.uniunboxd.DTO;

/**
 * The CourseModel class represents a course with its unique identifier, name, code, and image.
 */
public class CourseModel {
    /**
     * The unique identifier of the course.
     */
    public int id;

    /**
     * The name of the course.
     */
    public String name;

    /**
     * The code of the course.
     */
    public String code;

    /**
     * The image associated with the course.
     */
    public String image;

    /**
     * Constructs a new CourseModel with the specified id, name, code, and image.
     * @param id The unique identifier of the course.
     * @param name The name of the course.
     * @param code The code of the course.
     * @param image The image associated with the course.
     */
    public CourseModel(int id, String name, String code, String image) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.image = image;
    }
}
