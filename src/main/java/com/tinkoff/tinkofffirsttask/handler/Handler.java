package com.tinkoff.tinkofffirsttask.handler;

import com.tinkoff.tinkofffirsttask.response.ApplicationStatusResponse;

public interface Handler {
    ApplicationStatusResponse performOperation(String id);
}
