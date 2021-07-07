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
        ReflectionTestUtils.setField(cardPartitioner, "maxNumberOfCards", 300);
        ReflectionTestUtils.setField(cardPartitioner, "amexMaxApiClientCallRate", 5d);
        ReflectionTestUtils.setField(cardPartitioner, "mastercardMaxApiClientCallRate", 5d);
        ReflectionTestUtils.setField(cardPartitioner, "visaMaxApiClientCallRate", 5d);

        when(parlessCardsClient.getParlessCards(300)).thenReturn(testBeans.PARLESS_CARD_LIST);

        assertEquals(cardPartitioner.partition(15), testBeans.EXECUTION_CONTEXT_MAP_15_THREADS);

    }

    @Test
    void givenCardsListWithLessThread_returnExecutionContext(){
        ReflectionTestUtils.setField(cardPartitioner, "maxNumberOfCards", 300);
        ReflectionTestUtils.setField(cardPartitioner, "amexMaxApiClientCallRate", 5d);
        ReflectionTestUtils.setField(cardPartitioner, "mastercardMaxApiClientCallRate", 5d);
        ReflectionTestUtils.setField(cardPartitioner, "visaMaxApiClientCallRate", 5d);

        when(parlessCardsClient.getParlessCards(300)).thenReturn(testBeans.PARLESS_CARD_LIST);

        assertEquals(cardPartitioner.partition(12), testBeans.EXECUTION_CONTEXT_MAP_12_THREADS);

    }


    @Test
    void givenCardsListWithLessThreadUnbalanced_returnExecutionContext(){
        ReflectionTestUtils.setField(cardPartitioner, "maxNumberOfCards", 90);
        ReflectionTestUtils.setField(cardPartitioner, "amexMaxApiClientCallRate", 5d);
        ReflectionTestUtils.setField(cardPartitioner, "mastercardMaxApiClientCallRate", 5d);
        ReflectionTestUtils.setField(cardPartitioner, "visaMaxApiClientCallRate", 5d);

        when(parlessCardsClient.getParlessCards(90)).thenReturn(testBeans.PARLESS_CARD_LIST_UNBALANCED);

        assertEquals(cardPartitioner.partition(9), testBeans.EXECUTION_CONTEXT_MAP_THREADS_UNBALANCED);

    }


    @Test
    void givenFewCardsList_returnExecutionContext(){
        ReflectionTestUtils.setField(cardPartitioner, "maxNumberOfCards", 10);
        ReflectionTestUtils.setField(cardPartitioner, "amexMaxApiClientCallRate", 5d);
        ReflectionTestUtils.setField(cardPartitioner, "mastercardMaxApiClientCallRate", 5d);
        ReflectionTestUtils.setField(cardPartitioner, "visaMaxApiClientCallRate", 5d);

        when(parlessCardsClient.getParlessCards(10)).thenReturn(testBeans.PARLESS_CARD_SMALL_LIST);

        assertEquals(cardPartitioner.partition(12), testBeans.EXECUTION_CONTEXT_MAP_THREADS_SMALL);

    }


}
