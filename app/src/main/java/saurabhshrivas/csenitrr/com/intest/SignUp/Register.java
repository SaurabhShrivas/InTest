package saurabhshrivas.csenitrr.com.intest.SignUp;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import org.json.JSONException;
import org.json.JSONObject;
import saurabhshrivas.csenitrr.com.intest.Home.Home;
import saurabhshrivas.csenitrr.com.intest.R;

public class Register extends AppCompatActivity {

    private Session session;
    private Button register;
    private TextView txtName, txtEmail;
    private EditText etadd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        session = new Session(this);
        register = (Button) findViewById(R.id.btRegisterAdd);
        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        etadd= (EditText) findViewById(R.id.etaddress);
        txtName.setText(getIntent().getStringExtra("name"));
        txtEmail.setText(getIntent().getStringExtra("email"));

        if(session.loggedin()) {
            startActivity(new Intent(Register.this, Home.class));
            finish();

        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    //Creating firebase object
                Firebase ref = new Firebase(Config.FIREBASE_URL);
                final String name = txtName.getText().toString().trim();
                String address = etadd.getText().toString().trim();
                String mail = txtEmail.getText().toString().trim();
                Person person = new Person();
                person.setName(name);
                person.setAddress(address);

                final String mail2=mail.substring(0,mail.length()-4);


                final ProgressDialog pd = new ProgressDialog(Register.this);
                pd.setMessage("Loading...");
                pd.show();
                String url = "https://intest-498a8.firebaseio.com/users.json";

                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String s) {
                        Firebase reference = new Firebase("https://intest-498a8.firebaseio.com/users");

                        if(s.equals("null")) {
                            reference.child(mail2).child("password").setValue(name);
                            Toast.makeText(Register.this, "registration successful", Toast.LENGTH_LONG).show();
                        }
                        else {
                            try {
                                JSONObject obj = new JSONObject(s);

                                if (!obj.has(mail2)) {
                                    reference.child(mail2).child("password").setValue(name);
                                    Toast.makeText(Register.this, "registration successful", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(Register.this, "username already exists", Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        pd.dismiss();
                    }

                },new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println("" + volleyError );
                        pd.dismiss();
                    }
                });

                RequestQueue rQueue = Volley.newRequestQueue(Register.this);
                rQueue.add(request);

                session.setLoggedin(true);
                Intent intent=new Intent(Register.this,Home.class);
                startActivity(intent);
                finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }


}