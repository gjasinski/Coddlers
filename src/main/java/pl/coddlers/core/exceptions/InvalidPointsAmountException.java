package pl.coddlers.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPointsAmountException extends RuntimeException {
    public InvalidPointsAmountException(int points, int maxPoints) {
        super(String.format("Points should be between 0 to %d. Current grade is %d.", maxPoints, points));
    }
}
