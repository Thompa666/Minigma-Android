package com.enayet.minigma;

import android.widget.EditText;

import java.util.Random;

/**
 * Created by earmu_000 on 6/30/2014.
 */
public class encoder {
    String return_message(EditText message, EditText password) {
        ran generator = new ran();
        Random salty = new Random();

        int seed = generator.gen_seed(password); //gets a numerical value to set seed for randomness (passwd can be alphanum)
        generator.shuffleArray(seed);
        String message_s = message.getText().toString();
        int indy;
        char c;
        StringBuilder transformed_message = new StringBuilder(160); //initialize string
        boolean message_even = message_s.length() % 2 == 0;
        int s_length = message_s.length();
        if (message_even) {
            for (int i = 0; i < s_length; i++) {
                c = message_s.charAt(i);
                indy = (new String(ran.standard_cipher).indexOf(c));
                transformed_message.append(Character.toString(generator.cipher[salty.nextInt(generator.cipher.length)])); //random salt
                transformed_message.append(Character.toString(generator.cipher[indy])); //ciphered character
                transformed_message.append(Character.toString(generator.cipher[salty.nextInt(generator.cipher.length)])); //random salt

            }
        } else {
            for (int i = 0; i < s_length; i++) {
                c = message_s.charAt(i);
                indy = (new String(ran.standard_cipher).indexOf(c));
                transformed_message.append(Character.toString(generator.cipher[indy])); //actual
                transformed_message.append(Character.toString(generator.cipher[salty.nextInt(generator.cipher.length)])); //randy
                transformed_message.append(Character.toString(generator.cipher[salty.nextInt(generator.cipher.length)])); //randy
            }
        }
        return transformed_message.toString();
    }
}
