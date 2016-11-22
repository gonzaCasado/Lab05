package dam.isi.frsf.utn.edu.ar.lab05;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

import dam.isi.frsf.utn.edu.ar.lab05.dao.EjemploPost;
import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoDAO;
import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoDBMetadata;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Proyecto;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Tarea;

/**
 * Created by Administrador on 21/11/2016.
 */
public class AdapterTarea extends ArrayAdapter<Tarea> {

    private LayoutInflater inflater;
    private Context contexto;
    private long tiempoActual;
    private ProyectoDAO myDao;

    public AdapterTarea(Context context, List<Tarea> listaTareas) {
        super(context, R.layout.fila_tarea,listaTareas);
        inflater = LayoutInflater.from(context);
        contexto=context;
    }


    public View newView(Context context, ViewGroup viewGroup) {
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vista = inflater.inflate(R.layout.fila_tarea,viewGroup,false);
        return vista;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        View row = view;
        if (row == null) row = inflater.inflate(R.layout.fila_tarea, parent, false);

        TextView nombre= (TextView) row.findViewById(R.id.tareaTitulo);
        TextView tiempoAsignado= (TextView) row.findViewById(R.id.tareaMinutosAsignados);
        final TextView tiempoTrabajado= (TextView) row.findViewById(R.id.tareaMinutosTrabajados);
        TextView prioridad= (TextView) row.findViewById(R.id.tareaPrioridad);
        TextView responsable= (TextView) row.findViewById(R.id.tareaResponsable);
        CheckBox finalizada = (CheckBox)  row.findViewById(R.id.tareaFinalizada);
        ImageButton botonTacho = (ImageButton) row.findViewById(R.id.imageButton);

        Integer calculo = (this.getItem(position).getHorasEstimadas()*60);
        nombre.setText(this.getItem(position).getDescripcion().toString());
        tiempoAsignado.setText( calculo.toString()+ " minutos");
        tiempoTrabajado.setText(this.getItem(position).getMinutosTrabajados().toString()+ " minutos");
        prioridad.setText(this.getItem(position).getPrioridad().getId().toString());
        responsable.setText(this.getItem(position).getResponsable().getId().toString());
        finalizada.setText(this.getItem(position).getTerminada().toString());

        final Button btnFinalizar = (Button)   row.findViewById(R.id.tareaBtnFinalizada);
        final Button btnEditar = (Button)   row.findViewById(R.id.tareaBtnEditarDatos);
        final ToggleButton btnEstado = (ToggleButton) row.findViewById(R.id.tareaBtnTrabajando);

        finalizada.setChecked(this.getItem(position).getTerminada()==true);
        finalizada.setTextIsSelectable(false);

        btnEditar.setTag(getItem(position).getId());
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Integer idTarea= (Integer) view.getTag();
                Intent intEditarAct = new Intent(contexto,AltaTareaActivity.class);
                intEditarAct.putExtra("ID_TAREA",idTarea);
                contexto.startActivity(intEditarAct);

            }
        });

        btnEstado.setTag(getItem(position).getId());
        btnEstado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnEstado.isChecked())
                    tiempoActual = System.currentTimeMillis();
                else {
                    tiempoActual = (System.currentTimeMillis() - tiempoActual) / 5000;
                    final Integer idTarea= (Integer) view.getTag();

                    //myDao.actualizarTarea(idTarea,tiempoActual);
                    EjemploPost.actualizarTarea("PUT",idTarea,tiempoActual);

                    Intent intentActualizar = new Intent(contexto,MainActivity.class);
                    contexto.startActivity(intentActualizar);
                }
            }
        });

        btnFinalizar.setTag(getItem(position).getId());
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

        botonTacho.setTag(getItem(position).getId());
        botonTacho.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                EjemploPost.enviarOperacionToAPIDELETE("DELETE",getItem(position).getId());

                final Integer idTarea= (Integer) view.getTag();
                //Proyecto proyecto = myDao.obtenerProyecto();
                //myDao.borrarTarea(idTarea);

                Intent intentActualizar = new Intent(contexto,MainActivity.class);
                contexto.startActivity(intentActualizar);
            }
        });

        return (row);

    }


}
