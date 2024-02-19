package com.tinkoff.tinkofffirsttask.handler;

import com.tinkoff.tinkofffirsttask.client.Client;
import com.tinkoff.tinkofffirsttask.handler.impl.HandlerImpl;
import com.tinkoff.tinkofffirsttask.response.ApplicationStatusResponse;
import com.tinkoff.tinkofffirsttask.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class HandlerImplTest {

    @Mock
    private Client client;

    @InjectMocks
    private HandlerImpl handler;

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(handler, "asyncExecutor", executorService);
    }

    @Test
    void performOperationShouldReturnSuccess() throws Exception {
        Response.Success successResponse = new Response.Success("Approved", "123");
        when(client.getApplicationStatus1(anyString())).thenReturn(successResponse);
        when(client.getApplicationStatus2(anyString())).thenReturn(successResponse);

        CompletableFuture<ApplicationStatusResponse> future = CompletableFuture.supplyAsync(() -> handler.performOperation("123"), executorService);
        ApplicationStatusResponse response = future.get();

        assertInstanceOf(ApplicationStatusResponse.Success.class, response);
    }
}
