package com.fiap.challengeidwall.model.interpol.details;

import lombok.Data;

@Data
public class Arrest {
    private String charge;
    private String issuing_country_id;
    private String charge_translation;
}
