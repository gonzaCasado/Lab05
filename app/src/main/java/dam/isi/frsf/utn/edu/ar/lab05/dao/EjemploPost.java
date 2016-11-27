package dam.isi.frsf.utn.edu.ar.lab05.dao;

import android.os.StrictMode;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import dam.isi.frsf.utn.edu.ar.lab05.modelo.Proyecto;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Tarea;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Usuario;


public class EjemploPost {

    //static final String IP_SERVER = "192.168.1.11";
    static final String IP_SERVER = "192.168.0.98"; // Gabi
    static final String PORT_SERVER = "4000";
    public static void conexion(String operacion, URL url, byte[] data){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpURLConnection urlConnection=null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod(operacion);
            urlConnection.setFixedLengthStreamingMode(data.length);
            urlConnection.setRequestProperty("Content-Type","application/json");
            DataOutputStream printout = new DataOutputStream(urlConnection.getOutputStream());
            printout.write(data);
            printout.flush ();
            printout.close ();
        } catch (IOException e1) {
            Log.e("TEST-ARR",e1.getMessage(),e1);
            e1.printStackTrace();
        }finally {
            urlConnection.disconnect();
        }
    }

    public static void nuevaTarea(String operacion, String descripcion, int responsable, int hsEstimadas, int proyecto, int minT, int prioridad){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy mm:ss SSS");
        try {
            JSONObject nuevoObjeto= new JSONObject();
            nuevoObjeto.put("descripcion",descripcion);
            nuevoObjeto.put("horasEstimadas",hsEstimadas);
            nuevoObjeto.put("finalizada",false);
            nuevoObjeto.put("minutosTrabajados", minT);
            nuevoObjeto.put("proyectoId", proyecto);
            nuevoObjeto.put("prioridadId",prioridad);
            nuevoObjeto.put("usuarioId", responsable);
            String str= nuevoObjeto.toString();
            byte[] data=str.getBytes("UTF-8");
            URL url = new URL("http://"+IP_SERVER+":"+PORT_SERVER+"/tareas/");
            conexion(operacion,url,data);
        }catch (JSONException e2) {
            Log.e("TEST-ARR",e2.getMessage(),e2);
            e2.printStackTrace();
        }  catch (IOException e1) {
            Log.e("TEST-ARR",e1.getMessage(),e1);
            e1.printStackTrace();
        }
    }

    public static void nuevoContacto(String operacion, String nombre, String correo){
        try {
            JSONObject nuevoObjeto= new JSONObject();
            nuevoObjeto.put("nombre",nombre);
            nuevoObjeto.put("correoElectronico",correo);
            String str= nuevoObjeto.toString();
            byte[] data=str.getBytes("UTF-8");
            URL url = new URL("http://"+IP_SERVER+":"+PORT_SERVER+"/usuarios/");
            conexion(operacion,url,data);

        }catch (JSONException e2) {
            Log.e("TEST-ARR",e2.getMessage(),e2);
            e2.printStackTrace();
        }  catch (IOException e1) {
            Log.e("TEST-ARR",e1.getMessage(),e1);
            e1.printStackTrace();
        }
    }

    public static void nuevoProyecto(String operacion, Proyecto proyecto){
        String nombre= proyecto.getNombre();
        try {
            JSONObject nuevoObjeto= new JSONObject();
            nuevoObjeto.put("nombre",nombre);
            String str= nuevoObjeto.toString();
            byte[] data=str.getBytes("UTF-8");
            URL url = new URL("http://"+IP_SERVER+":"+PORT_SERVER+"/proyectos/");
            conexion(operacion,url,data);
        }catch (JSONException e2) {
            Log.e("TEST-ARR",e2.getMessage(),e2);
            e2.printStackTrace();
        }  catch (IOException e1) {
            Log.e("TEST-ARR",e1.getMessage(),e1);
            e1.printStackTrace();
        }
    }

    public static void editarTarea(int idTarea, String operacion, int hsEstimadas, String descripcion, int responsable){
        List listaTareas = getTareas();
        try {
            JSONObject nuevoObjeto= new JSONObject();
            nuevoObjeto.put("descripcion",descripcion);
            nuevoObjeto.put("horasEstimadas",hsEstimadas);
            for(int i=0;i<listaTareas.size();i++){
                if(((Tarea)listaTareas.get(i)).getId()==idTarea) {
                    nuevoObjeto.put("finalizada",(((Tarea) listaTareas.get(i)).getTerminada()));
                    nuevoObjeto.put("minutosTrabajados", ((Tarea) listaTareas.get(i)).getMinutosTrabajados());
                    nuevoObjeto.put("proyectoId", ((Tarea) listaTareas.get(i)).getProyecto().getId());
                    nuevoObjeto.put("prioridadId", ((Tarea) listaTareas.get(i)).getPrioridad().getId());
                    nuevoObjeto.put("id",idTarea);
                    break;
                }
            }
            nuevoObjeto.put("usuarioId", responsable);
            String str= nuevoObjeto.toString();
            byte[] data=str.getBytes("UTF-8");
            Log.d("TEST-ARR","EDITADO: "+idTarea);

            URL url = new URL("http://"+IP_SERVER+":"+PORT_SERVER+"/tareas/"+idTarea);
            conexion(operacion, url, data);
        }catch (JSONException e2) {
            Log.e("TEST-ARR",e2.getMessage(),e2);
            e2.printStackTrace();
        }  catch (IOException e1) {
            Log.e("TEST-ARR",e1.getMessage(),e1);
            e1.printStackTrace();
        }
    }

    public static void actualizarTarea(String operacion,Integer idTarea, long tiempoActual) {
        long tiempoJson=0;
        List listaTareas = getTareas();
        try {
            JSONObject nuevoObjeto= new JSONObject();
            for(int i=0;i<listaTareas.size();i++){
                if(((Tarea)listaTareas.get(i)).getId()==idTarea) {
                    nuevoObjeto.put("descripcion",(((Tarea) listaTareas.get(i)).getDescripcion()));
                    nuevoObjeto.put("horasEstimadas",(((Tarea) listaTareas.get(i)).getHorasEstimadas()));
                    nuevoObjeto.put("finalizada",(((Tarea) listaTareas.get(i)).getTerminada()));
                    tiempoJson=((Tarea)listaTareas.get(i)).getMinutosTrabajados()+tiempoActual;
                    nuevoObjeto.put("minutosTrabajados",tiempoJson);
                    nuevoObjeto.put("proyectoId", ((Tarea) listaTareas.get(i)).getProyecto().getId());
                    nuevoObjeto.put("prioridadId", ((Tarea) listaTareas.get(i)).getPrioridad().getId());
                    nuevoObjeto.put("usuarioId",((Tarea) listaTareas.get(i)).getResponsable().getId());
                    nuevoObjeto.put("id",idTarea);
                    break;
                }
            }
            String str= nuevoObjeto.toString();
            byte[] data=str.getBytes("UTF-8");
            URL url = new URL("http://"+IP_SERVER+":"+PORT_SERVER+"/tareas/"+idTarea);
            conexion(operacion, url, data);
        }catch (JSONException e2) {
            Log.e("TEST-ARR",e2.getMessage(),e2);
            e2.printStackTrace();
        }  catch (IOException e1) {
            Log.e("TEST-ARR",e1.getMessage(),e1);
            e1.printStackTrace();
        }
    }

    public static void borrarTarea(String operacion, int id){
        try {
            JSONObject nuevoObjeto= new JSONObject();
            String str= nuevoObjeto.toString();
            byte[] data=str.getBytes("UTF-8");
            URL url = new URL("http://"+IP_SERVER+":"+PORT_SERVER+"/tareas/"+id);
            conexion(operacion, url, data);
        }catch (IOException e1) {
            Log.e("TEST-ARR",e1.getMessage(),e1);
            e1.printStackTrace();
        }
    }

    public static void borrarProyecto(String operacion, int id){
        try {
            JSONObject nuevoObjeto= new JSONObject();
            String str= nuevoObjeto.toString();
            byte[] data=str.getBytes("UTF-8");
            URL url = new URL("http://"+IP_SERVER+":"+PORT_SERVER+"/proyectos/"+id);
            conexion(operacion, url, data);
            List<Tarea> tareas= getTareas();
            for(int i = 0; i < tareas.size(); i++){
                if(tareas.get(i).getProyecto().getId() == id){
                    borrarTarea("DELETE",tareas.get(i).getId());
                }
            }
        }  catch (IOException e1) {
            Log.e("TEST-ARR",e1.getMessage(),e1);
            e1.printStackTrace();
        }
    }

    //Método que devuelve una lista de Tareas, que se encuentran en el archivo JSON
    public static List<Tarea> getTareas(){
        JSONArray jsonCadena;
        String json;
        List<Tarea> lista_tareas = new ArrayList<>();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        HttpURLConnection urlConnection=null;
        try {
            URL url= new URL("http://"+IP_SERVER+":"+PORT_SERVER+"/tareas");
            urlConnection= (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            InputStreamReader isw= new InputStreamReader(in);
            StringBuilder sb= new StringBuilder();
            int data = isw.read();
            while(data != -1) {
                char current= (char) data;
                sb.append(current);
                data = isw.read();
            }

            json = sb.toString();
            jsonCadena  = new JSONArray(json);

            for (int i = 0; i < jsonCadena.length(); i++) {
                lista_tareas.add(new Tarea(jsonCadena.getJSONObject(i)));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            if(urlConnection!=null)
                urlConnection.disconnect();
        }
        return lista_tareas;
    }

    public static String getUsuario(int id){
        JSONArray jsonCadena;
        String json, nombre = "";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        HttpURLConnection urlConnection=null;
        try {
            URL url= new URL("http://"+IP_SERVER+":"+PORT_SERVER+"/usuarios");
            urlConnection= (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            InputStreamReader isw= new InputStreamReader(in);
            StringBuilder sb= new StringBuilder();
            int data = isw.read();
            while(data != -1) {
                char current= (char) data;
                sb.append(current);
                data = isw.read();
            }
            json = sb.toString();
            jsonCadena  = new JSONArray(json);
            for (int i = 0; i < jsonCadena.length(); i++) {
                if(Integer.parseInt(jsonCadena.getJSONObject(i).optString("id")) == id){
                    nombre = jsonCadena.getJSONObject(i).optString("nombre");
                    break;
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            if(urlConnection!=null)
                urlConnection.disconnect();
        }
        return nombre;
    }

    public static List<Proyecto> getProyectos(){

        JSONArray jsonCadena;
        String json;
        List<Proyecto> lista_proyectos = new ArrayList<>();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        HttpURLConnection urlConnection=null;
        try {
            URL url= new URL("http://"+IP_SERVER+":"+PORT_SERVER+"/proyectos");
            urlConnection= (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            InputStreamReader isw= new InputStreamReader(in);
            StringBuilder sb= new StringBuilder();
            int data = isw.read();
            while(data != -1) {
                char current= (char) data;
                sb.append(current);
                data = isw.read();
            }

            json = sb.toString();
            jsonCadena  = new JSONArray(json);

            for (int i = 0; i < jsonCadena.length(); i++) {
                lista_proyectos.add(new Proyecto(jsonCadena.getJSONObject(i)));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            if(urlConnection!=null)
                urlConnection.disconnect();
        }
        return lista_proyectos;
    }

    public static List<Tarea> buscarTareas(int proyecto){

        List<Tarea> lista_tareas = getTareas();
        List<Tarea> lista_tareas_proyecto = new ArrayList<Tarea>();

        for(int i = 0; i < lista_tareas.size(); i++){
            if(lista_tareas.get(i).getProyecto().getId() == proyecto){
                lista_tareas_proyecto.add(lista_tareas.get(i));
            }
        }

        return lista_tareas_proyecto;
    }
}