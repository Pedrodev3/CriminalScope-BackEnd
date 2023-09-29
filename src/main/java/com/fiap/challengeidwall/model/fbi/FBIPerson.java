package com.fiap.challengeidwall.model.fbi;


import lombok.Data;

import java.util.List;

@Data
public class FBIPerson {

//    private String thumb;
    private String title;
    private String nationality;
    private List<String> dates_of_birth_used;
    private List<Image> images;

    @Override
    public String toString() {
        return
                "\nTitle: " + title
                + "\nNationality: " + nationality
                + "\nDatesOfBirthUsed: " + dates_of_birth_used
                + "\n";
    }
}
