package dam.isi.frsf.utn.edu.ar.lab05;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoDAO;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Usuario;

public class AltaTareaActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button btnGuardar = (Button)findViewById(R.id.btnGuardar);
        ProyectoDAO myDao = new ProyectoDAO(this);
        EditText editDescripcion = (EditText)findViewById(R.id.editText);
        EditText horasEstimadas = (EditText)findViewById(R.id.editText2);
        Spinner responsable = (Spinner) findViewById(R.id.spinner);
        ArrayList<String> listaResponsables = new ArrayList<>();
        List<Usuario> usuarios = myDao.listarUsuarios();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listaResponsables);
        setContentView(R.layout.activity_alta_tarea);
        System.out.println( "ASKDJASKDHJKSDAJKLDHJKADSKA" + usuarios.size());
        for(int i = 0; i < usuarios.size(); i++){
            listaResponsables.add(usuarios.get(i).getNombre());
        }

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        responsable.setAdapter(spinnerAdapter);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


}
