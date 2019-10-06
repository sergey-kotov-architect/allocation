package com.sergeykotov.allocation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "optimal allocation is being generated, please wait")
public class DataModificationException extends RuntimeException {
}