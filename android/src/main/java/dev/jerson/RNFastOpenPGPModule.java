package dev.jerson;


import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import openpgp.Entity;
import openpgp.FileHints;
import openpgp.KeyOptions;
import openpgp.KeyPair;
import openpgp.FastOpenPGP;
import openpgp.Openpgp;
import openpgp.Options;

public class RNFastOpenPGPModule extends ReactContextBaseJavaModule {

    private final FastOpenPGP instance;

    public RNFastOpenPGPModule(ReactApplicationContext reactContext) {
        super(reactContext);

        instance = Openpgp.newFastOpenPGP();
    }

    @Override
    public String getName() {
        return "RNFastOpenPGP";
    }


    private byte[] readFile(String inputFile) throws IOException {
        File file = new File(inputFile);
        int size = (int) file.length();
        byte[] bytes = new byte[size];

        BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
        buf.read(bytes, 0, bytes.length);
        buf.close();

        return bytes;
    }

    private void writeFile(byte[] data, String inputFile) throws IOException {
        FileOutputStream out = new FileOutputStream(inputFile);
        out.write(data);
        out.close();
    }

    @ReactMethod
    public void decrypt(final String message, final String privateKey, final String passphrase, final ReadableMap mapOptions, final Promise promise) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    KeyOptions options = getKeyOptions(mapOptions);
                    String result = instance.decrypt(message, privateKey, passphrase, options);
                    promise.resolve(result);
                } catch (Exception e) {
                    promise.reject(e);
                }
            }
        }).start();
    }

    @ReactMethod
    public void decryptFile(final String inputFile, final String outputFile, final String privateKey, final String passphrase, final ReadableMap mapOptions, final Promise promise) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    KeyOptions options = getKeyOptions(mapOptions);
                    byte[] result = instance.decryptBytes(readFile(inputFile), privateKey, passphrase, options);
                    writeFile(result, outputFile);
                    promise.resolve(outputFile);
                } catch (Exception e) {
                    promise.reject(e);
                }
            }
        }).start();
    }

    @ReactMethod
    public void encrypt(final String message, final String publicKey, final ReadableMap mapEntity, final ReadableMap mapFileHints, final ReadableMap mapOptions, final Promise promise) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    KeyOptions options = getKeyOptions(mapOptions);
                    FileHints fileHints = getFileHints(mapFileHints);
                    Entity signedEntity = getEntity(mapEntity);
                    String result = instance.encrypt(message, publicKey, signedEntity, fileHints, options);
                    promise.resolve(result);
                } catch (Exception e) {
                    promise.reject(e);
                }
            }
        }).start();
    }

    @ReactMethod
    public void encryptFile(final String inputFile, final String outputFile, final String publicKey, final ReadableMap mapEntity, final ReadableMap mapFileHints, final ReadableMap mapOptions, final Promise promise) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    KeyOptions options = getKeyOptions(mapOptions);
                    FileHints fileHints = getFileHints(mapFileHints);
                    Entity signedEntity = getEntity(mapEntity);
                    byte[] result = instance.encryptBytes(readFile(inputFile), publicKey, signedEntity, fileHints, options);
                    writeFile(result, outputFile);
                    promise.resolve(outputFile);
                } catch (Exception e) {
                    promise.reject(e);
                }
            }
        }).start();
    }

    @ReactMethod
    public void sign(final String message, final String publicKey, final String privateKey, final String passphrase, final ReadableMap mapOptions, final Promise promise) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    KeyOptions options = getKeyOptions(mapOptions);
                    String result = instance.sign(message, publicKey, privateKey, passphrase, options);
                    promise.resolve(result);
                } catch (Exception e) {
                    promise.reject(e);
                }
            }
        }).start();
    }

    @ReactMethod
    public void signFile(final String inputFile, final String publicKey, final String privateKey, final String passphrase, final ReadableMap mapOptions, final Promise promise) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    KeyOptions options = getKeyOptions(mapOptions);
                    String result = instance.signBytesToString(readFile(inputFile), publicKey, privateKey, passphrase, options);
                    promise.resolve(result);
                } catch (Exception e) {
                    promise.reject(e);
                }
            }
        }).start();
    }

    @ReactMethod
    public void verify(final String signature, final String message, final String publicKey, final Promise promise) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Boolean result = instance.verify(signature, message, publicKey);
                    promise.resolve(result);
                } catch (Exception e) {
                    promise.reject(e);
                }
            }
        }).start();
    }

    @ReactMethod
    public void verifyFile(final String signature, final String inputFile, final String publicKey, final Promise promise) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Boolean result = instance.verifyBytes(signature, readFile(inputFile), publicKey);
                    promise.resolve(result);
                } catch (Exception e) {
                    promise.reject(e);
                }
            }
        }).start();
    }

    @ReactMethod
    public void decryptSymmetric(final String message, final String passphrase, final ReadableMap mapOptions, final Promise promise) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    KeyOptions options = getKeyOptions(mapOptions);
                    String result = instance.decryptSymmetric(message, passphrase, options);
                    promise.resolve(result);
                } catch (Exception e) {
                    promise.reject(e);
                }
            }
        }).start();

    }

    @ReactMethod
    public void decryptSymmetricFile(final String inputFile, final String outputFile, final String passphrase, final ReadableMap mapOptions, final Promise promise) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    KeyOptions options = getKeyOptions(mapOptions);
                    byte[] result = instance.decryptSymmetricBytes(readFile(inputFile), passphrase, options);
                    writeFile(result, outputFile);
                    promise.resolve(outputFile);
                } catch (Exception e) {
                    promise.reject(e);
                }
            }
        }).start();

    }

    @ReactMethod
    public void encryptSymmetric(final String message, final String passphrase, final ReadableMap mapFileHints, final ReadableMap mapOptions, final Promise promise) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    FileHints fileHints = getFileHints(mapFileHints);
                    KeyOptions options = getKeyOptions(mapOptions);
                    String result = instance.encryptSymmetric(message, passphrase, fileHints, options);
                    promise.resolve(result);
                } catch (Exception e) {
                    promise.reject(e);
                }
            }
        }).start();
    }

    @ReactMethod
    public void encryptSymmetricFile(final String inputFile, final String outputFile, final String passphrase, final ReadableMap mapFileHints, final ReadableMap mapOptions, final Promise promise) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    FileHints fileHints = getFileHints(mapFileHints);
                    KeyOptions options = getKeyOptions(mapOptions);
                    byte[] result = instance.encryptSymmetricBytes(readFile(inputFile), passphrase, fileHints, options);
                    writeFile(result, outputFile);
                    promise.resolve(outputFile);
                } catch (Exception e) {
                    promise.reject(e);
                }
            }
        }).start();
    }


    @ReactMethod
    public void generate(final ReadableMap mapOptions, final Promise promise) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Options options = getOptions(mapOptions);
                    KeyPair keyPair = instance.generate(options);
                    WritableMap result = Arguments.createMap();
                    result.putString("publicKey", keyPair.getPublicKey());
                    result.putString("privateKey", keyPair.getPrivateKey());
                    promise.resolve(result);
                } catch (Exception e) {
                    promise.reject(e);
                }
            }
        }).start();
    }

    private FileHints getFileHints(ReadableMap map) {
        FileHints options = new FileHints();

        if (map == null) {
            return options;
        }
        if (map.hasKey("fileName")) {
            options.setFileName(map.getString("fileName"));
        }
        if (map.hasKey("isBinary")) {
            options.setIsBinary(map.getBoolean("isBinary"));
        }
        if (map.hasKey("modTime")) {
            options.setModTime(map.getString("modTime"));
        }
        return options;
    }

    private Entity getEntity(ReadableMap map) {
        Entity options = new Entity();

        if (map == null) {
            return null;
        }
        if (map.hasKey("publicKey")) {
            options.setPublicKey(map.getString("publicKey"));
        }
        if (map.hasKey("privateKey")) {
            options.setPrivateKey(map.getString("privateKey"));
        }
        if (map.hasKey("passphrase")) {
            options.setPassphrase(map.getString("passphrase"));
        }
        return options;
    }

    private KeyOptions getKeyOptions(ReadableMap map) {
        KeyOptions options = new KeyOptions();

        if (map == null) {
            return options;
        }
        if (map.hasKey("cipher")) {
            options.setCipher(map.getString("cipher"));
        }
        if (map.hasKey("compression")) {
            options.setCompression(map.getString("compression"));
        }
        if (map.hasKey("hash")) {
            options.setHash(map.getString("hash"));
        }
        if (map.hasKey("RSABits")) {
            options.setRSABits(map.getInt("RSABits"));
        }
        // this is just in case
        if (map.hasKey("rsaBits")) {
            options.setRSABits(map.getInt("rsaBits"));
        }
        if (map.hasKey("compressionLevel")) {
            options.setCompressionLevel(map.getInt("compressionLevel"));
        }
        return options;
    }

    private Options getOptions(ReadableMap map) {
        Options options = new Options();

        if (map == null) {
            return options;
        }
        if (map.hasKey("comment")) {
            options.setComment(map.getString("comment"));
        }
        if (map.hasKey("email")) {
            options.setEmail(map.getString("email"));
        }
        if (map.hasKey("name")) {
            options.setName(map.getString("name"));
        }
        if (map.hasKey("passphrase")) {
            options.setPassphrase(map.getString("passphrase"));
        }
        if (map.hasKey("keyOptions")) {
            ReadableMap keyOptions = map.getMap("keyOptions");
            if (keyOptions != null) {
                options.setKeyOptions(this.getKeyOptions(keyOptions));
            }
        }

        return options;
    }
}
