package es.riberadeltajo.buscaminas_davidpascual;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

public class SpinnerBomba extends DialogFragment {
    OnRespuestaSkin respuesta;
    String mensaje = "";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = getLayoutInflater().inflate(R.layout.nuevo_spinner, null);
        Spinner spinner = v.findViewById(R.id.spinner_bomba);
        Bomba[] bombas = new Bomba[5];
        bombas[0] = new Bomba("Bomba", R.drawable.bomba);
        bombas[1] = new Bomba("Bomba Azul", R.drawable.bomba_azul);
        bombas[2] = new Bomba("Granada", R.drawable.granada);
        bombas[3] = new Bomba("Molotov", R.drawable.molotov);
        bombas[4] = new Bomba("Bomba Atomica", R.drawable.bomba_atomica);
        Adaptador adaptador = new Adaptador(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, bombas);
        spinner.setAdapter(adaptador);
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        b.setTitle("Seleccionar Bomba");
        b.setView(v);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (bombas[position].getNombre().equals("Bomba")) {
                    mensaje = "bomba";
                } else if (bombas[position].getNombre().equals("Bomba Azul")) {
                    mensaje = "azul";
                } else if (bombas[position].getNombre().equals("Granada")) {
                    mensaje = "granada";
                } else if (bombas[position].getNombre().equals("Molotov")) {
                    mensaje = "molotov";
                } else if (bombas[position].getNombre().equals("Bomba Atomica")) {
                    mensaje = "atomica";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        b.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                respuesta.onSkin(mensaje);
            }
        });
        return b.create();
    }

    public interface OnRespuestaSkin {
        public void onSkin(String mensaje);
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        respuesta = (OnRespuestaSkin) context;
    }

    public class Adaptador extends ArrayAdapter<Bomba> {
        Bomba[] bombas;

        public Adaptador(@NonNull Context context, int resource, @NonNull Bomba[] objects) {
            super(context, resource, objects);
            bombas = objects;
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return crearFila(position, convertView, parent);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return crearFila(position, convertView, parent);
        }

        private View crearFila(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View fila = inflater.inflate(R.layout.spinner_bomba, parent, false);

            ImageView imgB = fila.findViewById(R.id.image_view_bomba);
            TextView nom = fila.findViewById(R.id.text_view_bomba);

            imgB.setImageResource(bombas[position].getImageResource());
            nom.setText(bombas[position].getNombre());

            return fila;
        }
    }
}