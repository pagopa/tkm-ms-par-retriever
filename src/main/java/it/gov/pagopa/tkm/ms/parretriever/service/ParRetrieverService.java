package it.gov.pagopa.tkm.ms.parretriever.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.bouncycastle.openpgp.PGPException;

public interface ParRetrieverService {

    String PROCESS_NAME = "PAR_RETRIEVER";

    void getBinRange() throws PGPException, JsonProcessingException;





}
