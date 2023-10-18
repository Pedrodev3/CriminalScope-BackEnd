package com.fiap.challengeidwall.model.fbi;


import lombok.Data;

import java.util.List;

@Data
public class FBIPerson {

    private String title;
    private List<String> dates_of_birth_used;
    private String nationality;
    private String place_of_birth;
    private String sex;
    private String height_min;
    private String height_max;
    private List<Image> images;
    private String details;

    @Override
    public String toString() {
        return
                "\nTitle: " + title
                        + "\nNationality: " + nationality
                        + "\nDatesOfBirthUsed: " + dates_of_birth_used
                        + "\nPlaceOfBirth: " + place_of_birth
                        + "\nSex: " + sex
                        + "\nHeightMax: " + height_max
                        + "\nImages: " + images
                        + "\nDetails: " + details
                        + "\n";
    }
}
