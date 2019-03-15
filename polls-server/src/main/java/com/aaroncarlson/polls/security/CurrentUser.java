package com.aaroncarlson.polls.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

/**
 * Spring security provides an annotation called @AuthenticationPrincipal to access the
 * currently authenticated user in the controllers.
 *
 * In addition created a meta-annotation so that the project didn't get too tied up with
 * Spring Security related annotations everywhere in our project. This reduces the dependency
 * on Spring Security. If the logic is changed to remove Spring Security from the project, it
 * can easily be done by changing this annotation class CurrentUser.
 */
@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {
}
