package tech.raulballeza.practica1;

import java.io.Serializable;
import java.util.ArrayList;

public class Asistencia implements Serializable {
    String fecha;
    ArrayList<Alumno> alumnos;

    public Asistencia(String fecha, ArrayList<Alumno> alumnos) {
        this.fecha = fecha;
        this.alumnos = alumnos;
    }

    @Override
    public String toString() {
        return fecha;
    }
}
