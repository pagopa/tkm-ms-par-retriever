package it.gov.pagopa.tkm.ms.parretriever.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.bouncycastle.openpgp.PGPException;

public interface ParSenderService {
   void addRecordToKafkaQueue() throws PGPException, JsonProcessingException;
}
