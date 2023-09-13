import { CanActivateFn, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Injectable, inject } from '@angular/core';
import { AuthService } from '../services/auth/auth.service';

export const authGuard: CanActivateFn = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
) => {
  // console.log('guard status: ', inject(TokenService).authenticated());

  return inject(AuthService).isLoggedIn()
    ? true
    : inject(Router).createUrlTree(['/login']);
};

// export const authGuard: CanActivateFn = (route, state) => {
  
//   const authService = AuthService;
//   const router = AdminDashboardComponent;

//   if (!authService.isLoggedIn()) {
//     // If the user is not authenticated, redirect to the login page
//     router.navigate(['/login']);
//     return false; // Return false to prevent accessing the protected route
//   }


//   return isLoggedIn;
//   // return true;
// };
