package co.com.fredymosqueralemus.pelucitas.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import co.com.fredymosqueralemus.pelucitas.constantes.Constantes;

/**
 * Esta clase representa las preferencias compartidas
 * Created by Fredy Mosquera Lemus on 12/02/17.
 */

public class SharedPreferencesSeguro {
    public static class SharedPreferenceException extends RuntimeException{
        public SharedPreferenceException(Throwable e){
            super(e);
        }
    }

    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String KEY_TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final String SECRET_KEY_HASH_TRANSFORMATION = "SHA-256";
    private static final String CHARSET = "UTF-8";

    private boolean encryptKey;
    private Cipher reader;
    private Cipher writer;
    private Cipher keyWriter;

    private final SharedPreferences sharedPreferences;

    public SharedPreferencesSeguro(Context context, String preferenceName, String secureKey){
        try {
            this.reader = Cipher.getInstance(TRANSFORMATION);
            this.writer = Cipher.getInstance(TRANSFORMATION);
            this.keyWriter = Cipher.getInstance(KEY_TRANSFORMATION);
            this.sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
            this.encryptKey = true;
            inicializarCipher(secureKey);
        }catch (GeneralSecurityException e){
            throw new SharedPreferenceException(e);
        }
    }
    private void inicializarCipher(String secureKey) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        IvParameterSpec parameterSpec = getIvParameterSpec();
        SecretKeySpec secretKeySpec = getSecretKeySpec(secureKey);
        writer.init(Cipher.ENCRYPT_MODE, secretKeySpec, parameterSpec);
        reader.init(Cipher.DECRYPT_MODE, secretKeySpec, parameterSpec);
        keyWriter.init(Cipher.ENCRYPT_MODE, secretKeySpec);
    }
    private IvParameterSpec getIvParameterSpec(){
        byte[] iv = new byte[writer.getBlockSize()];
        System.arraycopy(Constantes.KEY_SHAREDPREFERENCE.getBytes(), 0, iv, 0, writer.getBlockSize());
        return  new IvParameterSpec(iv);
    }
    private SecretKeySpec getSecretKeySpec(String key) throws NoSuchAlgorithmException {
        byte[] keyBytes = createKeyBytes(key);
        return new SecretKeySpec(keyBytes, TRANSFORMATION);
    }

    private byte[] createKeyBytes(String key) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(SECRET_KEY_HASH_TRANSFORMATION);
        messageDigest.reset();
        return messageDigest.digest(key.getBytes());
    }
    private String toKey(String key){
        if(!encryptKey){
           return key;
        }
        return encrypt(key, keyWriter);
    }

    private String encrypt(String value, Cipher writer){
        byte[] secureValue;
        try{
            secureValue = convert(writer, value.getBytes(CHARSET));
        }catch (UnsupportedEncodingException e){
            throw  new SharedPreferenceException(e);
        }

        return Base64.encodeToString(secureValue, Base64.NO_WRAP);
    }

    private String decrypt(String secureEncodedValue){
        byte[] secureValue = Base64.decode(secureEncodedValue, Base64.NO_WRAP);
        byte[] value = convert(reader, secureValue);
        try {
            return new String(value, CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new SharedPreferenceException(e);
        }
    }
    private  static byte[] convert(Cipher cipher, byte[] bs) throws SharedPreferenceException{
        try {
            return cipher.doFinal(bs);
        } catch (Exception e) {
            throw new SharedPreferenceException(e);
        }
    }

    public void put(String key, String value){
        if(null == value){
            sharedPreferences.edit().remove(toKey(key)).commit();
        }else{
            putValue(toKey(key), value);
        }
    }
    public boolean containsKey(String key){

        return sharedPreferences.contains(toKey(key));
    }
    public void removeValue(String key){

        sharedPreferences.edit().remove(toKey(key)).commit();
    }

    public void clear(){

        sharedPreferences.edit().clear().commit();
    }

    private void putValue(String key, String value){
        String securedValueEncoded = encrypt(value, writer);
        sharedPreferences.edit().putString(key, securedValueEncoded).commit();
    }
    public String getString(String key){
        if(sharedPreferences.contains(toKey(key))){
            String secureEncodedValue = sharedPreferences.getString(toKey(key), "");
            return decrypt(secureEncodedValue);
        }
        return null;
    }
}
