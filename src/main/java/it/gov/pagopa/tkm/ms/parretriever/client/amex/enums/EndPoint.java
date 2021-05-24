package it.gov.pagopa.tkm.ms.parretriever.client.amex.enums;

public enum EndPoint {
    SANDBOX("https", "api.qasb.americanexpress.com", 443),
    PRODUCTION("https", "api.americanexpress.com", 443);

    private final String scheme;
    private final String hostname;
    private final int port;

    EndPoint(String scheme, String hostname, int port) {
        this.scheme = scheme;
        this.port = port;
        this.hostname = hostname;
    }

    public String getScheme() {
        return scheme;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }
}
