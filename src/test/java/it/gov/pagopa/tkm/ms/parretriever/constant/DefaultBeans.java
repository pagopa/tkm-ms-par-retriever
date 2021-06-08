package it.gov.pagopa.tkm.ms.parretriever.constant;

import it.gov.pagopa.tkm.ms.parretriever.client.external.amex.model.response.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.visa.model.response.*;

import java.time.*;

public class DefaultBeans {

    public final String PAN = "111111111111";
    public final String PAR = "abc11111111111";

    public static final Instant INSTANT = Instant.EPOCH;

    public final AmexParResponse[] AMEX_PAR_RESPONSE = new AmexParResponse[]{new AmexParResponse(PAN, PAR, true)};

    public final VisaParEncryptedResponse VISA_PAR_ENC_RESPONSE = new VisaParEncryptedResponse("eyJlbmMiOiJBMTI4R0NNIiwiaWF0IjowLCJhbGciOiJSU0EtT0FFUC0yNTYiLCJraWQiOiJURVNUX0tFWV9JRCJ9.UdBI3Dy8AFg3PDPRjI453g7CK5Nq5YWnsaSOj199O9YhWRDd9Rf55b4ytF9pJ9jz_4MdhzzHSvVCbsTkQ5QAYir6tr9IbTtkn8eozGecUfKRowpztsFoWdN8mcr5bpot1bK5OVI2xQFmNixrj_ZPxkDk9FS1cxHIcMHiPm3K_YiaS_K8tMLng1oTH-XQkFE0AEM6P2HFyzun0SwkQP4nkYMI3wYgDPTEAos4orG2vgmFBR3CEXkQkkgodQu0YuOqwD8lhmp8fFTdHe-3nQ9u8k12z5whcOmcyFo7g8fDiGggTXZFbc_a5Unt9yvrzGiS4IHQk01RkK1eykh-rRCAuQ.eLGi-GPm_OJBv3jR.gjixjPlSXJhgjHm8OwxPPGfx343sy18NesE6ECtMKgtKUD7hEckmwHdYhPj_5DJ8srk1r3rgs35CWK_vX95fi2UrejQRG3vJZoKfqaeUCLfZwnI8xOQIGIO-J93WlXEkEcPl9jauF35aPWZx1ucGXxrheGnTnlJGWuHCYiUYQa8KD9x6TOpUzqk.TRwZRUVpEQW78ux2BnnA9g");
    public final VisaParDecryptedResponse VISA_PAR_DEC_RESPONSE = new VisaParDecryptedResponse(PAR, INSTANT.toString(), PAN);

}
