package it.gov.pagopa.tkm.ms.parretriever.constant;

import it.gov.pagopa.tkm.ms.parretriever.client.external.amex.model.response.*;

public class DefaultBeans {

    public final String PAN = "111111111111";
    public final String PAR = "abc11111111111";

    public final AmexParResponse[] AMEX_PAR_RESPONSE = new AmexParResponse[]{new AmexParResponse(PAN, PAR, true)};

}
