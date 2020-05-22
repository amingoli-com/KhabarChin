package ir.goliforoshani.sms.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

import ir.goliforoshani.sms.R;

public class AddNumberDialog extends AlertDialog implements
        android.view.View.OnClickListener {

    private Activity c;
    private listener listener;
    private EditText editText;
    private Button submit;

    public AddNumberDialog(Activity a, listener listeners) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.listener = listeners;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getWindow()).clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        setContentView(R.layout.item_dialog_add_number);
        editText = findViewById(R.id.editText);
        editText.requestFocus();
        if (editText.hasFocusable()){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (editText.getText().length() < 2){
            Toast.makeText(c,c.getResources().getString( R.string.warm_number_length_less_then_two_digits,"هفت"), Toast.LENGTH_SHORT).show();
        }else {
            listener.result(editText.getText().toString().trim());
            dismiss();
        }

    }

    public interface listener{
        void result(String number);
    }
}