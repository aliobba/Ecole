package com.example.meyss.monecole.Activities.EspaceEnseignant;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.meyss.monecole.Entities.Abscence;
import com.example.meyss.monecole.Entities.Classe;
import com.example.meyss.monecole.Entities.Eleve;
import com.example.meyss.monecole.Entities.UserLoged;
import com.example.meyss.monecole.Entities.sceance;
import com.example.meyss.monecole.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterEleveAbsent extends ArrayAdapter<Eleve> {

    ArrayList<Eleve> eleves;
    Context context ;
    LayoutInflater inflater;
    public AdapterEleveAbsent(@NonNull Context context, int resource, @NonNull ArrayList<Eleve> objects) {
        super(context, resource, objects);
        this.context=context;
        this.eleves=objects;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_eleve_absence, null);
            holder = new ViewHolder();
            holder.nom = (TextView) convertView.findViewById(R.id.nom);
            holder.dateNaissance = (TextView) convertView.findViewById(R.id.date);
            holder.numero = (TextView) convertView.findViewById(R.id.numero);
            holder.cbShowName = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Eleve person = eleves.get(position);
        holder.numero.setText(String.valueOf(position+1));
        holder.nom.setText(person.getNom());
        holder.dateNaissance.setText(person.getDateNaissance());
        holder.cbShowName.setChecked(person.isAbsent());

        holder.cbShowName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.cbShowName.isChecked()) {
                    eleves.get(position).setAbsent(true);
                    holder.cbShowName.setText("Absent");
                } else {
                    eleves.get(position).setAbsent(false);
                    holder.cbShowName.setText("Present");
                }
            }
        });
        return convertView;
    }

    @Override
    public int getCount() {
        return eleves.size();
    }

    static class ViewHolder {
        TextView nom ;
        TextView dateNaissance ;
        TextView numero ;
        CheckBox cbShowName;
    }


}
