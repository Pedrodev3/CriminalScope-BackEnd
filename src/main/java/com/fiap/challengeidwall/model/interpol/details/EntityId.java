package com.fiap.challengeidwall.model.interpol.details;

import lombok.Data;

import java.util.List;

@Data
public class EntityId {
    private String place_of_birth;
    private String sex_id;
    private String height;
    private List<Arrest> arrest_warrants;
}
