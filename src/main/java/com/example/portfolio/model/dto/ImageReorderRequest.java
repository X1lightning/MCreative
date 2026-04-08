package com.example.portfolio.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record ImageReorderRequest(@Valid @NotEmpty List<ImageReorderItem> items) {
}
