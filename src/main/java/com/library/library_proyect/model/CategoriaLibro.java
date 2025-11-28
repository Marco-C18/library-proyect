package com.library.library_proyect.model;

public enum CategoriaLibro {
    LITERATURA("Literatura"),
    MATEMATICAS("Matemáticas"),
    HISTORIA("Historia"),
    CIENCIAS("Ciencias"),
    IDIOMAS("Idiomas"),
    INFORMATICA("Informática"),
    FILOSOFIA("Filosofía"),
    ARTE("Arte"),
    PSICOLOGIA("Psicología"),
    DERECHO("Derecho");

    private final String displayName;

    CategoriaLibro(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
    
}