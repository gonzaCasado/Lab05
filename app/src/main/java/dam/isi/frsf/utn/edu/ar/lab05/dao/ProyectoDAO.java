package dam.isi.frsf.utn.edu.ar.lab05.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import dam.isi.frsf.utn.edu.ar.lab05.modelo.Prioridad;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Proyecto;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Tarea;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Usuario;

/**
 * Created by mdominguez on 06/10/16.
 */
public class ProyectoDAO {

    private static final String _SQL_TAREAS_X_PROYECTO = "SELECT "+ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata._ID+" as "+ProyectoDBMetadata.TablaTareasMetadata._ID+
            ", "+ProyectoDBMetadata.TablaTareasMetadata.TAREA +
            ", "+ProyectoDBMetadata.TablaTareasMetadata.HORAS_PLANIFICADAS +
            ", "+ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS +
            ", "+ProyectoDBMetadata.TablaTareasMetadata.FINALIZADA +
            ", "+ProyectoDBMetadata.TablaTareasMetadata.PRIORIDAD +
            ", "+ProyectoDBMetadata.TABLA_PRIORIDAD_ALIAS+"."+ProyectoDBMetadata.TablaPrioridadMetadata.PRIORIDAD +" as "+ProyectoDBMetadata.TablaPrioridadMetadata.PRIORIDAD_ALIAS+
            ", "+ProyectoDBMetadata.TablaTareasMetadata.RESPONSABLE +
            ", "+ProyectoDBMetadata.TABLA_USUARIOS_ALIAS+"."+ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO +" as "+ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO_ALIAS+
            " FROM "+ProyectoDBMetadata.TABLA_PROYECTO + " "+ProyectoDBMetadata.TABLA_PROYECTO_ALIAS+", "+
            ProyectoDBMetadata.TABLA_USUARIOS + " "+ProyectoDBMetadata.TABLA_USUARIOS_ALIAS+", "+
            ProyectoDBMetadata.TABLA_PRIORIDAD + " "+ProyectoDBMetadata.TABLA_PRIORIDAD_ALIAS+", "+
            ProyectoDBMetadata.TABLA_TAREAS + " "+ProyectoDBMetadata.TABLA_TAREAS_ALIAS+
            " WHERE "+ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.PROYECTO+" = "+ProyectoDBMetadata.TABLA_PROYECTO_ALIAS+"."+ProyectoDBMetadata.TablaProyectoMetadata._ID +" AND "+
            ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.RESPONSABLE+" = "+ProyectoDBMetadata.TABLA_USUARIOS_ALIAS+"."+ProyectoDBMetadata.TablaUsuariosMetadata._ID +" AND "+
            ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.PRIORIDAD+" = "+ProyectoDBMetadata.TABLA_PRIORIDAD_ALIAS+"."+ProyectoDBMetadata.TablaPrioridadMetadata._ID +" AND "+
            ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.PROYECTO+" = ?";

    private ProyectoOpenHelper dbHelper;
    private SQLiteDatabase db;


    public ProyectoDAO(Context c){
        this.dbHelper = new ProyectoOpenHelper(c);
    }

    public void open(){
        this.open(false);
    }

    public void open(Boolean toWrite){
        if(toWrite) {
            db = dbHelper.getWritableDatabase();
        }
        else{
            db = dbHelper.getReadableDatabase();
        }
    }

    public void close(){
        db = dbHelper.getReadableDatabase();
    }

    public Cursor listaTareas(Integer idProyecto){
        SQLiteDatabase db =dbHelper.getWritableDatabase();
        Cursor cursorPry = db.rawQuery("SELECT "+ProyectoDBMetadata.TablaProyectoMetadata._ID+ " FROM "+ProyectoDBMetadata.TABLA_PROYECTO,null);
        Integer idPry= 0;
        if(cursorPry.moveToFirst()){
            idPry=cursorPry.getInt(0);
        }
        Cursor cursor = null;
        Log.d("LAB05-MAIN","PROYECTO : _"+idPry.toString()+" - "+ _SQL_TAREAS_X_PROYECTO);
        cursor = db.rawQuery(_SQL_TAREAS_X_PROYECTO,new String[]{idPry.toString()});
        return cursor;
    }

