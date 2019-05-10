package com.aaroncarlson.polls.config;

import com.aaroncarlson.polls.security.UserPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * @EnableJpaAuditing annotation is all that is needed to enable auditing to begin. To automatically populate the
 * createdBy and updatedBy fields add AuditorAware bean which returns SpringSecurityAuditAwareImpl which is a nested
 * class of AuditorConfig (that implements AuditorAware)
 */
@Configuration
@EnableJpaAuditing
public class AuditingConfig {

    @Bean
    public AuditorAware<Long> auditorProvider() {
        return new SpringSecurityAuditAwareImpl();
    }

    public class SpringSecurityAuditAwareImpl implements AuditorAware<Long> {

        @Override
        public Optional<Long> getCurrentAuditor() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null ||
                    !authentication.isAuthenticated() ||
                    authentication instanceof AnonymousAuthenticationToken) {
                return Optional.empty();
            }

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

            return Optional.ofNullable(userPrincipal.getId());
        }
    }
}
