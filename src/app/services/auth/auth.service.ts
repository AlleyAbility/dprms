import { Observable, map, of, tap, throwError } from 'rxjs';
import { Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { UserService } from 'src/app/modules/admin/components/users/user.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private apiUrl = 'http://localhost:8080'; // Replace with your Spring Boot backend URL
  private tokenKey = 'authToken'; // Local storage key for the JWT token
  private userRoles: string[] = []; // Store user roles here
  public userId: number | null = null;
  
  authenticated = signal(false);
  constructor(private router: Router, private http: HttpClient,  private userService: UserService) {}

  // Add the JWT token to HTTP headers for authenticated requests
  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem(this.tokenKey);
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  login({ email, password }: any): Observable<any> {
    return this.http
      .post(`${this.apiUrl}/login`, { email, password }, { responseType: 'text' })
      .pipe(
        tap((token: string) => {
          // Store the JWT token in local storage
          this.setToken(token);

            // Fetch user roles and set them
          this.userService.getUserRoles(email).subscribe((response: any) => {
            // Assuming that the response contains a 'roles' property which is an array of role names
            if (response && response.roles && Array.isArray(response.roles)) {
              const roles = response.roles.map((role: any) => role.name); // Extract role names
              this.setUserRoles(roles);
              this.setUserId(response.id)
              // console.log(this.getUserId())
              // console.log(response.id)
              // console.log(roles);
            }
          });
        // Handle navigation after storing the token
        // tap(() => {
        //   // Use setTimeout to ensure the token is stored before navigation
        //   setTimeout(() => {
        //     this.router.navigate(['/admin/home']);
        //   }, 0);
        // })
      })
      );
  }
  

  setToken(token: string): void {
    localStorage.setItem('token', token);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  isLoggedIn() {
    return this.getToken() !== null;
  }

  logout() {
    localStorage.removeItem('token');
    // Clear user roles on logout
    this.resetUserRoles();
    this.router.navigate(['/login']);
  }

  resetUserRoles() {
    this.userRoles = [];
  }


  verifyEmail(token: string | null): Observable<string> {
    if (!token) {
      // Handle the case where the token is null or undefined
      return of('Invalid token');
    }
  
    // Construct the URL for the email verification endpoint
    const verifyUrl = `${this.apiUrl}/api/v1/users/verifyEmail?token=${token}`;
  
    // Send a GET request to the backend to verify the email
    return this.http.get<string>(verifyUrl);
  }


  hasAnyRole(requiredRoles: string[]): boolean {
    // Check if there's an intersection between userRoles and requiredRoles
    return this.userRoles.some(role => requiredRoles.includes(role));
  }

   // Method to set user roles after a successful login
   setUserRoles(roles: string[]): void {
    this.userRoles = roles;
  }

  getUserRoles(): string [] {
    return this.userRoles;
  }

  setUserId(userId: number): void {
    this.userId = userId;
  }
  
  getUserId(): number | null {
    return this.userId;
  }

  
}
