package com.fiap.challengeidwall.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Calendar;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_cif_procurado")
public class Procurado {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "procurado")
    @SequenceGenerator(name = "procurado", sequenceName = "sq_cif_procurado", allocationSize = 1)
    @Column(name = "cd_procurado")
    @Id
    private Long id;

    @Column(name = "nm_procurado", nullable=false, length=40)
    private String procurado;

    @Column(name = "dt_nascimento")
    private String dataNascimento;

    @Column(name = "ds_nacionalidade", length=30)
    private String nacionalidade;

    @Column(name = "ds_status")
    private String status;

    @Column(name = "ds_foto_link", length=300)
    private String foto;
}
