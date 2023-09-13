import { Observable, of, tap, throwError } from 'rxjs';
import { Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private apiUrl = 'http://localhost:8080'; // Replace with your Spring Boot backend URL
  private tokenKey = 'authToken'; // Local storage key for the JWT token
  
  authenticated = signal(false);
  constructor(private router: Router, private http: HttpClient) {}

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
          this.setToken(this.tokenKey);
        }),
        // Handle navigation after storing the token
        tap(() => {
          // Use setTimeout to ensure the token is stored before navigation
          setTimeout(() => {
            this.router.navigate(['/admin/home']);
          }, 0);
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
    this.router.navigate(['/login']);
  }

  // login({ email, password }: any): Observable<any> {
  //   if (email === 'admin@gmail.com' && password === 'admin123') {
  //     this.setToken('abcdefghijklmnopqrstuvwxyz');
  //     return of({ name: 'Tarique Akhtar', email: 'admin@gmail.com' });
  //   }else{
  //     // return throwError(new Error('Failed to login'));
  //     return throwError(()=>new  Error('Failed to login'));
  //   }
    
  // }

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
  
}
