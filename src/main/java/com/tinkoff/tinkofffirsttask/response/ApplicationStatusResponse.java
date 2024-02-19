package com.tinkoff.tinkofffirsttask.response;

import jakarta.annotation.Nullable;

import java.time.Duration;

public interface ApplicationStatusResponse {
    record Failure(@Nullable Duration lastRequestTime, int retriesCount) implements ApplicationStatusResponse {
    }

    record Success(String id, String status) implements ApplicationStatusResponse {
    }
}
