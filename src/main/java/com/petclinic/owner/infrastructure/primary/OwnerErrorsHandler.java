package com.petclinic.owner.infrastructure.primary;

import com.petclinic.owner.domain.BusinessRuleViolationException;
import com.petclinic.owner.domain.InvalidValueException;
import com.petclinic.owner.domain.UnknownElementException;
import com.petclinic.shared.error.domain.AssertionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackageClasses = OwnerErrorsHandler.class)
class OwnerErrorsHandler {

  @ExceptionHandler(UnknownElementException.class)
  ProblemDetail handleUnknownElement(UnknownElementException exception) {
    return problem(HttpStatus.NOT_FOUND, "Unknown element", exception.getMessage());
  }

  @ExceptionHandler(BusinessRuleViolationException.class)
  ProblemDetail handleBusinessRuleViolation(BusinessRuleViolationException exception) {
    return problem(HttpStatus.CONFLICT, "Business rule violation", exception.getMessage());
  }

  @ExceptionHandler(InvalidValueException.class)
  ProblemDetail handleInvalidValue(InvalidValueException exception) {
    return problem(HttpStatus.BAD_REQUEST, "Invalid value", exception.getMessage());
  }

  @ExceptionHandler(AssertionException.class)
  ProblemDetail handleAssertion(AssertionException exception) {
    ProblemDetail problem = problem(HttpStatus.BAD_REQUEST, "Invalid value", exception.getMessage());
    problem.setProperty("field", exception.field());

    return problem;
  }

  private static ProblemDetail problem(HttpStatus status, String title, String detail) {
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
    problem.setTitle(title);

    return problem;
  }
}
