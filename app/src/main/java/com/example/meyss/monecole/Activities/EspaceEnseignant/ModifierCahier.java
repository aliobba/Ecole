package com.example.meyss.monecole.Activities.EspaceEnseignant;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.meyss.monecole.Activities.EspaceParent.ModificationProfil;
import com.example.meyss.monecole.AesCrypt;
import com.example.meyss.monecole.Entities.UserLoged;
import com.example.meyss.monecole.R;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ModifierCahier extends Fragment {
    ImageButton datepicker;
    TextView date;
    EditText description;
    Button modifier;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    JSONObject json;
    String url = "http://"+ UserLoged.url+"/UpdateExercice";


    public ModifierCahier() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_modifier_cahier, container, false);

        description = (EditText) root.findViewById(R.id.consigne);
        datepicker = (ImageButton) root.findViewById(R.id.buttonDate);
        date = (TextView) root.findViewById(R.id.titre_date);
        modifier = (Button) root.findViewById(R.id.modifier);
        description.setText(UserLoged.exercice.getConsigne().toString());
        date.setText(UserLoged.exercice.getDateRendu().toString());
        //GetSeanceReq(UserLoged.userConnected.getId());
       // GetCahierReq(UserLoged.id_classe_eleve);

        datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(),android.R.style.Theme_Holo_Dialog_MinWidth,mDateSetListener,year,month,day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;

                String d = year+"-"+month+"-"+day;
                date.setText(d);
               /* if(!description.getText().equals("") && !date.getText().equals("")){
                    modifier.setVisibility(View.VISIBLE);
                }*/
                System.out.println(description.getText());
            }
        };

        modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<>();
                params.put("id",String.valueOf(UserLoged.exercice.getId()));
                params.put("consigne", description.getText().toString());
                params.put("date_rendu",date.getText().toString());
                Log.d("Json","Json"+params);
                json = new JSONObject(params);
                UpdateReq();
                //insertExercice(UserLoged.id_cahier,UserLoged.id_matiere,UserLoged.userConnected.getId());
            }
        });

        return root;
    }


    private void UpdateReq(){

        RequestQueue queue= Volley.newRequestQueue(getContext());



        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.PUT, url,json, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response+"++++++++++++++++++++");
                Toast.makeText(getActivity(),"Modification Réussit",Toast.LENGTH_LONG).show();
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //UserGetReq(UserLoged.userConnected.getId());
                        getFragmentManager().beginTransaction().replace(R.id.fragment_E,new CahierTexte()).commit();
                        Toast.makeText(getActivity(),"Modification Réussit",Toast.LENGTH_LONG).show();


                    }
                }
        );

        queue.add(postRequest);
    }
    }
