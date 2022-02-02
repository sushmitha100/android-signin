package android.demo.learnandroidwithrealapps;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mobsandgeeks.saripaar.QuickRule;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.Max;
import com.mobsandgeeks.saripaar.annotation.Min;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;
import com.mobsandgeeks.saripaar.annotation.Url;

import java.util.List;

public class MainActivity extends AppCompatActivity implements Validator.ValidationListener {

    @NotEmpty
    @Length(min = 3, max = 10)
    private EditText editTextUsername;

    @NotEmpty
    @Password
    @Pattern(regex = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})")
    private EditText editTextPassword;

    @ConfirmPassword
    private EditText editTextConfirmPassword;

    @NotEmpty
    @Pattern(regex =  "^\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})$")
    private EditText editTextPhone;

    @Min(18)
    @Max(120)
    private EditText editTextAge;

    @NotEmpty
    @Email
    private EditText editTextEmail;

    @Url
    private EditText editTextWebsite;

    @Checked
    private CheckBox checkBoxAgree;

    private Button buttonSave;

    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        validator = new Validator(this);
        validator.setValidationListener(this);
        validator.put(editTextUsername, new QuickRule<EditText>() {
            @Override
            public boolean isValid(EditText view) {
                EditText editText = (EditText) view;
                if (editText.getText().toString().equalsIgnoreCase("abc")) {
                    return false;
                }
                return true;
            }

            @Override
            public String getMessage(Context context) {
                return getText(R.string.username_already_exists).toString();
            }
        });
    }

    private void initView() {
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        editTextAge = findViewById(R.id.editTextAge);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextWebsite = findViewById(R.id.editTextWebsite);
        checkBoxAgree = findViewById(R.id.checkBoxAgree);
        buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSave_onClick(view);
            }
        });
    }

    private void buttonSave_onClick(View view) {
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        Toast.makeText(this, "We got it right!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}