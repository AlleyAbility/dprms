import { Component, OnInit } from '@angular/core';
import { UserService } from '../users/user.service';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  user: any; // Define a variable to hold the user data

  constructor(private userService: UserService, private auth: AuthService) {}

  ngOnInit(): void {
    this.getUser();
  }

  getUser() {
    const userId = this.auth.userId;
  
    // Check if userId is not null before making the request
    if (userId !== null) {
      this.userService.getUserById(userId).subscribe({
        next: (response) => {
          this.user = response; // Store the user data in the 'user' variable
          // console.log(this.user);
        },
        error: (error) => {
          console.log(error);
        }
      });
    } else {
      console.error('userId is null'); // Handle the case where userId is null
    }
  }
  
}
