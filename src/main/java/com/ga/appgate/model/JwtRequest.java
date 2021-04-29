package com.ga.appgate.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ApiModel
public class JwtRequest implements Serializable {
    private static final long serialVersionUID = 5143500880371658407L;

    @ApiModelProperty(value = "Nombre de usuario", required = true)
    @Max(value = 256, message = "No puede contener más de 256 caracteres")
    @NotBlank(message = "Campo requerido")
    private String username;

    @ApiModelProperty(value = "Contraseña", required = true)
    @Max(value = 256, message = "No puede contener más de 256 caracteres")
    @NotBlank(message = "Campo requerido")
    private String password;
}
