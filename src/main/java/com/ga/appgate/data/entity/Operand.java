package com.ga.appgate.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity(name = "operand")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Operand implements Serializable {

    private static final long serialVersionUID = -1506520267970356592L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;
    @Column
    private String username;
    @Column
    private String sessionId;
    @Column
    private Double value;
    @Column
    private boolean used;

}
