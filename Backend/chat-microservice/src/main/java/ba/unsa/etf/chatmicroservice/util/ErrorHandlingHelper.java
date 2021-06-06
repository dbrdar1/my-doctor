package ba.unsa.etf.chatmicroservice.util;

import ba.unsa.etf.chatmicroservice.exception.ResourceNotFoundException;
import ba.unsa.etf.chatmicroservice.exception.UnauthorizedException;
import ba.unsa.etf.chatmicroservice.response.Response;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

public class ErrorHandlingHelper {

    public static Response handleConstraintViolationException(ConstraintViolationException exception) {
        StringBuilder message = new StringBuilder();
        List<String> messages = exception.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage).collect(Collectors.toList());
        for (int i = 0; i < messages.size(); i++)
            if (i < messages.size() - 1) message.append(messages.get(i)).append("; ");
            else message.append(messages.get(i));
        return new Response(message.toString(),400);
    }

    public static Response handleEntityNotFoundException(ResourceNotFoundException exception) {
        return new Response(exception.getMessage(),404);
    }

    public static Response handleUnauthorizedException(UnauthorizedException exception) {
        return new Response(exception.getMessage(),401);
    }
}
