package com.empresa.aplicacion.auditor;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditorAwareImpl implements AuditorAware<String> {

	// Para obtener el usuario en auditable
    @Override
    public Optional<String> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    if (authentication == null || !authentication.isAuthenticated()) {
	    	return null;
	    }

	    return Optional.of(authentication.getName());

//        return Optional.of("COD_USUARIO");
    }
}

