package com.fiap.challengeidwall.model.interpol;

import com.fiap.challengeidwall.model.interpol.link.Links;
import lombok.Data;

import java.util.List;

@Data
public class InterpolPerson {

    private String name;
    private List<String> nationalities;
    private String date_of_birth;
    private Links _links;

    @Override
    public String toString() {
        return
                "\nName: " + name
                + "\nNationalities: " + nationalities
                + "\nDateOfBirth: " + date_of_birth
                + "\n";
    }
}
