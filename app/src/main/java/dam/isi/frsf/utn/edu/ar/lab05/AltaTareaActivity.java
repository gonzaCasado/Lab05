package dam.isi.frsf.utn.edu.ar.lab05;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoDAO;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Prioridad;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Proyecto;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Tarea;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Usuario;

public class AltaTareaActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_tarea);

    }

    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_alta_tarea);
        Button btnGuardar = (Button)findViewById(R.id.btnGuardar);
        Button btnCancelar = (Button)findViewById(R.id.btnCanelar);

        final EditText editDescripcion = (EditText)findViewById(R.id.editText);
        final EditText horasEstimadas = (EditText)findViewById(R.id.editText2);
        final Spinner responsable = (Spinner) findViewById(R.id.spinner);
        final ArrayList<String> listaResponsables = new ArrayList<>();
        final ProyectoDAO myDao = new ProyectoDAO(this);
        List<Usuario> usuarios = myDao.listarUsuarios();

        Bundle extras = getIntent().getExtras();
        final int tareaAEditar = extras.getInt("ID_TAREA");

        for(int i = 0; i < usuarios.size(); i++){
            listaResponsables.add(usuarios.get(i).getNombre());
        }
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,listaResponsables);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        responsable.setAdapter(spinnerAdapter);

        if(tareaAEditar!=0) {

            //Toast.makeText(this,"Editar tarea: "+tareaAEditar,Toast.LENGTH_LONG).show();

            btnGuardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Proyecto proyecto = myDao.obtenerProyecto();
                    Usuario user = myDao.obtenerUsuario(responsable.getSelectedItem().toString());
                    if((horasEstimadas.getText().length()!=0)&&editDescripcion.getText().length()!=0){
                        myDao.editarTarea(tareaAEditar,Integer.parseInt(horasEstimadas.getText().toString()), editDescripcion.getText().toString(), user );
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Ingrese descripción y horas estimadas",Toast.LENGTH_LONG).show();
                    }
                    /*Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
*/
                }
            });


        }
        else{

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Proyecto proyecto = myDao.obtenerProyecto();
                Prioridad prioridad = new Prioridad(4, "Urgente");
                Usuario user = myDao.obtenerUsuario(responsable.getSelectedItem().toString());
                if((horasEstimadas.getText().length()!=0)&&editDescripcion.getText().length()!=0){
                    Tarea tarea = new Tarea (1, false, Integer.parseInt(horasEstimadas.getText().toString()), 0, editDescripcion.getText().toString(), proyecto, prioridad, user);
                    myDao.nuevaTarea(tarea);
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Ingrese descripción y horas estimadas",Toast.LENGTH_LONG).show();
                }
            }
        });

        }


        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
