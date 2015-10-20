package com.shamar.themes.livestatus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterActivity extends AppCompatActivity {

    protected EditText usernameField;
    protected EditText emailField;
    protected EditText passwordField;
    protected Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
    }

    private void initViews() {
        usernameField = (EditText) findViewById(R.id.username_field);
        passwordField = (EditText) findViewById(R.id.password_field);
        emailField = (EditText) findViewById(R.id.email_field);
        registerBtn = (Button) findViewById(R.id.register_btn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSignUpSuccess()) {
                    signUp();
                } else {
                    onSignUpFailed();
                }
            }
        });
    }

    private boolean onSignUpSuccess() {
        boolean onSignup = false;

        if (Patterns.EMAIL_ADDRESS.matcher(emailField.getText().toString()).matches()
                && !usernameField.getText().toString().isEmpty()
                && !passwordField.getText().toString().isEmpty()) {
            onSignup = true;
        }

        return onSignup;
    }

    private void signUp() {
        // register and store the user on our parse database
        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String email = emailField.getText().toString().trim();

        logOutCurrentUser(); // needed to register user
        createNewUser(username,password,email);
    }

    private void logOutCurrentUser() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.logOut();
    }

    private void createNewUser(String username, String password, String email) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Intent homepageActivity = new Intent(RegisterActivity.this, HomepageActivity.class);
                    startActivity(homepageActivity);
                   // Toast.makeText(RegisterActivity.this, "Welcome " + username, Toast.LENGTH_SHORT).show();
                } else {
                    // TODO: something went wrong...
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void onSignUpFailed() {
        Toast.makeText(RegisterActivity.this, "Fields cannot be empty. Try again.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
