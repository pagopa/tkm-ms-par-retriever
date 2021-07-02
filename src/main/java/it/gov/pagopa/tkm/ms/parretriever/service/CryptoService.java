package it.gov.pagopa.tkm.ms.parretriever.service;

import org.bouncycastle.crypto.CryptoException;

public interface CryptoService {

    String decrypt(String toDecrypt);

}
