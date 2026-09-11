package com.pinwox.tvmazeapi.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentRequestDTO {

    @NotNull(message = "show_id es requerido")
    private Long showId;

    @NotBlank(message = "comment es requerido")
    private String comment;

    @NotNull(message = "rating es requerido")
    @Min(value = 0, message = "rating debe ser entre 0 y 5")
    @Max(value = 5, message = "rating debe ser entre 0 y 5")
    private Integer rating;

}
