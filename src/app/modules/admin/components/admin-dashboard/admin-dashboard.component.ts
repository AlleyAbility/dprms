import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth/auth.service';
import Swal from 'sweetalert2';
import { UserService } from '../users/user.service';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.scss']
})
export class AdminDashboardComponent implements OnInit {

  constructor(private auth: AuthService, private userService: UserService) { }

  ngOnInit(): void {
  }
  status: boolean = false;
  clickEvent(){
      this.status = !this.status;       
  }

  isAdmin(): boolean {
    // Check if the user has the 'ROLE_ADMIN' role
    const userRoles = this.auth.getUserRoles(); // Replace with your actual method to get user roles
    return userRoles.includes('ROLE_ADMIN');
  }
  

  logout(): void {
      Swal.fire({
        title: 'Are you sure?',
        text: 'This process is irreversible.',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Yes',
        cancelButtonText: 'No',
      }).then((result) => {
        if (result.value) {
          this.auth.logout();

          // Swal.fire('Removed!', 'Data removed successfully.', 'success');
        } else if (result.dismiss === Swal.DismissReason.cancel) {
          Swal.fire('Cancelled', 'Process Failed!', 'error');
        }
      });
    }
    
  }
