package it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.util;

import com.mastercard.developer.utils.*;

import java.security.*;
import java.security.spec.*;

/*Methods adapted from com.mastercard.developer.utils.EncryptionUtils*/
public class EncryptionUtils {

    private static final String PKCS_1_PEM_HEADER = "-----BEGIN RSA PRIVATE KEY-----";
    private static final String PKCS_1_PEM_FOOTER = "-----END RSA PRIVATE KEY-----";

    public static PrivateKey loadDecryptionKey(String fileAsString) throws GeneralSecurityException {
        return readPkcs1PrivateKey(EncodingUtils.base64Decode(fileAsString.replace(PKCS_1_PEM_HEADER, "").replace(PKCS_1_PEM_FOOTER, "")));
    }

    private static PrivateKey readPkcs8PrivateKey(byte[] pkcs8Bytes) throws GeneralSecurityException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8Bytes);
        try {
            return keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException var4) {
            throw new IllegalArgumentException("Unexpected key format!", var4);
        }
    }

    private static PrivateKey readPkcs1PrivateKey(byte[] pkcs1Bytes) throws GeneralSecurityException {
        int pkcs1Length = pkcs1Bytes.length;
        int totalLength = pkcs1Length + 22;
        byte[] pkcs8Header = new byte[]{48, -126, (byte) (totalLength >> 8 & 255), (byte) (totalLength & 255), 2, 1, 0, 48, 13, 6, 9, 42, -122, 72, -122, -9, 13, 1, 1, 1, 5, 0, 4, -126, (byte) (pkcs1Length >> 8 & 255), (byte) (pkcs1Length & 255)};
        byte[] pkcs8bytes = join(pkcs8Header, pkcs1Bytes);
        return readPkcs8PrivateKey(pkcs8bytes);
    }

    private static byte[] join(byte[] byteArray1, byte[] byteArray2) {
        byte[] bytes = new byte[byteArray1.length + byteArray2.length];
        System.arraycopy(byteArray1, 0, bytes, 0, byteArray1.length);
        System.arraycopy(byteArray2, 0, bytes, byteArray1.length, byteArray2.length);
        return bytes;
    }

}
