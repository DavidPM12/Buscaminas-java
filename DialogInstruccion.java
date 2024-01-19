package es.riberadeltajo.buscaminas_davidpascual;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

public class DialogInstruccion extends DialogFragment {
    public Dialog onCreateDialog(Bundle savedInstanState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Instrucciones");
        builder.setMessage("Cuando pulsas en una casilla, sale un número que identifica cuántas minas hay alrededor: Ten cuidado porque si pulsas en una casilla que tenga una mina escondida, perderás. Si crees o tienes la certeza de que hay una mina, haz un click largo sobre la casilla para señalarla. No hagas un click largo en una casilla donde no hay una mina porque perderás. Ganas una vez que hayas encontrado todas las minas.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return builder.create();
    }
}