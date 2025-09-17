package com.pnam.validator;

import com.pnam.pojo.Payment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PaymentValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Payment.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Payment p = (Payment) target;

        if (p.getAmount() == null || p.getAmount().doubleValue() <= 0) {
            errors.rejectValue("amount", "payment.amount.positive", "Số tiền phải > 0");
        }
        if (p.getMethod() == null || p.getMethod().isBlank()) {
            errors.rejectValue("method", "payment.method.required", "Phương thức bắt buộc");
        }
        if (p.getStatus() == null || p.getStatus().isBlank()) {
            errors.rejectValue("status", "payment.status.required", "Trạng thái bắt buộc");
        }
        if (p.getEnrollmentId() == null || p.getEnrollmentId().getId() == null) {
            errors.rejectValue("enrollmentId", "payment.enrollment.required", "Phải gắn với Enrollment");
        }
    }
}
