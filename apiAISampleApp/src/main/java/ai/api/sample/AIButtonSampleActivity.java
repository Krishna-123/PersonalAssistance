package ai.api.sample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Map;

import ai.api.android.AIConfiguration;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Metadata;
import ai.api.model.Result;
import ai.api.model.Status;
import ai.api.ui.AIButton;

public class AIButtonSampleActivity extends BaseActivity implements AIButton.AIButtonListener {

    public static final String TAG = "ButtonSampleActivity";

    private AIButton aiButton;
    private LinearLayout unLockSwitch, LockSwitch, unLockLight, LockLight;
    Button urlButton;
    EditText url_et;
    String Action,Url ;
   // private Gson gson = GsonFactory.getGson();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        TTS.speak(" HEY YOUR SECURITY HANDLER IS UP AND RUNNING  ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aibutton_sample);

        unLockSwitch = (LinearLayout) findViewById(R.id.unlockSwitch);
        LockSwitch = (LinearLayout) findViewById(R.id.lockSwitch);
        unLockLight = (LinearLayout) findViewById(R.id.unlockLight);
        LockLight = (LinearLayout) findViewById(R.id.lockLight);
        url_et = (EditText) findViewById(R.id.url_et);
        urlButton = (Button) findViewById(R.id.urlButton);

        aiButton = (AIButton) findViewById(R.id.micButton);

        final AIConfiguration config = new AIConfiguration(Config.ACCESS_TOKEN,
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        config.setRecognizerStartSound(getResources().openRawResourceFd(R.raw.test_start));
        config.setRecognizerStopSound(getResources().openRawResourceFd(R.raw.test_stop));
        config.setRecognizerCancelSound(getResources().openRawResourceFd(R.raw.test_cancel));

        aiButton.initialize(config);
        aiButton.setResultsListener(this);

    }

    public void setURL(View v){
        Url = url_et.getText().toString();
        new CallJson(Url);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // use this method to disconnect from speech recognition service
        // Not destroying the SpeechRecognition object in onPause method would block other apps from using SpeechRecognition service
        aiButton.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // use this method to reinit connection to recognition service
        aiButton.resume();
    }


    @Override
    public void onResult(final AIResponse response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onResult");

               // resultTextView.setText(gson.toJson(response));

                Log.i(TAG, "Received success response");

                // this is example how to get different parts of result object
                final Status status = response.getStatus();
                Log.i(TAG, "Status code: " + status.getCode());
                Log.i(TAG, "Status type: " + status.getErrorType());

                // do the job on getting the user says

                final Result result = response.getResult();
                Log.i(TAG, "Resolved query: " + result.getResolvedQuery());

                Action = result.getAction();

                // for on action


                if (Action.equals("onlight")) {
                    new CallJson("on", "not used").OnClickPOST();
                    unLockLight.setVisibility(View.VISIBLE);
                    LockLight.setVisibility(View.GONE);
                }
                // for off action
                else if (Action.equals("offlight")) {
                    new CallJson("off", "not used").OnClickPOST();
                    unLockLight.setVisibility(View.GONE);
                    LockLight.setVisibility(View.VISIBLE);
                }

                 if (Action.equals("open")) {
                    new CallJson("not used", "on").OnClickPOST();
                    unLockSwitch.setVisibility(View.VISIBLE);
                    LockSwitch.setVisibility(View.GONE);
                }
                // for off action
                else if (Action.equals("close")) {
                    new CallJson("not used", "off").OnClickPOST();
                    unLockSwitch.setVisibility(View.GONE);
                    LockSwitch.setVisibility(View.VISIBLE);
                }


                Log.i(TAG, "Action: " + result.getAction());
                final String speech = result.getFulfillment().getSpeech();
                Log.i(TAG, "Speech: " + speech);
                TTS.speak(speech);

                final Metadata metadata = result.getMetadata();
                if (metadata != null) {
                    Log.i(TAG, "Intent id: " + metadata.getIntentId());
                    Log.i(TAG, "Intent name: " + metadata.getIntentName());
                }

                final HashMap<String, JsonElement> params = result.getParameters();
                if (params != null && !params.isEmpty()) {
                    Log.i(TAG, "Parameters: ");
                    for (final Map.Entry<String, JsonElement> entry : params.entrySet()) {
                        Log.i(TAG, String.format("%s: %s", entry.getKey(), entry.getValue().toString()));
                    }
                }
            }

        });
    }

    @Override
    public void onError(final AIError error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onError");
              //  resultTextView.setText(error.toString());
            }
        });
    }

    @Override
    public void onCancelled() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onCancelled");
               // resultTextView.setText("");
            }
        });
    }
}
