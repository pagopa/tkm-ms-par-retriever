package it.gov.pagopa.tkm.ms.parretriever.constant;

import it.gov.pagopa.tkm.ms.parretriever.client.external.amex.model.response.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.model.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.visa.model.response.*;
import it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager.model.response.*;
import it.gov.pagopa.tkm.ms.parretriever.client.internal.consentmanager.model.response.*;
import org.springframework.batch.item.ExecutionContext;

import java.time.*;
import java.util.*;

public class DefaultBeans {

    public final String PAN_1 = "111111111111";
    public final String PAN_2 = "211111111111";
    public final String PAN_AMEX = "374477742523440";
    public final String PAN_MASTERCARD = "5340160579206608";
    public final String PAN_VISA = "4024007111341771";

    public final String PAR_1 = "abc11111111111";
    public final String PAR_2 = "xyz11111111111";
    public final String TAX_CODE = "PCCRLE04M24L219D";
    public final String HPAN_1 = "92fc472e8709cf61aa2b6f8bb9cf61aa2b6f8bd8267f9c14f58f59cf61aa2b6f";
    public final String HPAN_2 = "15fc472e8709cf61aa2b6f8bb9cf61aa2b6f8bd8267f9c14f58f59cf61aa2b6a";
    public final String HPAN_3 = "00fc472e8709cf61aa2b6f8bb9cf61aa2b6f8bd8267f9c14f58f59cf61aa2b00";

    public static final Instant INSTANT = Instant.EPOCH;

    public final AmexParResponse[] AMEX_PAR_RESPONSE = new AmexParResponse[]{new AmexParResponse(PAN_1, PAR_1, true)};

    public final VisaParEncryptedResponse VISA_PAR_ENC_RESPONSE = new VisaParEncryptedResponse("eyJlbmMiOiJBMTI4R0NNIiwiaWF0IjowLCJhbGciOiJSU0EtT0FFUC0yNTYiLCJraWQiOiJURVNUX0tFWV9JRCJ9.UdBI3Dy8AFg3PDPRjI453g7CK5Nq5YWnsaSOj199O9YhWRDd9Rf55b4ytF9pJ9jz_4MdhzzHSvVCbsTkQ5QAYir6tr9IbTtkn8eozGecUfKRowpztsFoWdN8mcr5bpot1bK5OVI2xQFmNixrj_ZPxkDk9FS1cxHIcMHiPm3K_YiaS_K8tMLng1oTH-XQkFE0AEM6P2HFyzun0SwkQP4nkYMI3wYgDPTEAos4orG2vgmFBR3CEXkQkkgodQu0YuOqwD8lhmp8fFTdHe-3nQ9u8k12z5whcOmcyFo7g8fDiGggTXZFbc_a5Unt9yvrzGiS4IHQk01RkK1eykh-rRCAuQ.eLGi-GPm_OJBv3jR.gjixjPlSXJhgjHm8OwxPPGfx343sy18NesE6ECtMKgtKUD7hEckmwHdYhPj_5DJ8srk1r3rgs35CWK_vX95fi2UrejQRG3vJZoKfqaeUCLfZwnI8xOQIGIO-J93WlXEkEcPl9jauF35aPWZx1ucGXxrheGnTnlJGWuHCYiUYQa8KD9x6TOpUzqk.TRwZRUVpEQW78ux2BnnA9g");
    public final VisaParDecryptedResponse VISA_PAR_DEC_RESPONSE = new VisaParDecryptedResponse(PAR_1, INSTANT.toString(), PAN_1);

    public final MastercardParResponse MASTERCARD_PAR_RESPONSE = new MastercardParResponse("RESPONSE_ID_TEST", new ParResponseEncryptedPayload(new ParResponseEncryptedData(PAR_1, INSTANT.toString())));

    public final String TOKEN_1 = "abcde123";
    public final String TOKEN_2 = "xyz6543";
    public final String TOKEN_3 = "aerr126";

    public final Set<String> TOKEN_SET_1 = new HashSet<>(Arrays.asList(TOKEN_1, TOKEN_2));
    public final Set<String> TOKEN_SET_2 = new HashSet<>(Arrays.asList(TOKEN_2, TOKEN_3));

