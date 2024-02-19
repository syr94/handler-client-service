package com.tinkoff.tinkofffirsttask.handler.impl;

import com.tinkoff.tinkofffirsttask.client.Client;
import com.tinkoff.tinkofffirsttask.handler.Handler;
import com.tinkoff.tinkofffirsttask.response.ApplicationStatusResponse;
import com.tinkoff.tinkofffirsttask.response.Response;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
public class HandlerImpl implements Handler {

    private final Client client;
    private final Executor asyncExecutor;

    public HandlerImpl(Client client, @Qualifier("asyncExecutor") Executor asyncExecutor) {
        this.client = client;
        this.asyncExecutor = asyncExecutor;
    }

    /**
     * Асинхронно запрашивает статус заявки по идентификатору из двух сервисов и возвращает первый полученный ответ.
     * В случае таймаута или ошибки возвращает соответствующий статус неудачи.
     *
     * @param id идентификатор заявки
     * @return статус заявки в виде {@link ApplicationStatusResponse}
     * @throws InterruptedException если поток был прерван во время ожидания
     * @throws ExecutionException   если выполнение задачи завершилось с ошибкой
     * @throws TimeoutException     если не получен ответ в течение заданного времени ожидания
     */
    @Override
    public ApplicationStatusResponse performOperation(String id) {
        CompletableFuture<Response> future1 = CompletableFuture.supplyAsync(() -> client.getApplicationStatus1(id), asyncExecutor);
        CompletableFuture<Response> future2 = CompletableFuture.supplyAsync(() -> client.getApplicationStatus2(id), asyncExecutor);

        try {
            Response response = (Response) CompletableFuture.anyOf(future1, future2)
                    .get(15, TimeUnit.SECONDS);

            if (response == null) {
                return new ApplicationStatusResponse.Failure(null, 0);
            } else if (response instanceof Response.Success) {
                Response.Success success = (Response.Success) response;
                return new ApplicationStatusResponse.Success(success.applicationId(), success.applicationStatus());
            } else {
                return new ApplicationStatusResponse.Failure(null, 1);
            }
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            return new ApplicationStatusResponse.Failure(null, 1);
        } catch (TimeoutException e) {
            return new ApplicationStatusResponse.Failure(null, 0);
        }
    }
}
