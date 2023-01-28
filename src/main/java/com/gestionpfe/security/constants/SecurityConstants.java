package com.gestionpfe.security.constants;

public enum SecurityConstants {

    JWT_KEY("jxgEQeXHuPq8VdbyYFNkANdudQ53YUn4"),
    JWT_HEADER("Authorization"),
    JWT_REFRESH("Authorization_Refresh");

    private String constant;

    SecurityConstants(String constant) {
        this.constant = constant;
    }

    public String value() {
        return this.constant;
    }
}