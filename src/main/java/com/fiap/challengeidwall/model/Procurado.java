package com.fiap.challengeidwall.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

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

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "procurado")
    @SequenceGenerator(name = "procurado", sequenceName = "sq_cif_procurado", allocationSize = 1)
    @Column(name = "cd_procurado")
    private Long id;

    @Column(name = "nm_procurado", nullable=false, length=40)
    private String procurado;

    @Temporal(TemporalType.DATE)
    @Column(name = "dt_nascimento", nullable=false)
    private Calendar dataNascimento;

    @Column(name = "ds_nacionalidade", nullable=false, length=30)
    private String nacionalidade;

    @Column(name = "ds_foto_link", nullable=false, length=300)
    private String foto;
}
