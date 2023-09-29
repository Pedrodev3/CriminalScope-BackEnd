package com.fiap.challengeidwall.model.fbi;

import com.fiap.challengeidwall.model.fbi.FBIPerson;
import lombok.Data;

import java.util.List;

@Data
public class FBIResponse {

    private int total;
    private List<FBIPerson> items;
}