    public void nuevaTarea(Tarea t){

        EjemploPost.enviarOperacionToAPI("POST",t.getDescripcion().toString(),t.getResponsable().getId(),t.getHorasEstimadas(),t.getProyecto().getId(),t.getMinutosTrabajados(),t.getPrioridad().getId());
        //Log.d("TEST-ARR","NUEVAtAREA: "+t.getId());


        SQLiteDatabase mydb =dbHelper.getWritableDatabase();
        mydb.beginTransaction();
        try{

            String consulta = "INSERT INTO " +  ProyectoDBMetadata.TABLA_TAREAS+ "(" +ProyectoDBMetadata.TablaTareasMetadata.TAREA
                    +","+ProyectoDBMetadata.TablaTareasMetadata.HORAS_PLANIFICADAS +","+ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS
                    +","+ProyectoDBMetadata.TablaTareasMetadata.PRIORIDAD +","+ ProyectoDBMetadata.TablaTareasMetadata.RESPONSABLE
                    +","+ProyectoDBMetadata.TablaTareasMetadata.PROYECTO+") VALUES ('"+t.getDescripcion()+"',"+t.getHorasEstimadas()
                    +","+t.getMinutosTrabajados()+","+t.getPrioridad().getId()+","+ t.getResponsable().getId()+","+t.getProyecto().getId()+");";
            Log.d("LAB05-MAIN","INSERCION DE UNA FILA: "+consulta);
            mydb.rawQuery(consulta,null);
            mydb.execSQL(consulta);
            mydb.setTransactionSuccessful();



        }catch(SQLException e){
            Log.d("LAB05-MAIN","INSERCION DE UNA FILA: _"+e.toString());
        }
        finally {
            mydb.endTransaction();
        }
    }

    public void editarTarea(int idTarea, int nuevasHorasEstimadas, String nuevaDesc, Usuario nuevoUsuario  ) {



        Integer nuevasHoras = nuevasHorasEstimadas;
        EjemploPost.enviarOperacionToAPIUPDATE(idTarea,"PUT",nuevasHoras,nuevaDesc,nuevoUsuario.getId());


        SQLiteDatabase mydb =dbHelper.getWritableDatabase();
        mydb.beginTransaction();
        try{

            String consulta = "UPDATE " + ProyectoDBMetadata.TABLA_TAREAS+ " SET " +ProyectoDBMetadata.TablaTareasMetadata.TAREA
                    +"='"+nuevaDesc+"',"+ProyectoDBMetadata.TablaTareasMetadata.HORAS_PLANIFICADAS +"="+nuevasHorasEstimadas+","
                    +ProyectoDBMetadata.TablaTareasMetadata.RESPONSABLE+"="+nuevoUsuario.getId() +" WHERE "
                    +ProyectoDBMetadata.TablaTareasMetadata._ID+"="+idTarea+";";


            mydb.rawQuery(consulta,null);
            mydb.execSQL(consulta);
            mydb.setTransactionSuccessful();

        }catch(SQLException e){
            Log.d("LAB05-MAIN","Edicion de una fila: _"+e.toString());
        }
        finally {
            mydb.endTransaction();
        }
    }

    public void actualizarTarea(int id, long tiempoTrabajado){

        SQLiteDatabase mydb =dbHelper.getWritableDatabase();
        long tiempoNuevo = 0;
        ContentValues valores = new ContentValues();
        String consulta = "SELECT "+ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS+" FROM "+ProyectoDBMetadata.TABLA_TAREAS + " WHERE " +ProyectoDBMetadata.TablaPrioridadMetadata._ID + " = " + Integer.toString(id);
        System.out.println("long: "+consulta);
        Cursor cursorPry = mydb.rawQuery(consulta,null);

        if(cursorPry.moveToFirst()){
            System.out.println("long: "+ cursorPry.getString(0));
            tiempoNuevo = tiempoTrabajado + cursorPry.getLong(0);
        }

        valores.put(ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS,tiempoNuevo);
        mydb.update(ProyectoDBMetadata.TABLA_TAREAS, valores, "_id=?", new String[]{Integer.toString(id)});

    }

    public void borrarTarea(int idTarea){
        EjemploPost.enviarOperacionToAPIDELETE("DELETE",idTarea);


        SQLiteDatabase mydb =dbHelper.getWritableDatabase();
        mydb.beginTransaction();
        try{
            String consulta = "DELETE FROM " + ProyectoDBMetadata.TABLA_TAREAS+ " WHERE "
                    +ProyectoDBMetadata.TablaTareasMetadata._ID+"="+idTarea+";";

            mydb.rawQuery(consulta,null);
            mydb.execSQL(consulta);
            mydb.setTransactionSuccessful();

        }catch(SQLException e){
            Log.d("LAB05-MAIN","Borrado de una fila: _"+e.toString());
        }
        finally {
            mydb.endTransaction();
        }

    }

