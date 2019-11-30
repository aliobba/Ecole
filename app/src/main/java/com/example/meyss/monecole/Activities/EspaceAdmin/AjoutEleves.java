package com.example.meyss.monecole.Activities.EspaceAdmin;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.meyss.monecole.Entities.Eleve;
import com.example.meyss.monecole.Entities.Personne;
import com.example.meyss.monecole.Entities.UserLoged;
import com.example.meyss.monecole.R;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AjoutEleves extends Fragment {
    ArrayList<Eleve> eleveList = new ArrayList<>();

    String urlP = "https://" + UserLoged.url + "/MonEcole-web/rest/personne/inscription";


    static String url = "https://" + UserLoged.url + "/MonEcole-web/rest/eleve/addEleve/";
    //  static int READ_REQUEST_FILE=1;
    private String[] FilePathStrings;
    private String[] FileNameStrings;
    private File[] listFile;
    File file;
    boolean OPEN = false;
    static final Personne PERSONNE = new Personne();
    FloatingActionButton importFile;


    ArrayList<String> pathHistory;
    String lastDirectory;
    int count = 0;

    ArrayList<Eleve> uploadData, AllEleves;

    ListView lvInternalStorage, listeEleves;

    public AjoutEleves() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.activity_ajout_eleves, container, false);


        importFile = (FloatingActionButton) root.findViewById(R.id.btnImport);
        listeEleves = (ListView) root.findViewById(R.id.ImportedList);
        lvInternalStorage = (ListView) root.findViewById(R.id.storageList);
        uploadData = new ArrayList<>();

        checkFilePermissions();
        listeEleves(getArguments().getInt("id_classe", 0));


        importFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (OPEN == false) {
                    OPEN = true;
                    listeEleves.setVisibility(View.GONE);
                    lvInternalStorage.setVisibility(View.VISIBLE);
                    count = 0;
                    pathHistory = new ArrayList<>();
                    pathHistory.add(count, System.getenv("EXTERNAL_STORAGE"));
                    Log.d("AjoutEleves", "btnSDCard: " + pathHistory.get(count));
                    checkInternalStorage();


                } else {
                    OPEN = false;
                    listeEleves.setVisibility(View.VISIBLE);
                    lvInternalStorage.setVisibility(View.GONE);
                }


             
            }
        });


        lvInternalStorage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                lastDirectory = pathHistory.get(count);
                if (lastDirectory.equals(adapterView.getItemAtPosition(i))) {
                    Log.d("AjoutEleves", "lvInternalStorage: Selected a file for upload: " + lastDirectory);

                    //Execute method for reading the excel data.
                    readExcelData(lastDirectory);

                } else {
                    count++;
                    pathHistory.add(count, (String) adapterView.getItemAtPosition(i));
                    checkInternalStorage();
                    Log.d("AjoutEleves", "lvInternalStorage: " + pathHistory.get(count));
                }
            }


        });

        return root;
    }

  
    private void readExcelData(String filePath) {
        Log.d("AjoutEleves", "readExcelData: Reading Excel File.");

        //decarle input file
        File inputFile = new File(filePath);

        try {
            InputStream inputStream = new FileInputStream(inputFile);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getPhysicalNumberOfRows();
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            StringBuilder sb = new StringBuilder();

            //outter loop, loops through rows
            for (int r = 1; r < rowsCount; r++) {
                Row row = sheet.getRow(r);
                int cellsCount = row.getPhysicalNumberOfCells();
                //inner loop, loops through columns
                for (int c = 0; c < cellsCount; c++) {
                    //handles if there are to many columns on the excel sheet.
                    if (c > 4) {
                        Log.e("AjoutEleves", "readExcelData: ERROR. Excel File Format is incorrect! ");
                        Toast.makeText(this.getContext(), "ERROR: Excel File Format is incorrect!", Toast.LENGTH_SHORT).show();

                        break;
                    } else {
                        String value = getCellAsString(row, c, formulaEvaluator);
                        String cellInfo = "r:" + r + "; c:" + c + "; v:" + value;
                        Log.d("AjoutEleves", "readExcelData: Data from row: " + cellInfo);
                        sb.append(value + ", ");
                    }
                }
                sb.append(":");
            }
            Log.d("AjoutEleves", "readExcelData: STRINGBUILDER: " + sb.toString());

            parseStringBuilder(sb);

        } catch (FileNotFoundException e) {
            Log.e("AjoutEleves", "readExcelData: FileNotFoundException. " + e.getMessage());
        } catch (IOException e) {
            Log.e("AjoutEleves", "readExcelData: Error reading inputstream. " + e.getMessage());
        }
    }

    public void parseStringBuilder(StringBuilder mStringBuilder) {
        Log.d("AjoutEleves", "parseStringBuilder: Started parsing.");

        // splits the sb into rows.
        String[] rows = mStringBuilder.toString().split(":");

        //Add to the ArrayList<Eleve> row by row
        for (int i = 0; i < rows.length; i++) {
            //Split the columns of the rows
            String[] columns = rows[i].split(",");
            //use try catch to make sure there are no "" that try to parse into doubles.
            try {
                String nom = columns[0];
                String prenom = columns[1];
                String date_naissance = columns[2];
                String mailParent = columns[3];

                Eleve eleve = new Eleve(null, nom, prenom, date_naissance, 0, getArguments().getInt("id_classe", 0));
                Personne p = new Personne(mailParent.trim(), "Parent");
                findIDParent(p, eleve);


                String cellInfo = "(eleve): (" + nom + "," + prenom + "," + date_naissance + mailParent + ")";
                Log.d("AjoutEleves", "ParseStringBuilder: Data from row: " + cellInfo);

                //add the the uploadData ArrayList

            } catch (NumberFormatException e) {

                Log.e("AjoutEleves", "parseStringBuilder: NumberFormatException: " + e.getMessage());

            }
        }

        printDataToLog();
    }


    private void printDataToLog() {
        Log.d("AjoutEleves", "printDataToLog: Printing data to log...");

        for (int i = 0; i < uploadData.size(); i++) {
            String nomE = uploadData.get(i).getNom();
            String prenomE = uploadData.get(i).getPrenom();
            String naissance = uploadData.get(i).getDateNaissance();
            String photo = uploadData.get(i).getImg();
            System.out.println("printDataToLog: (Eleve): (" + nomE + "," + prenomE + "," + naissance + "," + photo + ")");
        }
    }


    private String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = "" + cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericValue = cellValue.getNumberValue();
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        double date = cellValue.getNumberValue();
                        SimpleDateFormat formatter =
                                new SimpleDateFormat("yyyy-MM-dd");
                        value = formatter.format(HSSFDateUtil.getJavaDate(date));
                    } else {
                        value = "" + numericValue;
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = "" + cellValue.getStringValue();
                    break;
                default:
            }
        } catch (NullPointerException e) {

            Log.e("AjoutEleves", "getCellAsString: NullPointerException: " + e.getMessage());
        }
        return value;
    }

    public void checkInternalStorage() {
        Log.d("AjoutEleves", "checkInternalStorage: Started.");

        try {
            if (!Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                Toast.makeText(this.getContext(), "No SD card found.", Toast.LENGTH_SHORT).show();
            } else {
                // Locate the image folder in your SD Car;d
                file = new File(pathHistory.get(count));
                Log.d("AjoutEleves", "checkInternalStorage: directory path: " + pathHistory.get(count));
            }

            listFile = file.listFiles();

            // Create a String array for FilePathStrings
            FilePathStrings = new String[listFile.length];

            // Create a String array for FileNameStrings
            FileNameStrings = new String[listFile.length];

            for (int i = 0; i < listFile.length; i++) {
                // Get the path of the image file
                FilePathStrings[i] = listFile[i].getAbsolutePath();
                // Get the name image file
                FileNameStrings[i] = listFile[i].getName();

            }

            for (int i = 0; i < listFile.length; i++) {
                Log.d("Files", "FileName:" + listFile[i].getName());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, FilePathStrings);
            lvInternalStorage.setAdapter(adapter);

        } catch (NullPointerException e) {
            Log.e("AjoutEleves", "checkInternalStorage: NULLPOINTEREXCEPTION " + e.getMessage());
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void checkFilePermissions() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = ActivityCompat.checkSelfPermission(getContext(), "Manifest.permission.READ_EXTERNAL_STORAGE");
            permissionCheck += ActivityCompat.checkSelfPermission(getContext(), "Manifest.permission.WRITE_EXTERNAL_STORAGE");
            if (permissionCheck != 0) {
                this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1001); //Any number
            }
        } else {
            Log.d("AjoutEleves", "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

    private void insertReqParent(Personne p) {

        RequestQueue queue = Volley.newRequestQueue(this.getContext());
        Map<String, String> params = new HashMap<String, String>();
        params.put("mail", p.getMail());
        params.put("role", p.getRole());
        System.out.println(params);
        Log.d("Json", "Json" + params);
        JSONObject json = new JSONObject(params);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, urlP, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response + "++++++++++++++++++++");
                Toast.makeText(getContext(), "Succès d'ajout parent", Toast.LENGTH_LONG).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error + "----------------");
                        Toast.makeText(getContext(), error + "", Toast.LENGTH_LONG).show();
                    }
                }
        );

        queue.add(postRequest);
    }

    private void insertReq(Eleve eleve) {
        System.out.println("d5alt houni insert Eleve");
        RequestQueue queue = Volley.newRequestQueue(this.getContext());
        Map<String, String> params = new HashMap<String, String>();
        params.put("nom", eleve.getNom());
        params.put("prenom", eleve.getPrenom());
        params.put("date_naissance", eleve.getDateNaissance());
        params.put("photo", eleve.getImg());


        System.out.println(params);
        Log.d("Json", "Json" + params);
        JSONObject json = new JSONObject(params);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url + eleve.getIdClasse() + "/" + eleve.getIdParent(), json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response + "++++++++++++++++++++");
                Toast.makeText(getContext(), "Succès d'ajout des élèves", Toast.LENGTH_LONG).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error + "----------------");
                        Toast.makeText(getContext(), "Succès d'ajout des élèves" + "", Toast.LENGTH_LONG).show();
                    }
                }
        );

        queue.add(postRequest);
    }

    private void findIDParent(final Personne pers, final Eleve el) {

        final String findParent = "http://" + UserLoged.url + "/findUserMail/" + pers.getMail() + "/";
        RequestQueue queue = Volley.newRequestQueue(getContext());


        StringRequest stringRequest = new StringRequest(Request.Method.GET, findParent, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    JSONArray array = new JSONArray(response);

                    JSONObject o = array.getJSONObject(0);


                    PERSONNE.setId(o.getInt("id"));
                    PERSONNE.setMail(o.getString("mail"));
                    PERSONNE.setRole(o.getString("role"));
                    System.out.println("fi west el findParent " + PERSONNE.toString());
                    el.setIdParent(PERSONNE.getId());
                    AllEleves(el);


                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("mahouch mawjoud");
                    insertReqParent(pers);
                    findIDParent(pers, el);


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), "verifier votre connexion", Toast.LENGTH_LONG).show();
                System.out.println("d5alt houni On Error Parent");


            }
        });

        queue.add(stringRequest);

    }


    private void listeEleves(int idC) {
        String url = "http://" + UserLoged.url + "/findEleveByClasse/" + idC + "/";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONArray jsA = new JSONArray(response);

                            int i = 0;
                            for (i = 0; i < jsA.length(); i++) {
                                Eleve eleve = new Eleve();
                                JSONObject o = jsA.getJSONObject(i);
                                eleve.setId(o.getInt("id"));
                                eleve.setNom(o.getString("nom"));
                                eleve.setPrenom(o.getString("prenom"));
                                eleve.setDateNaissance(o.getString("date_naissance"));
                                JSONObject jo = o.getJSONObject("personne");
                                JSONObject joc = o.getJSONObject("classe");
                                eleve.setIdParent(jo.getInt("id"));
                                eleve.setIdClasse(joc.getInt("id"));
                                eleveList.add(eleve);
                            }

                            EleveAdapter cAdapter = new EleveAdapter(getContext(), R.layout.eleve_item, eleveList);
                            listeEleves.setAdapter(cAdapter);

                            listeEleves.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Eleve el = (Eleve) parent.getItemAtPosition(position);
                                    UserLoged.selectedEleve= el;
                                    System.out.println(UserLoged.selectedEleve);
                                    Intent intent = new Intent(view.getContext(),ProfileEleve.class);
                                    System.out.println("clikiiiit");
                                    view.getContext().startActivity(intent);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("d5alt houni catch liste Eleves");

                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("volley error exception, it didn't work " + error.getMessage());
                System.out.println("d5alt houni onError listeEleves");

            }
        });

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);


    }

    void AllEleves(final Eleve eleve1) {
        AllEleves = new ArrayList<>();
        System.out.println("d5alt houni all Eleves");

        String url = "https://" + UserLoged.url + "/MonEcole-web/rest/eleve";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONArray jsA = new JSONArray(response);

                            int i = 0;
                            for (i = 0; i < jsA.length(); i++) {
                                System.out.println("d5alt houni  for all Eleves");
                                Eleve eleve = new Eleve();
                                JSONObject o = jsA.getJSONObject(i);
                                eleve.setId(o.getInt("id"));
                                eleve.setNom(o.getString("nom"));
                                eleve.setPrenom(o.getString("prenom"));
                                eleve.setDateNaissance(o.getString("date_naissance"));
                                JSONObject jo = o.getJSONObject("classe");
                                JSONObject jop = o.getJSONObject("personne");
                                eleve.setIdClasse(jo.getInt("id"));
                                eleve.setIdParent(jop.getInt("id"));
                                AllEleves.add(eleve);

                            }
                                eleve1.setImg(null);
                            if (AllEleves.contains(eleve1)) {
                                System.out.println("d5alt houni for list all Eleves");
                                System.out.println(AllEleves);
                                Toast.makeText(getContext(), "l'eleve:" + eleve1.getNom() + " " + eleve1.getPrenom() +
                                            " existe déjà!!!", Toast.LENGTH_LONG).show();

                                } else {
                                    System.out.println("d5alt houni  else all Eleves");

                                    insertReq(eleve1);
                                }




                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("d5alt houni  catch all Eleves");

                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("volley error exception, it didn't work " + error.getMessage());
                insertReq(eleve1);
                System.out.println("d5alt houni  on error all Eleves");

            }
        });

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);

    }
}





