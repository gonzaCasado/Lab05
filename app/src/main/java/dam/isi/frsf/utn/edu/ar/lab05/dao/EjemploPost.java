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
import java.util.Date;
import java.util.List;

import dam.isi.frsf.utn.edu.ar.lab05.modelo.Proyecto;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Tarea;


public class EjemploPost {

    static final String IP_SERVER = "192.168.0.15";//"192.168.1.11";
    static final String PORT_SERVER = "4000";

    // OPERACION POST (nueva tarea)
    public static void enviarOperacionToAPI(String operacion, String descripcion, int responsable, int hsEstimadas, int proyecto, int minT, int prioridad){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        HttpURLConnection urlConnection=null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy mm:ss SSS");
        try {
            JSONObject nuevoObjeto= new JSONObject();
            nuevoObjeto.put("descripcion",descripcion);
            nuevoObjeto.put("horasEstimadas",hsEstimadas);
            nuevoObjeto.put("minutosTrabajados", minT);
            nuevoObjeto.put("finalizada",false);
            nuevoObjeto.put("proyectoId", proyecto);
            nuevoObjeto.put("prioridadId",prioridad);
            nuevoObjeto.put("usuarioId", responsable);


            String str= nuevoObjeto.toString();
            byte[] data=str.getBytes("UTF-8");
            //Log.d("EjemploPost","str---> "+str);

            URL url = new URL("http://"+IP_SERVER+":"+PORT_SERVER+"/tareas/");

            // VER AQUI https://developer.android.com/reference/java/net/HttpURLConnection.html
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod(operacion);
            urlConnection.setFixedLengthStreamingMode(data.length);
            urlConnection.setRequestProperty("Content-Type","application/json");
            DataOutputStream printout = new DataOutputStream(urlConnection.getOutputStream());

            printout.write(data);
            printout.flush ();
            printout.close ();
            //Log.d("TEST-ARR","FIN!!! "+urlConnection.getResponseMessage());

        }catch (JSONException e2) {
            Log.e("TEST-ARR",e2.getMessage(),e2);
            e2.printStackTrace();
        }  catch (IOException e1) {
            Log.e("TEST-ARR",e1.getMessage(),e1);
            e1.printStackTrace();
        }finally {
            urlConnection.disconnect();
        }
    }

    // OPERACION POST (agrega contacto)
    public static void agregaContacto(String operacion, String nombre, String correo){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        HttpURLConnection urlConnection=null;
        try {
            JSONObject nuevoObjeto= new JSONObject();
            nuevoObjeto.put("nombre",nombre);
            nuevoObjeto.put("correoElectronico",correo);

            String str= nuevoObjeto.toString();
            byte[] data=str.getBytes("UTF-8");
            //Log.d("EjemploPost","str---> "+str);

            URL url = new URL("http://"+IP_SERVER+":"+PORT_SERVER+"/usuarios/");

            // VER AQUI https://developer.android.com/reference/java/net/HttpURLConnection.html
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod(operacion);
            urlConnection.setFixedLengthStreamingMode(data.length);
            urlConnection.setRequestProperty("Content-Type","application/json");
            DataOutputStream printout = new DataOutputStream(urlConnection.getOutputStream());

            printout.write(data);
            printout.flush ();
            printout.close ();
            //Log.d("TEST-ARR","FIN!!! "+urlConnection.getResponseMessage());

        }catch (JSONException e2) {
            Log.e("TEST-ARR",e2.getMessage(),e2);
            e2.printStackTrace();
        }  catch (IOException e1) {
            Log.e("TEST-ARR",e1.getMessage(),e1);
            e1.printStackTrace();
        }finally {
            urlConnection.disconnect();
        }
    }



