package tech.raulballeza.practica1;

import java.io.Serializable;
import java.util.Objects;

public class Alumno implements Serializable {
    String nombre;
    Boolean asistencia;

    public float getNumAsistencia() {
        return numAsistencia;
    }

    public void setNumAsistencia(float numAsistencia) {
        this.numAsistencia = numAsistencia;
    }

    float numAsistencia;

    public Alumno(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(Boolean asistencia) {
        this.asistencia = asistencia;
    }

    @Override
    public String toString() {
        return nombre;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alumno alumno = (Alumno) o;
        return Objects.equals(getNombre(), alumno.getNombre());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNombre());
    }
}
