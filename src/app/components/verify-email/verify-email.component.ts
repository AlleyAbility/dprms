import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
  selector: 'app-verify-email',
  templateUrl: './verify-email.component.html',
  styleUrls: ['./verify-email.component.scss']
})
export class VerifyEmailComponent implements OnInit {
  verificationMessage: string = ''; // Holds the verification message

  constructor(
    private route: ActivatedRoute,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Get the verification token from the URL
    const token : string | null = this.route.snapshot.queryParamMap.get('token');

    // Call a service method to verify the email
    // Assuming token is obtained from your route parameters or some other source
// const token: string | null = ...; // Assign your token here

    if (token !== null) {
      // alert(token)
      this.authService.verifyEmail(token).subscribe({
        next: (response: any) => {
          // Update the verification message based on the response message
          this.verificationMessage = response.message;
      
          // Optionally, you can automatically redirect to the login page after a few seconds
          setTimeout(() => {
            this.redirectToLogin();
          }, 3000); // Redirect after 3 seconds (adjust the delay as needed)
        },
        error: (error: any) => {
          // Handle any errors that occur during email verification
          console.error('Email verification error:', error);
          this.verificationMessage = 'Email verification failed.';
        }
      });
      
    } else {
      // Handle the case where the token is null, e.g., show an error message
      this.verificationMessage = 'Invalid token. Email verification failed.';
    }

  }

  // Function to redirect to the login page
  redirectToLogin(): void {
    this.router.navigate(['/login']);
  }
}
