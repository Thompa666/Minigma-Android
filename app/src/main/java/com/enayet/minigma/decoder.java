package com.enayet.minigma;

import android.widget.EditText;

/**
 * Created by earmu_000 on 7/2/2014.
 */
public class decoder {
    String decode_message(EditText message, EditText password) {
        ran generator = new ran();
        int seed = generator.gen_seed(password);
        generator.shuffleArray(seed);

        String message_hashed = message.getText().toString(); //turns message into a string
        String message_s = "";


        if (message_hashed.length() % 2 == 0) {
            for (int i = 1; i < message_hashed.length(); i += 3) {
                message_s += message_hashed.charAt(i);
            }
        } else {
            for (int i = 0; i < message_hashed.length(); i += 3) {
                message_s += message_hashed.charAt(i);
            }
        }

        int decoding_index;

        String transformed_s = "";
        int s_length = message_s.length();
        for (int i = 0; i < s_length; i++) {
            char c = message_s.charAt(i);
            decoding_index = (new String(generator.cipher).indexOf(c));
            transformed_s = transformed_s + Character.toString(ran.standard_cipher[decoding_index]);
        }
        return transformed_s;
    }
}
