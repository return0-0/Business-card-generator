import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.IOException;

public class QRCodeGenerator {
    public static void generateQRCode(String nameCardInfo, String filePath) {
        int width = 300;
        int height = 300;
        String fileType = "png";

        Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(nameCardInfo, BarcodeFormat.QR_CODE, width, height, hintMap);
            File outputFile = new File(filePath);
            MatrixToImageWriter.writeToPath(bitMatrix, fileType, outputFile.toPath());
            System.out.println("二维码生成成功");
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            System.out.println("二维码生成失败: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String nameCardInfo = "nihao 1231";
        String filePath = "qr_code.png";
        generateQRCode(nameCardInfo, filePath);
    }
}