    public final ParlessCard PARLESS_CARD_1 = new ParlessCard(
            TAX_CODE, PAN_1, HPAN_1, TOKEN_SET_1, CircuitEnum.AMEX
    );
    public final ParlessCard PARLESS_CARD_2 = new ParlessCard(
            TAX_CODE, PAN_2, HPAN_2, TOKEN_SET_2, CircuitEnum.VISA
    );

    public final ParlessCard PARLESS_AMEX_CARD = new ParlessCard(
            TAX_CODE, PAN_AMEX, HPAN_1, TOKEN_SET_1, CircuitEnum.AMEX
    );
    public final ParlessCard PARLESS_VISA_CARD = new ParlessCard(
            TAX_CODE, PAN_VISA, HPAN_2, TOKEN_SET_2, CircuitEnum.VISA
    );
    public final ParlessCard PARLESS_VISA_ELECTRON_CARD = new ParlessCard(
            TAX_CODE, PAN_VISA, HPAN_2, TOKEN_SET_2, CircuitEnum.VISA_ELECTRON
    );
    public final ParlessCard PARLESS_VPAY_CARD = new ParlessCard(
            TAX_CODE, PAN_VISA, HPAN_2, TOKEN_SET_2, CircuitEnum.VPAY
    );

    public final ParlessCard PARLESS_MASTERCARD_CARD = new ParlessCard(
            TAX_CODE, PAN_MASTERCARD, HPAN_3, TOKEN_SET_2, CircuitEnum.MASTERCARD
    );

    public final List<ParlessCard> PARLESS_CARDS_LIST = Arrays.asList(PARLESS_CARD_1, PARLESS_CARD_2);

    public final String MASTERCARD_RESPONSE_PRIVATE_KEY = "-----BEGIN RSA PRIVATE KEY-----\n" +
            "MIICXAIBAAKBgQCMhM/5iBYjLJFKM5sZRXJvGu/K5nLDf1v4oaTM+DxZaH9q1gwZ\n" +
            "BJ3OVe9Dc6l63FSD8LH0fgzIC9Xbnal4LGkymroFZu1HSzFZWLXuAm3F8xMojAZV\n" +
            "k6DuAMVff0zhv+7lh/qs71Y1qwV4YXtpYqRaA0Nipq0xHa56pCLIQnty0QIDAQAB\n" +
            "AoGAZKhW2qbzRKsOQJsLBWVL/e7LywqdNxGqbkZMaU+A24MRkxRVgi5eZIoGZMkb\n" +
            "pJjkKcdnkn4g5cQko7ciyKT3jWXd3Pw/j6gUtEVeooDSAR5QPjW3J1wh06bAk0Rz\n" +
            "E3l0V1g+Fna91BBqbeWblceKP8iRN0ak5VZhkT2jrkuS3OkCQQDk9A7d4CZxQ3JK\n" +
            "csw0EKH7frl9TUJ/EQ33RbzpKnRWdBITsNnXPkg6Dyr7IpF4CL4tR23fRbqEGiC/\n" +
            "rqBHC+nLAkEAnR5XgFWUENb8oK7oRKvDZDViRpZz2r33OG0uihpmoCGp9ANr6t00\n" +
            "VHNk5v+wYuyfj7MVB3dG/NTXXG1fdz8yUwJBAKWC7XBmR0Qf/vJk9Mw6re7SWKOF\n" +
            "g6m6GX+FUaC5iSqqdBr6ATECHFKkfmzYO5MrtteiyIHctY4kGUKYqdNSgGsCQH0h\n" +
            "Rwq8HTn0HipSYkUejnRmV6hj9kzFcfiMa74tHIJ3jQT7HJ1mImEHrdqS1AVU8hbd\n" +
            "xue1ROHjGMlRifBMcmUCQBcBArUv6frNTeAhgXJxmjWxaV4Ua6uU901yISiXXGvh\n" +
            "SdBhLBGIeHh/UfW3Dz3o3RPJJgh5rlpdnhRitfVfmt8=\n" +
            "-----END RSA PRIVATE KEY-----";

    public final List<ParlessCard> PARLESS_CARD_LIST = createParlessCardList();
    public final List<ParlessCard> PARLESS_CARD_LIST_UNBALANCED = createParlessCardListUnbalanced();
    private  List<ParlessCard> PARLESS_AMEX_CARD_LIST;
    private  List<ParlessCard> PARLESS_AMEX_CARD_LIST_2;
    private List<ParlessCard> PARLESS_VISA_CARD_LIST;
    private List<ParlessCard> PARLESS_VISA_CARD_LIST_2;
    private List<ParlessCard> PARLESS_MASTERCARD_CARD_LIST;
    private List<ParlessCard> PARLESS_MASTERCARD_CARD_LIST_2;

