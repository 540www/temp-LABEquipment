package com.example.equipment.web.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class DeviceDtos {

    public record Create(
            @NotNull Long categoryId,
            @NotNull Long laboratoryId,
            String className,
            @NotBlank String deviceName,
            @NotBlank String deviceCode,
            String serialNo,
            String spec,
            BigDecimal unitPrice,
            @PositiveOrZero Integer quantity
    ) {}

    public record UpdateStatus(
            @NotBlank String status
    ) {}
}


