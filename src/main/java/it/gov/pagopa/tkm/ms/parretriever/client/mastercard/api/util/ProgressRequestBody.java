/*
 * Payment Account Reference Inquiry API
 * Merchants, Acquirers, or Digital Activity Customers (DACs) can use this service to query the PAR Vault to obtain a Payment Account Reference(PAR), from a Primary Account Number (PAN). PAR provides an industry-aligned approach designed to help link PAN-based transactions to transactions using associated payment tokens, without using the PAN as the linkage mechanism.
 *
 * The version of the OpenAPI document: 1.3
 * Contact: apisupport@mastercard.com
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api.util;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import java.io.IOException;

import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

public class ProgressRequestBody extends RequestBody {

    private final RequestBody requestBody;

    private final ApiCallback callback;

    public ProgressRequestBody(RequestBody requestBody, ApiCallback callback) {
        this.requestBody = requestBody;
        this.callback = callback;
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        BufferedSink bufferedSink = Okio.buffer(sink(sink));
        requestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {

            long bytesWritten = 0L;
            long contentLength = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    contentLength = contentLength();
                }

                bytesWritten += byteCount;
                callback.onUploadProgress(bytesWritten, contentLength, bytesWritten == contentLength);
            }
        };
    }
}
