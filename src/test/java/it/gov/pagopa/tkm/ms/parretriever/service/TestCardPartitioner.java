package it.gov.pagopa.tkm.ms.parretriever.service;

import com.fasterxml.jackson.databind.*;
import it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager.*;
import it.gov.pagopa.tkm.ms.parretriever.constant.*;
import it.gov.pagopa.tkm.ms.parretriever.service.impl.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestCardPartitioner {

    @InjectMocks
    private CardPartitioner cardPartitioner;

    @Mock
    private ParlessCardsClient parlessCardsClient;

    private DefaultBeans testBeans;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void init() {
        testBeans = new DefaultBeans();
    }

    //TODO TESTS

    @Test
    void givenEmptyList_returnEmptyMap(){
        ReflectionTestUtils.setField(cardPartitioner, "maxNumberOfCards", 100);

        when(parlessCardsClient.getParlessCards(100)).thenReturn(new ArrayList<>());
            Map<String, ExecutionContext> executionContextMap= new HashMap<>();
            assertEquals(cardPartitioner.partition(15), executionContextMap);
    }

    @Test
    void givenNullList_returnEmptyMap(){
        ReflectionTestUtils.setField(cardPartitioner, "maxNumberOfCards", 100);

        when(parlessCardsClient.getParlessCards(100)).thenReturn(null);
        Map<String, ExecutionContext> executionContextMap= new HashMap<>();
        assertEquals(cardPartitioner.partition(15), executionContextMap);

    }

    @Test
    void givenZeroAvailableThredas_returnEmptyMap(){
        Map<String, ExecutionContext> executionContextMap= new HashMap<>();
        assertEquals(cardPartitioner.partition(0), executionContextMap);

    }


    @Test
    void givenCardsList_returnExecutionContext(){
        ReflectionTestUtils.setField(cardPartitioner, "maxNumberOfCards", 15);
        ReflectionTestUtils.setField(cardPartitioner, "amexMaxApiClientCallRate", 5d);
        ReflectionTestUtils.setField(cardPartitioner, "mastercardMaxApiClientCallRate", 5d);
        ReflectionTestUtils.setField(cardPartitioner, "visaMaxApiClientCallRate", 5d);

        when(parlessCardsClient.getParlessCards(15)).thenReturn(testBeans.PARLESS_CARD_LIST);
       Map<String, ExecutionContext> mapA= cardPartitioner.partition(15);
       Map<String, ExecutionContext> mapB= testBeans.EXECUTION_CONTEXT_MAP_15_THREADS;

       if (mapA.equals(mapB)){
           System.out.println("\n \n \n ________ MAPS ARE EQUAL");
        } else {
           System.out.println("\n \n \n ________ MAPS ARE NOT EQUAL");
       }

        assertEquals(cardPartitioner.partition(15), testBeans.EXECUTION_CONTEXT_MAP_15_THREADS);

    }


}
