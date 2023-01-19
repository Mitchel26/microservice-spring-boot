package com.jvaras.product.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter @Getter @Builder
public class ErrorMessage {
    private String code;
    private List<Map<String, String>> messages;
}
