package com.example.meyss.monecole.Activities.EspaceParent;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.meyss.monecole.Entities.Classe;
import com.example.meyss.monecole.Entities.Eleve;
import com.example.meyss.monecole.Entities.Matiere;
import com.example.meyss.monecole.Entities.Personne;
import com.example.meyss.monecole.Entities.UserLoged;
import com.example.meyss.monecole.Entities.sceance;
import com.example.meyss.monecole.R;
import com.github.eunsiljo.timetablelib.data.TimeData;
import com.github.eunsiljo.timetablelib.data.TimeGridData;
import com.github.eunsiljo.timetablelib.data.TimeTableData;
import com.github.eunsiljo.timetablelib.view.TimeTableView;
import com.github.eunsiljo.timetablelib.viewholder.TimeTableItemViewHolder;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

    public class Emploi extends Fragment {

    String url = "http://"+ UserLoged.url+"/findemploiByIdclasse/";
    ArrayList<sceance> seance = new ArrayList<sceance>();
    ArrayList<Matiere> matiere = new ArrayList<Matiere>();
    ArrayList<Classe> classe = new ArrayList<Classe>();
    ArrayList<Personne> personne = new ArrayList<Personne>();
    TimeTableView timeTable;
    private long mNow = 0;
    private List<String> mShortHeaders = Arrays.asList("lundi", "mardi", "mercredi", "jeudi", "vendredi", "samedi");

    public Emploi() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       final View root = inflater.inflate(R.layout.activity_emploi, container, false);

        RequestQueue queue= Volley.newRequestQueue(getActivity());


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url+UserLoged.id_classe_eleve, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray array = new JSONArray(response.toString());

                    for (int i=0;i < array.length();i++) {

                        JSONObject o = array.getJSONObject(i);
                        JSONObject matiereObj = o.getJSONObject("matiere");
                        JSONObject emploiObj = o.getJSONObject("emploi");
                        JSONObject classeObj = emploiObj.getJSONObject("classe");
                        JSONObject personneObj = o.getJSONObject("personne");
                        System.out.println("hani hnééééé-----------------------------------> "+o);
                        sceance s = new sceance();
                        s.setJour(o.getString("jour"));
                        /*String dateStr = o.getString("date");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date dateSeance = sdf.parse(dateStr);
                        s.setDate(dateSeance);*/
                        s.setHeureDeb(o.getString("heureDeb"));
                        s.setHeureFin(o.getString("heureFin"));
                        Matiere m = new Matiere();
                        m.setId(matiereObj.getInt("id"));
                        m.setNom(new String(matiereObj.getString("nom").getBytes("ISO-8859-1"), "UTF-8"));
                        m.setCoefficient(matiereObj.getDouble("coefficient"));
                        Classe c = new Classe();
                        c.setId(classeObj.getInt("id"));
                        c.setNiveau(classeObj.getInt("niveau"));
                        c.setNom(classeObj.getString("nom"));
                        Personne p = new Personne();
                        p.setNom(personneObj.getString("nom"));
                        p.setPrenom(personneObj.getString("prenom"));
                        p.setMail(personneObj.getString("mail"));
                        p.setImg(personneObj.getString("image"));
                        seance.add(s);
                        matiere.add(m);
                        classe.add(c);
                        personne.add(p);


                    }
                    timeTable = (TimeTableView)root.findViewById(R.id.timeTable);
                    timeTable.setStartHour(8);

                    timeTable.setShowHeader(true);
                    timeTable.setTableMode(TimeTableView.TableMode.SHORT);

                    timeTable.setTimeTable(mNow, getSamples(mNow, mShortHeaders, matiere,seance));
                    timeTable.setOnTimeItemClickListener(new TimeTableItemViewHolder.OnTimeItemClickListener() {
                        @Override
                        public void onTimeItemClick(View view, int position, TimeGridData item) {
                            TimeData time = item.getTime();
                            Toast.makeText(getContext(),
                                    time.getTitle() + ", " + new DateTime(time.getStartMills()).toString() +
                                            " ~ " + new DateTime(time.getStopMills()).toString(),Toast.LENGTH_SHORT);
                        }
                    });



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


        System.out.println("titre matiere hné : "+matiere);



        return root;
    }

    private ArrayList<TimeTableData> getSamples(long date, List<String> headers, List<Matiere> titles,List<sceance> seance){
        TypedArray colors_table = getResources().obtainTypedArray(R.array.colors_table);
        TypedArray colors_table_light = getResources().obtainTypedArray(R.array.colors_table_light);

        ArrayList<TimeTableData> tables = new ArrayList<>();
        for(int i=0; i<headers.size(); i++){
            ArrayList<TimeData> values = new ArrayList<>();

            for(int j=0; j<seance.size(); j++){
                DateTime start = new DateTime(date);
                DateTime end = new DateTime(date);
                if (seance.get(j).getJour().equals(headers.get(i))) {
                for (int k=0 ; k<titles.size();k++) {
                    if(k==j){


                    int color = colors_table_light.getResourceId(j, 0);
                    int textColor = R.color.black;
                        start = start.plusMinutes((int)((Integer.valueOf(seance.get(j).getHeureDeb().substring(0, 2))  ) - 1) * 60);
                        end = end.plusMinutes((int)((Integer.valueOf(seance.get(j).getHeureFin().substring(0, 2))  ) - 1) * 60);
                            System.out.println(i + " " + j + "<= JOUR => " + seance.get(j).getJour()+" nom : "+titles.get(k).getNom()+" "+start.getMillis()+" - "+end.getMillis());

                            TimeData timeData = new TimeData(k, titles.get(k).getNom(), color, textColor, start.getMillis(), end.getMillis());

                            values.add(timeData);

                            //start = start.plus((long) (Integer.valueOf(seance.get(j).getHeureDeb().substring(0, 2)) ) );
                           // end = end.plusMinutes((int) (Integer.valueOf(seance.get(j).getHeureFin().substring(0, 2)) *60)+1 );
                    }
                    }
                }

            }

            tables.add(new TimeTableData(headers.get(i), values));
        }
        return tables;
    }

    private long getMillis(String day){
        DateTime date = getDateTimePattern().parseDateTime(day);
        return date.getMillis();
    }

    private DateTimeFormatter getDateTimePattern(){
        return DateTimeFormat.forPattern("HH:mm:ss");
    }


}
