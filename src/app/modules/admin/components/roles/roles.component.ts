import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Subject } from 'rxjs';
import Swal from 'sweetalert2';
import { UserService } from '../users/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-roles',
  templateUrl: './roles.component.html',
  styleUrls: ['./roles.component.scss']
})
export class RolesComponent {

  roleForm!: FormGroup
  assignForm!: FormGroup
  removeForm!: FormGroup
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  roleList: any[] = [];
  assignRoleId: number | null = null;
  removeRoleId: number | null = null;


  constructor(private userService: UserService, private router:Router) { }

  ngOnInit(): void {
 
    this.roleForm = new FormGroup({
      name: new FormControl('', [Validators.required]),
    })
    this.assignForm = new FormGroup({
      email: new FormControl('', [Validators.required,Validators.email]),
    })
    this.removeForm = new FormGroup({
      email: new FormControl('', [Validators.required,Validators.email]),
    })
    this. getRole()
    this.dtOptions = {
      pagingType: 'simple_numbers',
      pageLength: 7,
      processing: true,
      responsive: true
    };
  }

  addRole() {

    if (this.roleForm.valid) {

      this.userService.addRole(this.roleForm.value).subscribe({
        next: (response: any[]) => {
          // console.log(response)

          Swal.fire('Success', 'Role added successfully!', 'success');
          // this.router.navigate(['/admin/roles']);
        },
        error: (error) => {
          console.error(error);
        }
      })
    }

  }

  getRole() {
    this.userService.getRoles().subscribe({
      next: (response) => {
        this.roleList = response
        this.dtTrigger.next(null);
        // console.log(response)
      },
      error: (error) => {
        console.log(error)
      }
    })
  }

  openAssignModal(roleId: number): void {
    this.assignRoleId = roleId;
  }

  openRemoveModal(roleId: number): void {
      this.removeRoleId = roleId;
  }

  assign() {
    // Check if the email FormControl exists and is valid
    const emailControl = this.assignForm.get('email');

    if (emailControl !== null && emailControl.valid && this.assignRoleId !== null) {
        const userEmail = emailControl.value; // Access the value of the FormControl
        const roleId = this.assignRoleId;
        this.userService.getUserRoles(userEmail).subscribe({
            next: (response: any) => {
              this.userService.assign(response.id, roleId).subscribe({
                next: (response) => {
                  Swal.fire('Success', 'User assigned role successfully!', 'success');
                },
                error: (error) => {
                    Swal.fire('Error', 'Error in assigning user role!', 'error');
                    // console.log(error);
                }
              })
            },
            error: (error) => {
                Swal.fire('Error', 'Error in assigning user role!', 'error');
                // console.log(error);
            }
        });
    } else {
        // Handle the case where emailControl is null or the FormControl is invalid
        console.error('Invalid or null email address');
        Swal.fire('Error', 'Invalid or null email address!', 'error');
    }
}


remove() {
  // Check if the email FormControl exists and is valid
  const emailControl = this.removeForm.get('email');

  if (emailControl !== null && emailControl.valid && this.removeRoleId !== null) {
      const userEmail = emailControl.value; // Access the value of the FormControl
      const roleId = this.removeRoleId;
      this.userService.getUserRoles(userEmail).subscribe({
          next: (response: any) => {
            this.userService.remove(response.id, roleId).subscribe({
              next: (response) => {
                Swal.fire('Success', 'User removed from role successfully!', 'success');
              },
              error: (error) => {
                  Swal.fire('Error', 'Error in removing user from role!', 'error');
                  // console.log(error);
              }
            })
          },
          error: (error) => {
              Swal.fire('Error', 'Error in removing user from role!', 'error');
              // console.log(error);
          }
      });
  } else {
      // Handle the case where emailControl is null or the FormControl is invalid
      console.error('Invalid or null email address');
      Swal.fire('Error', 'Invalid or null email address!', 'error');
  }
}



}
