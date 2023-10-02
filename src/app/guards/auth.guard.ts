

import { CanActivateFn, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Injectable, inject } from '@angular/core';
import { AuthService } from '../services/auth/auth.service';

export const authGuard: CanActivateFn = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
) => {
  const requiredRoles = route.data['roles'] as string[]; // Get the required roles from the route's data
  const authService = inject(AuthService);
  // console.log(requiredRoles);

  if (!authService.isLoggedIn()) {
    // User is not authenticated, redirect to the login page
    return inject(Router).createUrlTree(['/login']);
  }

  // console.log(requiredRoles);
  if (!requiredRoles || requiredRoles.length === 0) {
    // No roles are required; allow access
    return true;
  }

  // Check if the user has any of the required roles
 
  if (authService.hasAnyRole(requiredRoles)) {
    // User is logged in and has the required roles; allow access
    // console.log(requiredRoles);
    return true;
  }

  // Redirect to the login page or an unauthorized page
  return inject(Router).createUrlTree(['/login']);
};
