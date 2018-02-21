package ai.api.sample;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by krishna on 3/7/17.
 */

    class CallJson {

    private String Light, Switch;
    //private String URL = "https://simple-reactapp.herokuapp.com/api/stuff.json";
    private String URL = "http://192.168.43.239:3001/api/stuff.json";

    CallJson(String light, String Switch) {
        Light = light;
        this.Switch = Switch;
    }

    CallJson(String Url){
        URL = Url;
    }

    public void OnClickPOST() {
        new myAsyncTask("POST").execute(URL, Light, Switch);
    }

    private class myAsyncTask extends AsyncTask<String,Integer,String> {

        String ReqTYPE ;

        public myAsyncTask(String TYPE){
            ReqTYPE = TYPE;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser myRquest = new JSONParser();

            JSONObject myObj = new JSONObject();
            HashMap myParam = new HashMap();
            myParam.put("Light",params[1]);
            myParam.put("Switch",params[2]);

            myObj = myRquest.makeHttpRequest(params[0],"POST",myParam);

            publishProgress(10);

            return myObj.toString();
        }

        @Override
        protected void onPostExecute(String resp) {
            super.onPostExecute(resp);

            Log.d("HTTP Class", "Resp Log: "+resp);

           }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }
}