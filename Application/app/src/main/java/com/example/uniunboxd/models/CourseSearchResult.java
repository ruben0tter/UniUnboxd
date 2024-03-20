package com.example.uniunboxd.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CourseSearchResult {
    public final int Id;
    public final String Name;
    public final String Code;
    public final String University;
    public final int UniversityId;

    public final String Image;
    public final double AverageRating;

    @JsonCreator
    public CourseSearchResult(@JsonProperty("id") int id, @JsonProperty("name") String name,
                              @JsonProperty("code") String code, @JsonProperty("image") String image,
                              @JsonProperty("university") String university,
                              @JsonProperty("universityId") int universityId,
                              @JsonProperty("averageRating") int averageRating) {
        Id = id;
        Name = name;
        Code = code;
        Image = image;
        University = university;
        UniversityId = universityId;
        AverageRating = averageRating;
    }

//    public View createView(LayoutInflater inflater, ViewGroup container,
//                           Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.search_result_course, container, false);
//        TextView name = view.findViewById(R.id.courseName);
//        TextView code = view.findViewById(R.id.courseCode);
//        TextView professor = view.findViewById(R.id.professor);
//        TextView description = view.findViewById(R.id.description);
//        TextView universityName = view.findViewById(R.id.universityName);
//
//        name.setText(Name);
//        code.setText(Code);
//        universityName.setText(University);
//        return view;
//    }
}
