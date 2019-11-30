package com.example.meyss.monecole.Activities.EspaceAdmin;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.meyss.monecole.Entities.Emploi;
import com.example.meyss.monecole.Entities.Matiere;
import com.example.meyss.monecole.Entities.Personne;
import com.example.meyss.monecole.Entities.UserLoged;
import com.example.meyss.monecole.Entities.sceance;
import com.example.meyss.monecole.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AjoutSeance extends Fragment {
    String urlM = "http://" + UserLoged.url + "/matiere";
    String urlEns = "http://" + UserLoged.url + "/AllEnseignant";
    String urSeance = "http://" + UserLoged.url + "/addSeance/";


    private JSONArray result;
    private Spinner matSpinner,enSpinner,jour;
    private ArrayList<String> matiere,enseignant;
    TextView hD,hF;
    EditText input,inputC;


    public AjoutSeance() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.activity_ajout_seance, container, false);

        matSpinner = (Spinner) root.findViewById(R.id.SpinnerMat);
        enSpinner = (Spinner) root.findViewById(R.id.SprinnerEns);
        Button btnAddMat = (Button) root.findViewById(R.id.addMat);
            btnAddMat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog dialogBuilder = new AlertDialog.Builder(getContext()).create();
                    View view = inflater.inflate(R.layout.dialog_add_matiere, null);
                    dialogBuilder.setTitle("Ajouter une matiere");
                    dialogBuilder.setMessage("veuillez remplir les champs");

                     input = (EditText) view.findViewById(R.id.nomMatiere);
                     inputC = (EditText) view.findViewById(R.id.coefMat);
                    Button submit = (Button) view.findViewById(R.id.submitMat);
                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            insertReq();
                            Toast.makeText(getContext(),"Matiere ajoutée",Toast.LENGTH_LONG);
                            dialogBuilder.dismiss();

                           startActivity(new Intent(root.getContext(),ClasseAdmin.class).putExtra("id_classe",getArguments().getInt("id_classe", 0)));
                        }
                    });

                    dialogBuilder.setView(view);

                    dialogBuilder.show();
                }
            });
            enseignant =  new ArrayList<String>();
            matiere =  new ArrayList<String>();
            getENS();
            getMat();


         hD = (TextView) root.findViewById(R.id.EditHd);
         hF = (TextView) root.findViewById(R.id.editHF);
        jour = (Spinner) root.findViewById(R.id.Spinnerjours);

        Calendar calendar = Calendar.getInstance();
        final int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        final int currentMinute = calendar.get(Calendar.MINUTE);

        hD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        String amPm;
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        hD.setText(String.format("%02d:%02d", hourOfDay, minutes) );
                    }
                }, currentHour, currentMinute, false);
                timePickerDialog.show();

            }
        });

        hF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        String amPm;
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        hF.setText(String.format("%02d:%02d", hourOfDay, minutes) );
                    }
                }, currentHour, currentMinute, false);
                timePickerDialog.show();

            }
        });



        Button btnAdd = (Button) root.findViewById(R.id.addsceance);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!hD.getText().equals("cliquer ici"))&&(!hF.getText().equals("cliquer ici"))
                        &&(!jour.getSelectedItem().toString().equals("")) &&(!matSpinner.getSelectedItem().toString().equals(""))
                        &&(!enSpinner.getSelectedItem().toString().equals(""))) {
                    if(getHour(hF.getText().toString())== getHour(hD.getText().toString())+1) {
                        if((getMin(hF.getText().toString())== 0)&& (getMin(hF.getText().toString())== 0)) {
                            sceance sceance = new sceance(hD.getText().toString(), hF.getText().toString(), jour.getSelectedItem().toString());
                            findEnseignant(enSpinner.getSelectedItem().toString(), sceance);

                        }else {
                            Toast.makeText(getContext(),"les Minutes doivent êtres :00",Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getContext(),"les heures de la scéance sont invalides",Toast.LENGTH_LONG).show();
                    }

                }else
                {
                    Toast.makeText(getContext(),"veuillez verifier tous les champs",Toast.LENGTH_LONG).show();
                }

            }
        });

        return root;
    }

    private void reqAdd(sceance seance,Matiere m, Emploi emp) {

        /*jour = jour;
		this.heureDeb = heureDeb;
		this.heureFin = heureFin;
		this.matiere = matiere;
		this.personne*/
        System.out.println(seance.toString());
        System.out.println(m.toString());
        System.out.println(emp.getId());
        RequestQueue queue= Volley.newRequestQueue(getContext());
        Map<String, String> params = new HashMap<String, String>();


        System.out.println(params);
        Log.d("Json","Json"+params);
        JSONObject json = new JSONObject(params);
       /* params.put("heureDeb",hD.getText().toString());
        params.put("heureFin",hD.getText().toString());
        params.put("jour",jour.getSelectedItem().toString());*/
        System.out.println(jour.getSelectedItem().toString());
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, urSeance+ seance.getIdEnseignant()+"/"+hD.getText().toString()
                +"/"+hF.getText().toString()+"/"+jour.getSelectedItem().toString()+"/"+emp.getId()+"/"+m.getId(),json, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response+"++++++++++++++++++++");
                Toast.makeText(getContext(),"Succès ajout seance",Toast.LENGTH_LONG).show();
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error+"----------------");
                        Toast.makeText(getContext(),error+"",Toast.LENGTH_LONG).show();
                    }
                }
        );

        queue.add(postRequest);
    }

    public void getENS() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlEns,
        new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject j = null;
                try {
                    JSONArray jsA = new JSONArray(response);

                    int i = 0;
                    for (i = 0; i < jsA.length(); i++) {
                        Personne p = new Personne();
                        JSONObject o = jsA.getJSONObject(i);
                        p.setId(o.getInt("id"));
                        p.setNom(o.getString("nom"));
                        p.setPrenom(o.getString("prenom"));
                        p.setPassword(o.getString("password"));
                        p.setMail(o.getString("mail"));
                        p.setTel(o.getString("tel"));
                        p.setImg(o.getString("image"));
                        p.setAdresse(o.getString("adresse"));
                        p.setRole(o.getString("role"));
                        System.out.println("ici mail : " + p.getMail());
                        enseignant.add(p.getMail());
                    }
                    enSpinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, enseignant));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    public void getMat() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,urlM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            JSONArray jsA = new JSONArray(response);

                            int i = 0;
                            for (i = 0; i < jsA.length(); i++) {
                                Matiere m = new Matiere();
                                JSONObject o = jsA.getJSONObject(i);
                                m.setId(o.getInt("id"));
                                m.setNom(o.getString("nom"));
                                //p.getCoefficient(o.getDouble("Coefficient"));
                                //System.out.println("ici mail : " + p.getMail());
                                try {
                                    matiere.add(new String(m.getNom().getBytes("ISO-8859-1"), "UTF-8"));

                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                //new String(m.getNom().getBytes("ISO-8859-1"), "UTF-8");
                            }
                            System.out.println(matiere);
                            matSpinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, matiere));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void findEnseignant(String email, final sceance sc) {
        final Personne PERSONNE = new Personne();
        final String findParent = "http://" + UserLoged.url + "/findUserMail/" + email + "/";
        RequestQueue queue = Volley.newRequestQueue(this.getContext());


        StringRequest stringRequest = new StringRequest(Request.Method.GET, findParent, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    JSONArray array = new JSONArray(response);

                    JSONObject o = array.getJSONObject(0);


                    PERSONNE.setId(o.getInt("id"));
                    /*PERSONNE.setNom(o.getString("nom"));
                    PERSONNE.setPrenom(o.getString("prenom"));
                    PERSONNE.setPassword(o.getString("password"));
                    PERSONNE.setMail(o.getString("mail"));
                    PERSONNE.setTel(o.getString("tel"));
                    PERSONNE.setImg(o.getString("image"));
                    PERSONNE.setAdresse(o.getString("adresse"));
                    PERSONNE.setRole(o.getString("role"));*/
                    System.out.println("fi west el findEnseignant " + PERSONNE.toString());
                     sc.setIdEnseignant(o.getInt("id"));
                     getMatiere(sc);



                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("mahouch mawjoud");
                                   }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), "verifier votre connexion", Toast.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);

}

    public void getMatiere(final sceance sc) {
        final Matiere m = new Matiere();
        String urlGetMat = "http://"+ UserLoged.url+"/findMatiereByName/";

        RequestQueue queue = Volley.newRequestQueue(this.getContext());


        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlGetMat+matSpinner.getSelectedItem().toString(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    JSONArray array = new JSONArray(response);

                    JSONObject o = array.getJSONObject(0);


                    m.setId(o.getInt("id"));
                    m.setNom(o.getString("nom"));

                    System.out.println("fi west el matiere " + m.toString());
                    sc.setIdMatiere(o.getInt("id"));
                        getEmploi(sc,m);



                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("mahouch mawjoud");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), "verifier votre connexion", Toast.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);

    }

    public void getEmploi(final sceance sc ,final Matiere m) {
        final Emploi emp = new Emploi();
        System.out.println(getArguments().getInt("id_classe", 0));
        String urlGetEmp = "http://"+ UserLoged.url+"/findEmploiByIdClasse/"+getArguments().getInt("id_classe", 0)+"/";

        RequestQueue queue = Volley.newRequestQueue(this.getContext());


        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlGetEmp, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    JSONArray array = new JSONArray(response);

                    JSONObject o = array.getJSONObject(0);

                    emp.setId(o.getInt("id"));

                    sc.setIdEmploi(o.getInt("id"));
                    System.out.println(emp.getId());
                    System.out.println(m.toString());
                    reqAdd(sc,m,emp);


                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("mahouch mawjoud");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), "verifier votre connexion", Toast.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);
    }


    private void insertReq(){
        String addMat = "http://" + UserLoged.url + "/addMat";
        RequestQueue queue= Volley.newRequestQueue(getContext());
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("nom", input.getText().toString());
        params.put("coefficient",Integer.valueOf(inputC.getText().toString()));

        System.out.println(params);
        JSONObject json = new JSONObject(params);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, addMat,json, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response+"++++++++++++++++++++");
                Toast.makeText(getContext(),"Succès ajout classe",Toast.LENGTH_LONG).show();

            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error+"----------------");
                        Toast.makeText(getContext(),error+"",Toast.LENGTH_LONG).show();
                    }
                }
        );

        queue.add(postRequest);
    }

    public int getHour(String time)
    {

        String[] times = time.split ( ":" );
        int hour = Integer.parseInt ( times[0].trim() );
        int min = Integer.parseInt ( times[1].trim() );
        return hour;
    }
    public int getMin(String time)
    {

        String[] times = time.split ( ":" );
        int min = Integer.parseInt ( times[1].trim() );
        return min;
    }
}
