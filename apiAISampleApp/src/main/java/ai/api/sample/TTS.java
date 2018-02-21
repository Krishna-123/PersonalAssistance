package ai.api.sample;

import android.content.Context;
import android.speech.tts.TextToSpeech;

public class TTS {

    private static TextToSpeech textToSpeech;

    public static void init(final Context context) {
        if (textToSpeech == null) {
            textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int i) {

                }
            });
        }
    }

    public static void speak(final String text) {


        textToSpeech.setSpeechRate((float) 0.80);
        textToSpeech.setPitch((float) 1);

        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
}
