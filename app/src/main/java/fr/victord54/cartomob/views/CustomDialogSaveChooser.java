package fr.victord54.cartomob.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fr.victord54.cartomob.R;

public class CustomDialogSaveChooser extends Dialog {
    public interface FileListener {
        void nameFileTyped(String name);
    }

    private final Context context;
    private final FileListener fileListener;

    private RecyclerView recyclerView;

    public CustomDialogSaveChooser(@NonNull Context context, FileListener fl) {
        super(context);
        this.context = context;
        fileListener = fl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_save_chooser);

        recyclerView = findViewById(R.id.custom_dialog_save_chooser_recycler_view);
    }
}
