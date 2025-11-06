package com.example.equipment.web.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class BorrowDtos {
    public record Create(
            @NotNull Long deviceId,
            @NotNull Long borrowerUserId,
            @NotNull LocalDateTime borrowedAt,
            @NotNull LocalDateTime dueAt,
            @NotNull Long checkoutOperatorId
    ) {}

    public record Return(
            @NotNull Long returnOperatorId,
            @NotNull LocalDateTime returnedAt
    ) {}
}


