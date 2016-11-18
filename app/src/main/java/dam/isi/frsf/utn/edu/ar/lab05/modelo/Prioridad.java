package dam.isi.frsf.utn.edu.ar.lab05.modelo;

/**
 * Created by mdominguez on 06/10/16.
 */
public class Prioridad {

    private Integer id;
    private String prioridad;

    public Prioridad(){

    }

    public Prioridad(String cadena){
        prioridad=cadena;
    }
    public Prioridad(int prior){id=prior;}

    public Prioridad(Integer id, String prioridad) {
        this.id = id;
        this.prioridad = prioridad;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }
}