    public Map<String, ExecutionContext> EXECUTION_CONTEXT_MAP_15_THREADS = createExecutionsContextMap15Threads();
    public Map<String, ExecutionContext> EXECUTION_CONTEXT_MAP_12_THREADS = createExecutionsContextMap12Threads();
    public Map<String, ExecutionContext> EXECUTION_CONTEXT_MAP_12_THREADS_UNBALANCED = createExecutionsContextMapThreadsUnbalanced();


    public ConsentResponse CARD_CONSENT_RESPONSE_ALLOW = new ConsentResponse(ConsentEntityEnum.Allow, Instant.now(),
            new HashSet<>(Collections.singletonList(new CardServiceConsent("hashpan",
                    new HashSet<>(Collections.singletonList(new ServiceConsent(ConsentRequestEnum.Allow, ServiceEnum.FA))))))
             );

    public ConsentResponse CARD_CONSENT_RESPONSE_DENY = new ConsentResponse(ConsentEntityEnum.Deny, Instant.now(),
            new HashSet<>(Collections.singletonList(new CardServiceConsent("hashpan",
                    new HashSet<>(Collections.singletonList(new ServiceConsent(ConsentRequestEnum.Allow, ServiceEnum.FA))))))
    );

    public ConsentResponse CARD_CONSENT_RESPONSE_PARTIAL = new ConsentResponse(ConsentEntityEnum.Partial, Instant.now(),
            new HashSet<>(Collections.singletonList(new CardServiceConsent("hashpan",
                    new HashSet<>(Collections.singletonList(new ServiceConsent(ConsentRequestEnum.Allow, ServiceEnum.FA))))))
    );


    private List<ParlessCard> createParlessCardList(){
        List<ParlessCard> parlessCards = new ArrayList<>();
        PARLESS_AMEX_CARD_LIST=createAmexParlessCardList(100);
        PARLESS_VISA_CARD_LIST=createVisaParlessCardList(40, 40, 20);
        PARLESS_MASTERCARD_CARD_LIST=createMastercardParlessCardList(100);
        parlessCards.addAll(PARLESS_AMEX_CARD_LIST);
        parlessCards.addAll(PARLESS_VISA_CARD_LIST);
        parlessCards.addAll(PARLESS_MASTERCARD_CARD_LIST);

        return parlessCards;
    }


    private List<ParlessCard> createParlessCardListUnbalanced(){

        List<ParlessCard> parlessCards = new ArrayList<>();
        PARLESS_AMEX_CARD_LIST_2=createAmexParlessCardList(10);
        PARLESS_VISA_CARD_LIST_2=createVisaParlessCardList(4,4,2);
        PARLESS_MASTERCARD_CARD_LIST_2=createMastercardParlessCardList(70);
        parlessCards.addAll(PARLESS_AMEX_CARD_LIST_2);
        parlessCards.addAll(PARLESS_VISA_CARD_LIST_2);
        parlessCards.addAll(PARLESS_MASTERCARD_CARD_LIST_2);

        return parlessCards;
    }



    private List<ParlessCard> createAmexParlessCardList(int size){
        List<ParlessCard> parlessCards = new ArrayList<>();

        for (int i=0; i<size; i++){
            parlessCards.add(PARLESS_AMEX_CARD);
        }
        return parlessCards;
    }

    private List<ParlessCard> createVisaParlessCardList(int size1, int size2, int size3){
        List<ParlessCard> parlessCards = new ArrayList<>();

        for (int i=0; i<size1; i++){
            parlessCards.add(PARLESS_VISA_CARD);
        }
        for (int i=0; i<size2; i++){
            parlessCards.add(PARLESS_VISA_ELECTRON_CARD);
        }
        for (int i=0; i<size3; i++){
            parlessCards.add(PARLESS_VPAY_CARD);
        }
        return parlessCards;
    }

    private List<ParlessCard> createMastercardParlessCardList(int size){
        List<ParlessCard> parlessCards = new ArrayList<>();

        for (int i=0; i<size; i++){
            parlessCards.add(PARLESS_MASTERCARD_CARD);
        }

        return parlessCards;
    }

