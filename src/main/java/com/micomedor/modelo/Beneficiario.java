package com.micomedor.modelo;

public class Beneficiario {

    private int id;
    private String nombre;
    private String documento;
    private String telefono;
    private int edad;

    public Beneficiario() {
    }

    public Beneficiario(String nombre, String documento, String telefono, int edad) {
        this.nombre = nombre;
        this.documento = documento;
        this.telefono = telefono;
        this.edad = edad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }
}
