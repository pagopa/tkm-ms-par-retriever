package it.gov.pagopa.tkm.ms.parretriever.controller.impl;

import it.gov.pagopa.tkm.ms.parretriever.model.topic.ReadQueue;
import it.gov.pagopa.tkm.ms.parretriever.model.topic.Token;
import it.gov.pagopa.tkm.ms.parretriever.service.impl.ProducerService;
import org.bouncycastle.openpgp.PGPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

//TODO: REMOVE (TEST CONTROLLER)
@RestController
@RequestMapping("/kafka")
public final class KafkaController {

    @Autowired
    private ProducerService producerService;

    @PostMapping
    public void sendMessageToKafkaTopic() throws PGPException {
        ReadQueue readQueue = new ReadQueue(
                "AAABBBCCCDDD1234",
                "1234567890123456789",
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu",
               null,
                Collections.singletonList(new Token("vvvvvvvvvvvvvvvv", "hhhhhhhhhhhhh"))
        );
        producerService.sendMessageV2(readQueue.toString());
    }

}