package tech.raulballeza.p_intermedio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Restaurente {
    int code;
    String name;
    Map<String, Double> menu = new HashMap<String, Double>();
    public Restaurente(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Double> getMenu() {
        return menu;
    }

    public void setMenu(Map<String, Double> menu) {
        this.menu = menu;
    }



    public void addToMenu(String name, Double price) {
        menu.put(name, price);
    }

    @Override
    public String toString() {
        return "Restaurente{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", menu=" + menu +
                '}';
    }
}
