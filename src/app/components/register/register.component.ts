import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { UserService } from 'src/app/modules/admin/components/users/user.service';
import Swal from 'sweetalert2';

// import custom validator to validate that password and confirm password fields match
// import { MustMatch } from './_helpers';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;
  userList:any[] = [];
  submitted = false;
  hide = true;
  loading = false;

  firstName = new FormControl({ value: null, disabled: true },[Validators.required]);
  lastName = new FormControl({ value: null, disabled: true },[Validators.required]);
  email = new FormControl('',[Validators.required,Validators.email]);
  institutionName = new FormControl({ value: null, disabled: true },[Validators.required]);
  position = new FormControl({ value: null, disabled: true },[Validators.required]);
  division = new FormControl({ value: null, disabled: true },[Validators.required]);
  employeeId = new FormControl('',[Validators.required]);
  phone = new FormControl('',[Validators.required, Validators.pattern(/^\+255\d{9}$/)]);
  password = new FormControl('', [Validators.required, Validators.minLength(6)]);
  confirmPassword = new FormControl('', Validators.required);

  constructor(private userService:UserService) { }

  ngOnInit(): void {
      this.registerForm = new FormGroup({
        firstName:new FormControl({ value: null, disabled: true },[Validators.required]),
        lastName:new FormControl({ value: null, disabled: true },[Validators.required]),
        email:new FormControl({ value: null, disabled: true },[Validators.required,Validators.email]),
        institutionName:new FormControl({ value: null, disabled: true },[Validators.required]),
        position:new FormControl({ value: null, disabled: true },[Validators.required]),
        division:new FormControl({ value: null, disabled: true },[Validators.required]),
        employeeId:new FormControl('',[Validators.required]),
        phone:new FormControl('',[Validators.required, Validators.pattern(/^\+255\d{9}$/)]),
        password: new FormControl('', [Validators.required, Validators.minLength(6)]),
        confirmPassword: new FormControl('', Validators.required),
          // acceptTerms: new FormControl(false, Validators.requiredTrue)
      }, {
          validators: MustMatch('password', 'confirmPassword')
      });

      // this.initForm();
  }

   // Function to handle changes in the employeeId input field
   onEmployeeIdChange(): void {
    const employeeIdControl = this.registerForm.get('employeeId');

    // Guard clause to check for null
    if (!employeeIdControl) {
      return; // Exit early if it's null
    }
    const employeeId = employeeIdControl.value;

    // Check if the employeeId is not empty
    if (employeeIdControl) {
      // Fetch user data based on the entered employeeId
      this.userService.getUserByEmployeeId(employeeId).subscribe({
        next:(userData: any) => {
          // Populate the form fields with fetched data
          this.registerForm.patchValue({
            firstName: userData.firstName,
            lastName: userData.lastName,
            institutionName: userData.workLocation,
            position:userData.position,
            division:userData.department,
            email:userData.email
         });
       },
       error:(error) => {
         console.error('Error fetching user data:', error);
       }
      }
        
      );
    }
  }



  onSubmit() {
    this.submitted = true;
  
    // Stop here if the form is invalid
    if (this.registerForm.invalid) {
      return;
    }
  
    this.loading = true;
  
    // User Add logic
    this.userService.add(this.registerForm.value).subscribe({
      next: (response: any) => {
        // Check if the response contains the success message
        if (response && response.message) {
          Swal.fire({
            icon: 'success',
            title: 'Registration Successful',
            text: response.message, // Display the success message from the response
          }).then(() => {
            window.location.href = '/login'; // Redirect to login page after success
          });
        } else {
          // Handle unexpected response
          console.error('Unexpected response:', response);
          Swal.fire('Error', 'Unexpected Error!', 'error');
        }
      },
      error: (error) => {
        console.error(error);
        this.loading = false;
        Swal.fire('Error', 'Failed to register!', 'error');
      },
    });
  }
  
  

  onReset() {
      this.submitted = false;
      this.registerForm.reset();
  }


  
}
export function MustMatch(controlName: string, matchingControlName: string): any {
  return (group: AbstractControl) => {
    const control = group.get(controlName);
    const matchingControl = group.get(matchingControlName);

    if (!control || !matchingControl) {
        return null;
    }

    // return if another validator has already found an error on the matchingControl
    if (matchingControl.errors && !matchingControl.errors['mustMatch']) {
        return null;
    }

    // set error on matchingControl if validation fails
    if (control.value !== matchingControl.value) {
        matchingControl.setErrors({ mustMatch: true });
    } else {
        matchingControl.setErrors(null);
    }
    return null;
}


}

