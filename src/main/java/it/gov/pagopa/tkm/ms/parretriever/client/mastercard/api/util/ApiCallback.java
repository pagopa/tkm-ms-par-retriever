package it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api.util;

public interface ApiCallback {

    void onUploadProgress(long bytesWritten, long contentLength, boolean done);

    void onDownloadProgress(long bytesRead, long contentLength, boolean done);

}
