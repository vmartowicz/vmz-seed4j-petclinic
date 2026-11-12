package com.petclinic.shared.kipe.application;

import com.petclinic.shared.authentication.application.AuthenticatedUser;
import com.petclinic.shared.authentication.domain.Role;
import com.petclinic.shared.authentication.domain.Roles;
import com.petclinic.shared.authentication.domain.Username;
import com.petclinic.shared.error.domain.Assert;
import com.petclinic.shared.kipe.domain.Action;
import com.petclinic.shared.kipe.domain.Resource;
import com.petclinic.shared.kipe.domain.RolesAccesses;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JhipetclinicAuthorizations {

  private final RolesAccesses accesses;

  public JhipetclinicAuthorizations(Collection<RolesAccesses> accesses) {
    this.accesses = accesses.stream().reduce(RolesAccesses.EMPTY, RolesAccesses::merge);
  }

  public boolean allAuthorized(Authentication authentication, String action, Resource resource) {
    if (missingAuthenticationInfo(authentication, action, resource)) {
      return false;
    }

    return accesses.allAuthorized(roles(authentication), action(action), resource);
  }

  public Username getUsername(Authentication authentication) {
    String principal = AuthenticatedUser.readPrincipal(authentication);
    Assert.notNull("principal", principal);

    return new Username(principal);
  }

  public boolean specificAuthorized(Authentication authentication, String action, Resource resource) {
    if (missingAuthenticationInfo(authentication, action, resource)) {
      return false;
    }

    return accesses.specificAuthorized(roles(authentication), action(action), resource);
  }

  private Roles roles(Authentication authentication) {
    Set<Role> role = authentication
      .getAuthorities()
      .stream()
      .map(GrantedAuthority::getAuthority)
      .map(Role::from)
      .collect(Collectors.toSet());

    return new Roles(role);
  }

  private Action action(String action) {
    return new Action(action);
  }

  private boolean missingAuthenticationInfo(Authentication authentication, String action, Resource resource) {
    return authentication == null || StringUtils.isBlank(action) || resource == null;
  }
}
