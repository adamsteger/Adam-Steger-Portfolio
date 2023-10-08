package com.capstone.lifesabit.gateguard.passes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.glxn.qrgen.javase.QRCode;

/**
 * This class generates QR codes and returns them
 * for GET requests, such that they can be embedded
 * in an email
 */
@RestController
public class QrCodeGen {
    @RequestMapping(value = "/qrcode/{passId}", method = RequestMethod.GET, produces = "image/png")
    public void genBarcode(HttpServletResponse response, @PathVariable("passId") String passID) throws IOException {
      String transformed = "https://www.gate-guard.com/use-pass?passID=" + passID;
      ByteArrayInputStream image = generateQRCodeImage(transformed);
      response.setHeader("Content-Type", MediaType.IMAGE_PNG_VALUE);
      IOUtils.copy(image, response.getOutputStream());
    }

  public static ByteArrayInputStream generateQRCodeImage(String barcodeText) {
    ByteArrayOutputStream stream = QRCode
      .from(barcodeText)
      .withSize(250, 250)
      .stream();
    ByteArrayInputStream bis = new ByteArrayInputStream(stream.toByteArray());
    return bis;
  }
}
