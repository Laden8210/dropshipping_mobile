package com.example.dropshipping.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dropshipping.R;
import com.example.dropshipping.api.PostCallback;
import com.example.dropshipping.api.PostTask;
import com.example.dropshipping.model.User;
import com.example.dropshipping.util.Messenger;
import com.example.dropshipping.util.Miner;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;


public class RegisterActivity extends AppCompatActivity implements PostCallback {

    private TextInputEditText firstName, lastName, email, phone, birthdate, password, confirmPassword;
    private Spinner genderSpinner;
    private MaterialButton registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);


        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        birthdate = findViewById(R.id.birthDate);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);

        genderSpinner = findViewById(R.id.genderSpinner);

        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this::registerAction);




    }

    private void registerAction(View view) {
        String firstName = Miner.getText(this.firstName);
        String lastName = Miner.getText(this.lastName);
        String email = Miner.getText(this.email);
        String phone = Miner.getText(this.phone);
        String birthdate = Miner.getText(this.birthdate);
        String password = Miner.getText(this.password);
        String confirmPassword = Miner.getText(this.confirmPassword);
        String gender = genderSpinner.getSelectedItem().toString();

        User user = new User();

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setBirthdate(birthdate);
        user.setPassword(password);
        user.setConfirmPassword(confirmPassword);
        user.setGender(gender);


        try {

        new PostTask(this, this, "Error", "auth/register.php").execute(user.toJson());

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPostSuccess(String responseData) {
        JSONObject jsonError = null;
        try {
            jsonError = new JSONObject(responseData);
            String message = jsonError.getString("message");

        if (jsonError.has("success")) {
            String successValue = jsonError.getString("success");
            Messenger.showAlertDialog(this, "Success", successValue, "OK").show();
            finish();
        } else if (jsonError.has("error")) {
            String errorValue = jsonError.getString("error");
            Messenger.showAlertDialog(this, "Error", errorValue, "OK").show();
        } else {
            Messenger.showAlertDialog(this, "Info", message, "OK").show();
        }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void onPostError(String errorMessage) {
        Messenger.showAlertDialog(this, "Error", errorMessage, "OK").show();
    }
}