    //OPERACION PUT (editar tarea)
    public static void enviarOperacionToAPIUPDATE(int idTarea, String operacion, int hsEstimadas, String descripcion, int responsable){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        List listaTareas = leerNoticias();

        HttpURLConnection urlConnection=null;

        try {
            JSONObject nuevoObjeto= new JSONObject();
            nuevoObjeto.put("descripcion",descripcion);
            nuevoObjeto.put("horasEstimadas",hsEstimadas);

            for(int i=0;i<listaTareas.size();i++){

                if(((Tarea)listaTareas.get(i)).getId()==idTarea) {
                    nuevoObjeto.put("minutosTrabajados", ((Tarea) listaTareas.get(i)).getMinutosTrabajados());
                    nuevoObjeto.put("finalizada",(((Tarea) listaTareas.get(i)).getTerminada()));
                    nuevoObjeto.put("proyectoId", ((Tarea) listaTareas.get(i)).getProyecto().getId());
                    nuevoObjeto.put("prioridadId", ((Tarea) listaTareas.get(i)).getPrioridad().getId());

                    //nuevoObjeto.put("id",((Tarea) listaTareas.get(i)).getId());
                }
            }
            nuevoObjeto.put("usuarioId", responsable);


            String str= nuevoObjeto.toString();
            byte[] data=str.getBytes("UTF-8");
            //Log.d("EjemploPost","str---> "+str);
            Log.d("TEST-ARR","EDITADO: "+idTarea);


            URL url = new URL("http://"+IP_SERVER+":"+PORT_SERVER+"/tareas/"+idTarea);

            // VER AQUI https://developer.android.com/reference/java/net/HttpURLConnection.html
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod(operacion);
            urlConnection.setFixedLengthStreamingMode(data.length);
            urlConnection.setRequestProperty("Content-Type","application/json");
            DataOutputStream printout = new DataOutputStream(urlConnection.getOutputStream());

            printout.write(data);
            printout.flush ();
            printout.close ();
            //Log.d("TEST-ARR","FIN!!! "+urlConnection.getResponseMessage());

        }catch (JSONException e2) {
            Log.e("TEST-ARR",e2.getMessage(),e2);
            e2.printStackTrace();
        }  catch (IOException e1) {
            Log.e("TEST-ARR",e1.getMessage(),e1);
            e1.printStackTrace();
        }finally {
            urlConnection.disconnect();
        }
    }

    //OPERACION DELETE (borrar tarea)
    public static void enviarOperacionToAPIDELETE(String operacion, int id){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        HttpURLConnection urlConnection=null;

        try {

            JSONObject nuevoObjeto= new JSONObject();
            String str= nuevoObjeto.toString();
            byte[] data=str.getBytes("UTF-8");
            // Log.d("EjemploPost","str---> "+str);

            URL url = new URL("http://"+IP_SERVER+":"+PORT_SERVER+"/tareas/"+id);
            Log.d("TEST-ARR","BORRADO: "+id);


            // VER AQUI https://developer.android.com/reference/java/net/HttpURLConnection.html
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod(operacion);
            urlConnection.setFixedLengthStreamingMode(data.length);
            urlConnection.setRequestProperty("Content-Type","application/json");
            DataOutputStream printout = new DataOutputStream(urlConnection.getOutputStream());

            printout.write(data);
            printout.flush ();
            printout.close ();
            //Log.d("TEST-ARR","FIN!!! "+urlConnection.getResponseMessage());

        /*}catch (JSONException e2) {
            Log.e("TEST-ARR",e2.getMessage(),e2);
            e2.printStackTrace();*/
        }  catch (IOException e1) {
            Log.e("TEST-ARR",e1.getMessage(),e1);
            e1.printStackTrace();
        }finally {
            urlConnection.disconnect();
        }
    }

    //Método que devuelve una lista de Tareas, que se encuentran en el archivo JSON
    public static List<Tarea> leerNoticias(){

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
                Log.d("Pruebaaa: ",lista_tareas.get(i).getId().toString());
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


    public static List<Proyecto> traerProyectos(){

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
                //Log.d("Pruebaaa: ",lista_proyectos.get(i).getDescripcion().toString());
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
}