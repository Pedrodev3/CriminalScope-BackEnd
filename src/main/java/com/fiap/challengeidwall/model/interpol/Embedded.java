package com.fiap.challengeidwall.model.interpol;

import lombok.Data;

import java.util.List;

@Data
public class Embedded {
    private List<InterpolPerson> notices;
}