    public void tareasConMasDesvios(){
        SQLiteDatabase mydb =dbHelper.getReadableDatabase();
        mydb.beginTransaction();
        try{
            String consulta = "SELECT "+ProyectoDBMetadata.TablaTareasMetadata.TAREA+" FROM " +ProyectoDBMetadata.TABLA_TAREAS + " WHERE "
                    +ProyectoDBMetadata.TablaTareasMetadata.HORAS_PLANIFICADAS+"-"+ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS +"<0;";

            mydb.rawQuery(consulta,null);
            mydb.execSQL(consulta);
            mydb.setTransactionSuccessful();

        }catch(SQLException e){
            Log.d("LAB05-MAIN","Consulta de tareas con mas desvios: _"+e.toString());
        }
        finally {
            mydb.endTransaction();
        }

    }

    public List<Prioridad> listarPrioridades(){
        return null;
    }

    public List<Usuario> listarUsuarios(){
        SQLiteDatabase mydb =dbHelper.getWritableDatabase();
        List<Usuario> resultado = new ArrayList<>();
        String consulta = "SELECT * FROM " + ProyectoDBMetadata.TABLA_USUARIOS;
        Cursor cursorPry = mydb.rawQuery(consulta,null);

        cursorPry.moveToFirst();
        do{
            Usuario usuario = new Usuario();
            usuario.setId(cursorPry.getInt(0));
            usuario.setNombre(cursorPry.getString(1));
            usuario.setCorreoElectronico(cursorPry.getString(2));
            resultado.add(usuario);
        }while(cursorPry.moveToNext());

        Cursor cursor = null;

        return resultado;
    }

    public Usuario obtenerUsuario(String nombre){
        SQLiteDatabase mydb =dbHelper.getWritableDatabase();
        String consulta = "SELECT * FROM " + ProyectoDBMetadata.TABLA_USUARIOS + " WHERE " + ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO + " = '" + nombre + "'";
        Cursor cursorPry = mydb.rawQuery(consulta,null);
        cursorPry.moveToFirst();
            Usuario usuario = new Usuario();
            usuario.setId(cursorPry.getInt(0));
            usuario.setNombre(cursorPry.getString(1));
            usuario.setCorreoElectronico(cursorPry.getString(2));
        return usuario;
    }
    public Proyecto obtenerProyecto(){
        SQLiteDatabase mydb =dbHelper.getWritableDatabase();
        String consulta = "SELECT * FROM " + ProyectoDBMetadata.TABLA_PROYECTO;
        Cursor cursorPry = mydb.rawQuery(consulta,null);
        cursorPry.moveToFirst();
        Proyecto proyecto = new Proyecto();
        proyecto.setId(cursorPry.getInt(0));
        proyecto.setNombre(cursorPry.getString(1));
        return proyecto;
    }
    public void finalizar(Integer idTarea){
        //Establecemos los campos-valores a actualizar
        ContentValues valores = new ContentValues();
        valores.put(ProyectoDBMetadata.TablaTareasMetadata.FINALIZADA,1);
        SQLiteDatabase mydb =dbHelper.getWritableDatabase();
        mydb.update(ProyectoDBMetadata.TABLA_TAREAS, valores, "_id=?", new String[]{idTarea.toString()});

    }

    public List<Tarea> listarDesviosPlanificacion(Boolean soloTerminadas,Integer desvioMaximoMinutos){
        // retorna una lista de todas las tareas que tardaron más (en exceso) o menos (por defecto)
        // que el tiempo planificado.
        // si la bandera soloTerminadas es true, se busca en las tareas terminadas, sino en todas.
        return null;
    }


    public void nuevoUser(Usuario u) {

        SQLiteDatabase mydb =dbHelper.getWritableDatabase();
        mydb.beginTransaction();
        try{

            String consulta = "INSERT INTO " +  ProyectoDBMetadata.TABLA_USUARIOS+ "(" +ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO
                    +","+ProyectoDBMetadata.TablaUsuariosMetadata.MAIL+ ") VALUES ('"+u.getNombre()+"','"+u.getCorreoElectronico()+"');";
            Log.d("LAB05-MAIN","INSERCION DE UNA USUARIO: "+consulta);
            mydb.rawQuery(consulta,null);
            mydb.execSQL(consulta);
            mydb.setTransactionSuccessful();



        }catch(SQLException e){
            Log.d("LAB05-MAIN","INSERCION DE UNA FILA: _"+e.toString());
        }
        finally {
            mydb.endTransaction();
        }
    }
}
