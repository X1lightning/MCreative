package com.example.portfolio.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

public record ImageReorderItem(
        @NotNull Long imageId,
        @NotNull @Max(9999) Integer displayOrder
) {
}
