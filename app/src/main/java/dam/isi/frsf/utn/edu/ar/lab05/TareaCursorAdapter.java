package dam.isi.frsf.utn.edu.ar.lab05;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

import dam.isi.frsf.utn.edu.ar.lab05.dao.EjemploPost;
import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoDAO;
import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoDBMetadata;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Proyecto;


public class TareaCursorAdapter extends CursorAdapter {
    private LayoutInflater inflador;
    private ProyectoDAO myDao;
    private Context contexto;
    private long tiempoActual;
    public static int filaSeleccionada;
    public TareaCursorAdapter (Context contexto, Cursor c, ProyectoDAO dao) {
        super(contexto, c, false);
        myDao= dao;
        this.contexto = contexto;
    }

    @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        inflador = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vista = inflador.inflate(R.layout.fila_tarea,viewGroup,false);
        return vista;
    }




    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        //obtener la posicion de la fila actual y asignarla a los botones y checkboxes
        int pos = cursor.getPosition();

        List listaTareas = EjemploPost.getTareas();

        // Referencias UI.
        TextView nombre= (TextView) view.findViewById(R.id.tareaTitulo);
        TextView tiempoAsignado= (TextView) view.findViewById(R.id.tareaMinutosAsignados);
        final TextView tiempoTrabajado= (TextView) view.findViewById(R.id.tareaMinutosTrabajados);
        TextView prioridad= (TextView) view.findViewById(R.id.tareaPrioridad);
        TextView responsable= (TextView) view.findViewById(R.id.tareaResponsable);
        CheckBox finalizada = (CheckBox)  view.findViewById(R.id.tareaFinalizada);
        ImageButton botonTacho = (ImageButton) view.findViewById(R.id.imageButton);


        final Button btnFinalizar = (Button)   view.findViewById(R.id.tareaBtnFinalizada);
        final Button btnEditar = (Button)   view.findViewById(R.id.tareaBtnEditarDatos);
        final ToggleButton btnEstado = (ToggleButton) view.findViewById(R.id.tareaBtnTrabajando);

        nombre.setText(cursor.getString(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.TAREA)));
        Integer horasAsigandas = cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.HORAS_PLANIFICADAS));
        tiempoAsignado.setText(horasAsigandas*60 + " minutos");

        Integer minutosAsigandos = cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS));
        tiempoTrabajado.setText(minutosAsigandos+ " minutos");
        String p = cursor.getString(cursor.getColumnIndex(ProyectoDBMetadata.TablaPrioridadMetadata.PRIORIDAD_ALIAS));
        prioridad.setText(p);
        responsable.setText(cursor.getString(cursor.getColumnIndex(ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO_ALIAS)));
        finalizada.setChecked(cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.FINALIZADA))==1);
        finalizada.setTextIsSelectable(false);

        btnEditar.setTag(cursor.getInt(cursor.getColumnIndex("_id")));
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Integer idTarea= (Integer) view.getTag();
                Intent intEditarAct = new Intent(contexto,AltaTareaActivity.class);
                intEditarAct.putExtra("ID_TAREA",idTarea);
                context.startActivity(intEditarAct);

            }
        });
        btnEstado.setTag(cursor.getInt(cursor.getColumnIndex("_id")));
        btnEstado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnEstado.isChecked())
                    tiempoActual = System.currentTimeMillis();
                else {
                    tiempoActual = (System.currentTimeMillis() - tiempoActual) / 5000;
                    final Integer idTarea= (Integer) view.getTag();

                    myDao.actualizarTarea(idTarea,tiempoActual);

                    Intent intentActualizar = new Intent(contexto,MainActivity.class);
                    context.startActivity(intentActualizar);
                }
            }
        });

        btnFinalizar.setTag(cursor.getInt(cursor.getColumnIndex("_id")));
        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Integer idTarea= (Integer) view.getTag();
                Thread backGroundUpdate = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("LAB05-MAIN","finalizar tarea : --- "+idTarea);
                        myDao.finalizar(idTarea);
                    }
                });
                backGroundUpdate.start();
            }
        });

        botonTacho.setTag(cursor.getInt(cursor.getColumnIndex("_id")));
        botonTacho.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                final Integer idTarea= (Integer) view.getTag();
                Proyecto proyecto = myDao.obtenerProyecto();
                myDao.borrarTarea(idTarea);

                Intent intentActualizar = new Intent(contexto,MainActivity.class);
                context.startActivity(intentActualizar);
            }
            });





    }

/*    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) row = inflador.inflate(R.layout.fila_tarea, parent, false);


        row.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                filaSeleccionada = getItem(position).hashCode();
                return false;
            }
        });

        return (row);

    }

    public static int getFilaSeleccionada(){
        return filaSeleccionada;
    }*/
}

