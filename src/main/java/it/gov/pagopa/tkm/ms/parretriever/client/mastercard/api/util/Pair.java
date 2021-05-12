package it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api.util;

public class Pair {

    private String name = "";

    private String value = "";

    public Pair (String name, String value) {
        setName(name);
        setValue(value);
    }

    private void setName(String name) {
        if (invalidString(name)) {
            return;
        }

        this.name = name;
    }

    private void setValue(String value) {
        if (invalidString(value)) {
            return;
        }

        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    private boolean invalidString(String arg) {
        if (arg == null) {
            return true;
        }
        return arg.trim().isEmpty();
    }
}
