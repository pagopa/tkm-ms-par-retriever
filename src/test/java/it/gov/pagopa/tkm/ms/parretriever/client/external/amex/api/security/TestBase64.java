package it.gov.pagopa.tkm.ms.parretriever.client.external.amex.api.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import zipkin2.Call;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestBase64 {

    @InjectMocks
    private Base64 base64;

    @Test
    void encode_byteToBytes() throws IOException {
        assertThrows(NullPointerException.class, () -> Base64.encodeBytesToBytes(null, 0, 0, 0));

        assertThrows(IllegalArgumentException.class,
                () -> Base64.encodeBytesToBytes("ciao".getBytes(), -1, 0, 0));

        assertThrows(IllegalArgumentException.class,
                () -> Base64.encodeBytesToBytes("ciao".getBytes(), 0, -1, 0));

        assertThrows(IllegalArgumentException.class,
                () -> Base64.encodeBytesToBytes("ciao".getBytes(), 4, 4, 0));

        assertThrows(NullPointerException.class,
                () -> Base64.encodeBytesToBytes("ciao".getBytes(), 0, 4, 2));

        assertNotNull(Base64.encodeBytesToBytes("ciao".getBytes(), 0, 4, 8));
    }

    @Test
    void flush_base64() {
        Base64.OutputStream outputStream = new Base64.OutputStream(new ByteArrayOutputStream(), 1);
        ReflectionTestUtils.setField(outputStream, "position", 1);
        assertDoesNotThrow(outputStream::flushBase64);

        Base64.OutputStream stream = new Base64.OutputStream(new ByteArrayOutputStream(), 0);
        ReflectionTestUtils.setField(stream, "position", 1);
        assertThrows(IOException.class, stream::flushBase64);
    }

    @Test
    void write_int() {
        Base64.OutputStream outputStream = new Base64.OutputStream(new ByteArrayOutputStream(), 1);
        ReflectionTestUtils.setField(outputStream, "suspendEncoding", true);
        assertDoesNotThrow(() -> outputStream.write(0));

        Base64.OutputStream stream = new Base64.OutputStream(new ByteArrayOutputStream(), 8);
        ReflectionTestUtils.setField(stream, "encode", true);
        ReflectionTestUtils.setField(stream, "lineLength", 76);
        ReflectionTestUtils.setField(stream, "bufferLength", 1);
        assertDoesNotThrow(() -> stream.write(0));

        Base64.OutputStream output = new Base64.OutputStream(new ByteArrayOutputStream(), 0);
        ReflectionTestUtils.setField(output, "bufferLength", 1);
        assertDoesNotThrow(() -> output.write(43));

        Base64.OutputStream outStr = new Base64.OutputStream(new ByteArrayOutputStream(), 0);
        assertThrows(IOException.class, () -> outStr.write(0));
    }

    @Test
    void write_byte() {
        Base64.OutputStream outputStream = new Base64.OutputStream(new ByteArrayOutputStream(), 1);
        ReflectionTestUtils.setField(outputStream, "suspendEncoding", true);
        assertDoesNotThrow(() -> outputStream.write("ciao".getBytes(), 0, 4));
    }

}
