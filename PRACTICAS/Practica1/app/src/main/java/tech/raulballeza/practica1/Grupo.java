package tech.raulballeza.practica1;

import java.io.Serializable;
import java.util.ArrayList;

public class Grupo implements Serializable {
    public int size() {
        return alumnos.size();
    }

    ArrayList<Alumno> alumnos;
    String nombre;
    ArrayList<Asistencia> asistencias;

    public Grupo(String nombre) {
        this.nombre = nombre;
        this.alumnos = new ArrayList<>();
        this.asistencias = new ArrayList<>();
    }

    public void add( Alumno element) {
        alumnos.add( element);
    }

    public String toString(){return nombre;} //assuming name is a String

    public void add(Asistencia element) {
        asistencias.add(element);
    }
}
