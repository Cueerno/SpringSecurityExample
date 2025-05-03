package com.radiuk.securityexample.handler;

import java.time.OffsetDateTime;

public record ExceptionResponseStructure(
        OffsetDateTime timestamp,
        Integer httpStatus,
        String error,
        Object message
) {
}
