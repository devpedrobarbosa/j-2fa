package me.pedrao.j2fa.authenticator;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import de.taimos.totp.TOTP;
import lombok.RequiredArgsConstructor;
import me.pedrao.j2fa.J2FA;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

@RequiredArgsConstructor
public class Authenticator {

    private final J2FA instance;
    private final SecureRandom random = new SecureRandom();

    public String generateSecret() {
        final byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        return new Base32().encodeToString(bytes);
    }

    private String getToken(String secret) {
        final byte[] bytes = new Base32().decode(secret);
        return TOTP.getOTP(Hex.encodeHexString(bytes));
    }

    public boolean authenticate(String token, String secret) {
        return token.equalsIgnoreCase(getToken(secret));
    }

    private String urlEncode(String string) {
        return URLEncoder.encode(string, StandardCharsets.UTF_8).replace("+", "%20");
    }

    public String getQRCodeURL(String secret) {
        return "otpauth://totp/"
                + urlEncode(instance.getApplicationName())
                + "?secret=" + urlEncode(secret);
    }

    public void createQRCodeFile(String secretKey, String filePath) {
        filePath = filePath.replace(".png", "") + ".png";
        try {
            final BitMatrix matrix = new MultiFormatWriter().encode(getQRCodeURL(secretKey), BarcodeFormat.QR_CODE, 400, 400);
            try(FileOutputStream out = new FileOutputStream(filePath)) {
                MatrixToImageWriter.writeToStream(matrix, "png", out);
            }
            instance.getLogger().info("A QRCode has been generated for you at \"" + filePath + "\"");
        } catch(WriterException | IOException exception) {
            exception.printStackTrace();
            instance.getLogger().severe("Could not generate QRCode at \"" + filePath + "\"");

        }
    }
}