package com.tinkoff.tinkofffirsttask.client.impl;

import com.tinkoff.tinkofffirsttask.client.Client;
import com.tinkoff.tinkofffirsttask.response.Response;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Random;

@Component
public class ClientImpl implements Client {

    private final Random random = new Random();

    @Override
    public Response getApplicationStatus1(String id) {
        try {
            Thread.sleep(random.nextInt(100, 500));
            if (random.nextBoolean()) {
                return new Response.Success("Approved", id);
            } else {
                return new Response.RetryAfter(Duration.ofSeconds(1));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new Response.Failure(e);
        }
    }

    @Override
    public Response getApplicationStatus2(String id) {
        try {
            Thread.sleep(random.nextInt(100, 500));
            if (random.nextBoolean()) {
                return new Response.Success("Denied", id);
            } else {
                return new Response.Failure(new RuntimeException("Service unavailable"));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new Response.Failure(e);
        }
    }
}