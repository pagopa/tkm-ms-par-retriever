package it.gov.pagopa.tkm.ms.parretriever.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.bouncycastle.openpgp.PGPException;

public interface ParRetrieverService {

    void getPar() throws PGPException, JsonProcessingException;

}
