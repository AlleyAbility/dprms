import { Router } from '@angular/router';
import { AuthService } from './../../services/auth/auth.service';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { faLock } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  faLock = faLock;
  hide = true;
  email = new FormControl('', [Validators.required, Validators.email]);
  password = new FormControl('', [Validators.required]);

  loginForm!:FormGroup
 
  constructor(private auth: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.formConfiguration()
    if (this.auth.isLoggedIn()) {
      this.router.navigate(['/admin/home']);
    }
  }
  formConfiguration() {
    this.loginForm = new FormGroup({
      password:new FormControl(''),
      email:new FormControl('',[Validators.email]),
    })
  }
  errorMessage: string = '';

  onSubmit(): void {
    if (this.loginForm.valid) {
      this.auth.login(this.loginForm.value).subscribe({
        next: (response) => {
          console.log(response);
          this.router.navigate(['/admin/home']);
        },
        error: (error) => {
          console.log(error);
          this.errorMessage = 'Incorrect Email/Password.';
        },
      });
    }
  }

  getErrorMessage() {
    if (this.email.hasError('email')) {
      return 'Not a valid email';
    }

    return this.email.hasError('email') ? 'Not a valid email' : '';
  }

  getPErrorMessage() {
    if (this.password.hasError('required')) {
      return 'You must enter a value';
    }

    return this.password.hasError('password') ? 'Try another' : '';
  }

}
