package com.fiap.challengeidwall.model.interpol;

import lombok.Data;

import java.util.List;

@Data
public class InterpolResponse {
    private int total;
    private InterpolPerson items;
}
