package com.example.meyss.monecole.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;



import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;


public class inscrit extends AppCompatActivity {

    String url = "http://"+ UserLoged.url+"/UpdatePersonne";
    EditText nom,prenom,password;
    String role="Parent";
    AesCrypt cryptage;
    ImageButton image;
    String imageName = "null",encodeImage;
    private final  int IMG_RESULT= 1;
    private Bitmap bitmap;
    RequestParams params = new RequestParams();
    ProgressDialog prgDialog;
    String mCurrentPhotoPath;
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscrit);
        CardView sign = (CardView) findViewById(R.id.inscrit);
        nom = (EditText) findViewById(R.id.username);
        prenom = (EditText) findViewById(R.id.prenom);
        password = (EditText) findViewById(R.id.password);
        image = (ImageButton) findViewById(R.id.image);
        prgDialog = new ProgressDialog(this);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        final SharedPreferences sharePref=getSharedPreferences("shPrefWorkshop",this.MODE_PRIVATE);

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertReq();
                Intent intent = new Intent(inscrit.this,login.class);
                startActivity(intent);
            }
        });
    }

    public void selectImage(){
        final CharSequence[] items={"Prendre Photo","Choisir depuis Gallery","Annuler"};

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Prendre Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //boolean result = checkPermission(inscrit.this);
               /* if (items[item].equals("Prendre Photo")) {
                    takePhoto();


                    }else */if(items [item].equals("Choisir depuis Gallery")) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent,IMG_RESULT);
                    }else{
                        dialog.cancel();
                    }
                }
            });
            //builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            builder.show();
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void insertReq(){

        RequestQueue queue=Volley.newRequestQueue(this);
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id", String.valueOf(UserLoged.userConnected.getId()));
                params.put("nom", nom.getText().toString());
                params.put("prenom",prenom.getText().toString());
                params.put("mail", UserLoged.userConnected.getMail().toString());
                params.put("password",cryptage.encrypt(password.getText().toString()));
                params.put("image",imageName);
               // params.put("image",image.getText().toString);
                System.out.println(params);
                Log.d("Json","Json"+params);
            JSONObject json = new JSONObject(params);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.PUT, url,json, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response+"++++++++++++++++++++");
                Toast.makeText(getApplicationContext(),"Inscription Réussit",Toast.LENGTH_LONG).show();
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(),error+"",Toast.LENGTH_LONG).show();
                    }
                }
        );
        

        queue.add(postRequest);
    }

    Uri imageUri;
    public void takePhoto(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        startActivityForResult(cameraIntent,IMG_RESULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMG_RESULT && resultCode == RESULT_OK && data != null){
            Uri path = data.getData();

            try {
                Random generator = new Random();
                int n = 10000;
                n = generator.nextInt(n);
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                imageName = "ProfilIMG-"+n+".jpg";
                new AsyncTask<Void,Void,String>(){

                    @Override
                    protected String doInBackground(Void... voids) {
                        ByteArrayOutputStream boss = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG,80,boss);
                        byte[] b = boss.toByteArray();
                        encodeImage = Base64.encodeToString(b,Base64.DEFAULT);
                        Log.e("encodeImage:  ", encodeImage);
                        // params.put("image",encodeImage);
                        return encodeImage;

                    }

                    @Override
                    protected void onPostExecute(String s) {
                        Log.e("Erreur: ",s);
                        params.add("image",s);
                        triggerImageUpload();


                    }
                }.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(resultCode == RESULT_OK){
            switch (requestCode){
                case IMG_RESULT:
                    Bitmap capturePhoto = null;
                    try {
                        Bitmap resized= MediaStore.Images.Media.getBitmap(
                                getApplicationContext().getContentResolver(), imageUri);
                        Matrix matrix = new Matrix();
                        matrix.postRotate(90);
                        Bitmap bitmap = Bitmap.createScaledBitmap(resized, resized.getWidth()-(resized.getWidth()/3), resized.getHeight(), true);
                        capturePhoto = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                                matrix, true);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //createScaledBitmap(resized, resized.getWidth()/2, resized.getHeight()/2, true);

                    //BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                    image.setImageBitmap(capturePhoto);

                    image.setVisibility(View.VISIBLE);
                    saveImage(capturePhoto);


                    break;
                default:
                    break;
            }
        }
    }


    public void triggerImageUpload() {
        makeHTTPCall();
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            prgDialog.incrementProgressBy(1);
        }
    };


    public void makeHTTPCall() {

        prgDialog.setMessage("Invoking JSP");

        AsyncHttpClient client = new AsyncHttpClient();

        params.add("filename",imageName);
        System.out.println(params+"    paramssss99999999999999562316161616161");
        // Don't forget to change the IP address to your LAN address. Port no as well.
        client.post("https://"+UserLoged.url+"/MonEcole-web/uploadimg.jsp?",params
                , new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        prgDialog.setMax(100);
                        prgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        prgDialog.show();





                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (prgDialog.getProgress()<= prgDialog.getMax())
                                {
                                    try {
                                        Thread.sleep(200);
                                        handler.sendMessage(handler.obtainMessage());
                                        if (prgDialog.getProgress() == prgDialog.getMax()){

                                            prgDialog.dismiss();


                                        }
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }).start();






                        Toast.makeText(getApplicationContext(), "ok",
                                Toast.LENGTH_LONG).show();


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if (statusCode == 404) {
                            Toast.makeText(getApplicationContext(),
                                    "Requested resource not found",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(getApplicationContext(),
                                    "Something went wrong at server end",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
                            prgDialog.setMax(100);
                            prgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            prgDialog.show();





                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    while (prgDialog.getProgress()<= prgDialog.getMax())
                                    {
                                        try {
                                            Thread.sleep(200);
                                            handler.sendMessage(handler.obtainMessage());
                                            if (prgDialog.getProgress() == prgDialog.getMax()){

                                                prgDialog.dismiss();


                                            }
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }
                            }).start();
                            Toast.makeText(
                                   getApplicationContext(),
                                    "Error Occured \n Most Common Error: \n1. Device not connected to Internet\n2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : "
                                            + statusCode, Toast.LENGTH_LONG)
                                    .show();

                        }
                    }


                    // When the response returned by REST has Http
                    // response code '200'



                    // When the response returned by REST has Http
                    // response code other than '200' such as '404',
                    // '500' or '403' etc

                });
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }




    private void saveImage(final Bitmap finalBitmap){
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root+"/ProfilIMG");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        imageName = "ProfilIMG-"+n+".jpg";

        File file = new File(myDir,imageName);
        if(file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG,100,out);

            String resizeImagePath = file.getAbsolutePath();
            out.flush();
            out.close();
            Toast.makeText(getApplicationContext(),"Your Photo save",Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Exception Throw",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new AsyncTask<Void,Void,String>(){

            @Override
            protected String doInBackground(Void... voids) {
                ByteArrayOutputStream boss = new ByteArrayOutputStream();
                finalBitmap.compress(Bitmap.CompressFormat.JPEG,100,boss);
                byte[] b = boss.toByteArray();
                encodeImage = Base64.encodeToString(b,Base64.DEFAULT);
                Log.e("encodeImage:  ", encodeImage);
                // params.put("image",encodeImage);
                return encodeImage;

            }

            @Override
            protected void onPostExecute(String s) {
                Log.e("Erreur: ",s);
                params.add("image",s);
                triggerImageUpload();


            }
        }.execute();
    }

}
