package fr.angers.univ.qrludo.utils;

import android.util.Log;

import com.google.api.client.util.Base64;

import java.sql.Timestamp;

/**
 * Created by Florian Lherbeil
 * Classe servant à encoder une chaine de caractère en base64 pour donner un nom reconnaissable aux fichers sans
 * passer par l'id qui est propre à google drive
 * Cette chaine n'aura jamais besoin d'être décompressée
 */
public class CompressionString {
    public static String compress(String str) {

        //On réduit la taille de la chaine si possible car un nom de fichier ne doit pas être trop grand
        if(str.contains(".com"))
            str=str.substring(str.indexOf(".com"));
        else if(str.contains(".fr"))
            str=str.substring(str.indexOf(".fr"));
        str=str.replace(".com","");
        byte[] encodedBytes = Base64.encodeBase64(str.getBytes());

        if(str.length()>100)
            return new String(encodedBytes).substring(0,10).replace("=","").replaceAll("/","_");
        else
            return new String(encodedBytes).replace("=","").replaceAll("/","_");


    }
}
