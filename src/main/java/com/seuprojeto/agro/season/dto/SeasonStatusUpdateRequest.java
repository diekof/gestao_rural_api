package com.seuprojeto.agro.season.dto;

import com.seuprojeto.agro.season.domain.SeasonStatus;
import jakarta.validation.constraints.NotNull;

public record SeasonStatusUpdateRequest(@NotNull SeasonStatus status) {}
