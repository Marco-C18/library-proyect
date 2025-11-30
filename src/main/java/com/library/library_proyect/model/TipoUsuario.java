package com.library.library_proyect.model;

public enum TipoUsuario {
    ESTUDIANTE("Estudiante"),
    BIBLIOTECARIO("Bibliotecario");

    private final String displayName;

    TipoUsuario(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}