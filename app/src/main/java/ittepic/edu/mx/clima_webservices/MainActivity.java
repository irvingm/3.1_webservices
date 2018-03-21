package ittepic.edu.mx.clima_webservices;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.StatusLine;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;



public class MainActivity extends AppCompatActivity {
    String cuidad="";
    String pais="";
    String tiempo="";
    TextView resp;
    Button btng;
    ListView lista;
    private String[] cord={};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resp = (TextView) findViewById(R.id.txtTemp);
        //4btng = (Button)  findViewById(R.id.btnre);
        lista= (ListView) findViewById(R.id.listview);
        soli();
        holaa();


    }
    public void holaa(){

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cord);
        lista.setAdapter(adaptador);

    }
    public void soli(){

        String url="http://api.openweathermap.org/data/2.5/weather?q=Tepic,mx&APPID=f984a946569c92d056a2c65fe7a42e3f";  //TEPIC
        //http://api.openweathermap.org/data/2.5/weather?q=Guadalajara,mx&appid=0906362826d2cfea265ed029381a7e31  GDL
        //String url="http://api.openweathermap.org/data/2.5/weather?q=London,uk&appid=0906362826d2cfea265ed029381a7e31";
        new ReadJSONFeed().execute(url);
    }

    private class ReadJSONFeed extends AsyncTask<String, String, String>  {

        protected void onPreExecute(){}
        @Override

        protected String doInBackground(String... urls){
            HttpClient httpclient=new DefaultHttpClient();
            StringBuilder builder=new StringBuilder();
            HttpPost  httppost= new HttpPost(urls[0]);

            try{

                HttpResponse response= httpclient.execute(httppost);
                StatusLine statusLine= response.getStatusLine();
                int statusCode = statusLine.getStatusCode();

                if (statusCode==200){
                    HttpEntity entity= response.getEntity();
                    InputStream content= entity.getContent();
                    BufferedReader reader= new BufferedReader(new InputStreamReader(content));
                    String line;
                    while((line = reader.readLine()) != null){
                        builder.append(line);

                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            return builder.toString();

        }

        protected void onPostExecute(String resultado){

            tiempo= "El reporte del tiempo de Tepic es: \n";
            cord = new String[6];

            try{
                JSONObject jsonObject = new JSONObject(resultado);

                // tiempo+="Codigo: "+ jsonObject.getString("id")+"\n";
                cord[0]="Codigo: "+ jsonObject.getString("id");

                JSONObject jcoordObject = new JSONObject(jsonObject.getString("coord"));

                //tiempo+="Longitud: "+ jcoordObject.getString("lon")+"\n";
                //tiempo+="Latitud: "+ jcoordObject.getString("lat")+"\n";
                cord[1]="Longitud: "+ jcoordObject.getString("lon");
                cord[2]="Latitud: "+ jcoordObject.getString("lat");

                JSONArray jsweatherObject = new JSONArray(jsonObject.getString("weather"));
                JSONObject jweatherObject= jsweatherObject.getJSONObject(0);
                //tiempo+="Nubes: "+ jweatherObject.getString("description")+"\n";
                //cord[3]="Nubes: "+ jweatherObject.getString("description");

                JSONObject jsmainObject= new JSONObject(jsonObject.getString("main"));
                //tiempo+="Humedad: "+ jsmainObject.getString("humidity")+"%\n";
                //tiempo+="Presion atmosferica: "+ jsmainObject.getString("pressure")+"hpa \n";
                cord[3]="Humedad: "+ jsmainObject.getString("humidity")+"%";
                cord[4]="Temperatura: "+(Float.valueOf(jsmainObject.getString("temp"))-273)+"°";
                cord[5]="Presion atmosferica: "+ jsmainObject.getString("pressure")+" hpa";
                cord[6]="velocidad del viento : "+ jsmainObject.getString("speed")+"mps";


                JSONObject jswindObject = new JSONObject(jsonObject.getString("wind"));
                //tiempo+="velocidad del viento : "+ jswindObject.getString("speed")+"mps \n";
                //cord[6]="velocidad del viento : "+ jswindObject.getString("speed")+"mps";
                JSONObject jstempObject= new JSONObject(jsonObject.getString("main"));
                tiempo+="Humedad: "+ jstempObject.getString("humidity")+"%\n";
                tiempo+="Grados: "+ jstempObject.getString("temp")+" \n";


            }catch (Exception e){
                e.printStackTrace();
            }
            holaa();
            if(tiempo.trim().length()>0){
                resp.setText(tiempo);
            }else{
                resp.setText("no se encontro");
            }

        }


    }

}

