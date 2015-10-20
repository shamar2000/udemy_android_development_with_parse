package com.shamar.themes.livestatus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    protected EditText loginActivityUsernameField;
    protected EditText loginActivityPasswordField;
    protected Button loginActivityRegisterBtn;
    protected Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initDatastore();
        initViews();
    }

    private void initDatastore() {
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "5CR40h1Beagl4vG8RYpPh2CMj2RB5IxC56L0v3Bb", "ZwWPoVwN2J3gYq20s9v97ElIwCq27OPRl7mSIFvc");
    }

    private void initViews() {
        loginActivityPasswordField = (EditText) findViewById(R.id.loginActivity_password_field);
        loginActivityUsernameField = (EditText) findViewById(R.id.loginActivity_username_field);
        loginActivityRegisterBtn = (Button) findViewById(R.id.loginActivity_register_btn);
        loginBtn = (Button) findViewById(R.id.login_btn);

        loginActivityRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerActivity = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerActivity);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (onLoginSuccess()) {
                   String username = loginActivityUsernameField.getText().toString().trim();
                   String password = loginActivityPasswordField.getText().toString().trim();
                   login(username,password);
               } else {
                   onLoginFailed();
               }
            }
        });
    }

    private boolean onLoginSuccess() {
        boolean onLogin = true;

        if (loginActivityUsernameField.getText().toString().isEmpty()
                || loginActivityPasswordField.getText().toString().isEmpty()) {
            onLogin = false;
        }
        return onLogin;
    }

    private void login(final String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    // the user has successfully logged in!
                    Toast.makeText(getBaseContext(), "Welcome Back " + username, Toast.LENGTH_SHORT).show();
                    Intent homepageActivity = new Intent(LoginActivity.this, HomepageActivity.class);
                    startActivity(homepageActivity);
                } else {
                    // something went wrong...tell the user
                    AlertDialog.Builder errorLoggingInDialog = new AlertDialog.Builder(LoginActivity.this);
                    errorLoggingInDialog.setMessage(e.getMessage());
                    errorLoggingInDialog.setTitle("Oops, Try Again.");
                    errorLoggingInDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // close the dialog
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = errorLoggingInDialog.create();
                    dialog.show();

                    /**
                     * Instead of the bulky AlertDialog, we could have just made a Toast :\
                     *
                     */
                    // Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void onLoginFailed() {
        Toast.makeText(this, "Error logging in. Please try again.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
