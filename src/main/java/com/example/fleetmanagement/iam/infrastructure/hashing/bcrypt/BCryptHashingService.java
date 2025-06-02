package com.example.fleetmanagement.iam.infrastructure.hashing.bcrypt;

import com.example.fleetmanagement.iam.application.internal.outboundservices.hashing.HashingService;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface BCryptHashingService extends HashingService, PasswordEncoder {
}
