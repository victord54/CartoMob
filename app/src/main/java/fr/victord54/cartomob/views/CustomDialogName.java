package fr.victord54.cartomob.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import fr.victord54.cartomob.R;

public class CustomDialogName extends Dialog {
    public interface NameListener {
        void nameTyped(String name);
    }

    private EditText textInput;
    private Button buttonOk;
    private final Context context;
    private final NameListener listener;

    public CustomDialogName(@NonNull Context context, NameListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_name);
        textInput = findViewById(R.id.custom_dialog_name_edit);
        buttonOk = findViewById(R.id.custom_dialog_name_btn_ok);
        buttonOk.setOnClickListener(view -> {
            String fullName = this.textInput.getText().toString();

            if (fullName.isEmpty()) {
                Toast.makeText(this.context, "Warning cannot create no named room !", Toast.LENGTH_LONG).show();
                return;
            }
            this.dismiss(); // Close Dialog

            if (this.listener != null) {
                this.listener.nameTyped(fullName);
            }

        });
    }
}

