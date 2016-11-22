package dam.isi.frsf.utn.edu.ar.lab05.modelo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mdominguez on 06/10/16.
 */
public class Tarea {

    private int id;
    private Boolean terminada;
    private Integer horasEstimadas;
    private Integer minutosTrabajados;
    private String descripcion;
    private Proyecto proyecto;
    private Prioridad prioridad;
    private Usuario responsable;

    public Tarea() {
    }

    public Tarea(Integer id, Boolean terminada, Integer horasEstimadas, Integer minutosTrabajados, String descripcion, Proyecto proyecto, Prioridad prioridad, Usuario responsable) {
        this.id = id;
        this.terminada = terminada;
        this.horasEstimadas = horasEstimadas;
        this.minutosTrabajados = minutosTrabajados;
        this.descripcion = descripcion;
        this.proyecto = proyecto;
        this.prioridad = prioridad;
        this.responsable = responsable;
    }

    public Tarea(JSONObject objetoJSON) throws JSONException {
        this.id = objetoJSON.getInt("id"); //PROBAR..IF ID>500...this.id = Integer.parseInt(objetoJSON.getString("id"));
        this.terminada=false;
        this.horasEstimadas = objetoJSON.getInt("horasEstimadas");
        this.minutosTrabajados = objetoJSON.getInt("minutosTrabajados");
        this.descripcion = objetoJSON.getString("descripcion");
        this.proyecto = new Proyecto(objetoJSON.getInt("proyectoId"));
        this.prioridad = new Prioridad(objetoJSON.getInt("prioridadId"));
        this.responsable = new Usuario(objetoJSON.getInt("usuarioId"));
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getTerminada() {
        return terminada;
    }

    public void setTerminada(Boolean terminada) {
        this.terminada = terminada;
    }

    public Integer getHorasEstimadas() {
        return horasEstimadas;
    }

    public void setHorasEstimadas(Integer horasEstimadas) {
        this.horasEstimadas = horasEstimadas;
    }

    public Integer getMinutosTrabajados() {
        return minutosTrabajados;
    }

    public void setMinutosTrabajados(Integer minutosTrabajados) {
        this.minutosTrabajados = minutosTrabajados;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }

    public Usuario getResponsable() {
        return responsable;
    }

    public void setResponsable(Usuario responsable) {
        this.responsable = responsable;
    }
}