    public final Map<String, ExecutionContext> createExecutionsContextMap15Threads(){
        Map<String, ExecutionContext> executionContextMap = new TreeMap<>();
        createVisaExecutionContextsMap(executionContextMap, 0, 20, 5, false);
        createMastercardExecutionContextsMap(executionContextMap, 5, 20, 5, false);
        createAmexExecutionContextsMap(executionContextMap, 10, 20, 5, false);

        return executionContextMap;

    };

    public final Map<String, ExecutionContext> createExecutionsContextMap12Threads(){
        Map<String, ExecutionContext> executionContextMap = new TreeMap<>();
        createVisaExecutionContextsMap(executionContextMap, 0, 25, 4, false);
        createMastercardExecutionContextsMap(executionContextMap, 4, 25, 4, false);
        createAmexExecutionContextsMap(executionContextMap, 8, 25, 4, false);

        return executionContextMap;

    };


    public final Map<String, ExecutionContext> createExecutionsContextMapThreadsUnbalanced(){
        Map<String, ExecutionContext> executionContextMap = new TreeMap<>();
        createVisaExecutionContextsMap(executionContextMap, 0, 5, 2, true);
        createMastercardExecutionContextsMap(executionContextMap, 2, 14, 5, true);
        createAmexExecutionContextsMap(executionContextMap, 7, 5, 2, true);

        return executionContextMap;

    };


    private Map<String, ExecutionContext> createAmexExecutionContextsMap(Map<String, ExecutionContext> executionContextMap,
                                                                         int circuitIndex, int step, int numberOfThreads,
                                                                         boolean secondList){
        List<ParlessCard> amexCircuitCards=secondList? PARLESS_AMEX_CARD_LIST_2:PARLESS_AMEX_CARD_LIST;

        for (int i=0; i<numberOfThreads; i++){
            ExecutionContext value = new ExecutionContext();
            int from= i*step;
            int to= (i+1)*step;
            int j = 1+ i + circuitIndex;

            value.putInt("from", from);
            value.putInt("to", to);
            value.putString("name", "Thread" + j);
            value.put("cardList", new ArrayList<>(amexCircuitCards.subList(from,  to)));
            value.put("rateLimit", (double)5/numberOfThreads);
            executionContextMap.put("partition" + j, value);

        }
        return executionContextMap;
    }


    private Map<String, ExecutionContext> createVisaExecutionContextsMap(Map<String, ExecutionContext> executionContextMap,
                                                                         int circuitIndex, int step, int numberOfThreads,
                                                                         boolean secondList){
        List<ParlessCard> visaCircuitCards= secondList? PARLESS_VISA_CARD_LIST_2:PARLESS_VISA_CARD_LIST;

        for (int i=0; i<numberOfThreads; i++){
            ExecutionContext value = new ExecutionContext();
            int from= i*step;
            int to= (i+1)*step;
            int j = 1+ i + circuitIndex;

            value.putInt("from", from);
            value.putInt("to", to);
            value.putString("name", "Thread" + j);
            value.put("cardList", new ArrayList<>(visaCircuitCards.subList(from,  to)));
            value.put("rateLimit", (double)5/numberOfThreads);
            executionContextMap.put("partition" + j, value);

        }
        return executionContextMap;
    }

    private Map<String, ExecutionContext> createMastercardExecutionContextsMap(Map<String, ExecutionContext> executionContextMap,
                                                                               int circuitIndex, int step, int numberOfThreads,
                                                                               boolean secondList){
        List<ParlessCard> mastercardCircuitCards= secondList? PARLESS_MASTERCARD_CARD_LIST_2:PARLESS_MASTERCARD_CARD_LIST;

        for (int i=0; i<numberOfThreads; i++){
            ExecutionContext value = new ExecutionContext();
            int from= i*step;
            int to= (i+1)*step;
            int j = 1+ i + circuitIndex;

            value.putInt("from", from);
            value.putInt("to", to);
            value.putString("name", "Thread" + j);
            value.put("cardList", new ArrayList<>(mastercardCircuitCards.subList(from,  to)));
            value.put("rateLimit", (double)5/numberOfThreads);
            executionContextMap.put("partition" + j, value);

        }
        return executionContextMap;
    }

}
