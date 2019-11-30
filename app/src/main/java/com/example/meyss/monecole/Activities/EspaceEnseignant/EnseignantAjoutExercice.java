package com.example.meyss.monecole.Activities.EspaceEnseignant;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.meyss.monecole.Activities.login;
import com.example.meyss.monecole.Entities.UserLoged;
import com.example.meyss.monecole.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EnseignantAjoutExercice extends Fragment {

    ImageButton datepicker;
    TextView date;
    EditText description;
    Button ajout;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    String url = "http://"+ UserLoged.url+"/findSeance/";
    String url1 = "http://"+ UserLoged.url+"/ajout/Exercice";
    String url2 = "http://"+ UserLoged.url+"/findCahier/";
    JSONObject json;


    public EnseignantAjoutExercice() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_enseignant_ajout_exercice, container, false);

        description = (EditText) root.findViewById(R.id.consigne);
        datepicker = (ImageButton) root.findViewById(R.id.buttonDate);
        date = (TextView) root.findViewById(R.id.titre_date);
        ajout = (Button) root.findViewById(R.id.ajout);

        GetSeanceReq(UserLoged.userConnected.getId());
        GetCahierReq(UserLoged.id_classe_eleve);

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
                if(!description.getText().equals("") && !date.getText().equals("")){
                    ajout.setVisibility(View.VISIBLE);
                }
                System.out.println(description.getText());
            }
        };

        ajout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<>();
                params.put("consigne", description.getText().toString());
                params.put("date_rendu",date.getText().toString());
                Log.d("Json","Json"+params);
                json = new JSONObject(params);
                insertExercice(UserLoged.id_cahier,UserLoged.id_matiere,UserLoged.userConnected.getId());
            }
        });

        return root;
    }

    private void GetSeanceReq(int id){

        RequestQueue queue= Volley.newRequestQueue(getContext());


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url+id+"/"+UserLoged.id_classe_eleve, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response.toString());
                    for (int i=0;i < array.length();i++) {
                        JSONObject o = array.getJSONObject(i);
                        JSONObject matiereObj = o.getJSONObject("matiere");
                        UserLoged.id_matiere = matiereObj.getInt("id");
                    }
                }  catch (Exception e) {
                    System.out.println(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Verifier votre connection",Toast.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);
    }

    private void GetCahierReq(int id){

        RequestQueue queue= Volley.newRequestQueue(getContext());


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url2+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response.toString());
                    for (int i=0;i < array.length();i++) {
                        JSONObject o = array.getJSONObject(i);
                        UserLoged.id_cahier = o.getInt("id");
                    }
                }  catch (Exception e) {
                    System.out.println(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Verifier votre connection",Toast.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);
    }

    private void insertExercice(int idCahier,int idMatiere,int idPersonne){
        RequestQueue queue= Volley.newRequestQueue(getContext());
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url1+idCahier+"/"+idMatiere+"/"+idPersonne,json , new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response+"++++++++++++++++++++");
                Toast.makeText(getContext(),"Exercice Ajouter avec succès",Toast.LENGTH_LONG).show();

            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("erreur");

                    }
                }
        );

        queue.add(postRequest);
    }


}
