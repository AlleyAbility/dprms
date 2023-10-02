import { AfterViewInit, Component, ElementRef, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../user.service';
import { response } from 'express';
import Swal from 'sweetalert2';
import { DataTableDirective } from 'angular-datatables';
import { Subject } from 'rxjs';
// import DataTable from 'datatables.net-dt';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {
  userForm!:FormGroup
  userList:any[] = [];
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();


  constructor(private userService:UserService){  }

 
  ngOnInit(): void {
    this.formConfiguration()
    this.getUser()
    this.dtOptions = {
      pagingType: 'simple_numbers',
      pageLength: 7,
      processing: true, 
      responsive: true
    };
  }


formConfiguration(){
  this.userForm = new FormGroup({
    firstName:new FormControl('',[Validators.required]),
    lastName:new FormControl('',[Validators.required]),
    email:new FormControl('',[Validators.email]),
    institutionName:new FormControl('',[Validators.required]),
    position:new FormControl('',[Validators.required]),
    division:new FormControl('',[Validators.required]),
    employeeId:new FormControl('',[Validators.required]),
    phone:new FormControl('',[Validators.required]),
    password:new FormControl('',[Validators.required]),
    cpassword:new FormControl('',[Validators.required])
  })
}


// tinyAlert() {
//   Swal.fire('Hey there!');
// }
successNotification() {
  Swal.fire('Success', 'Data updated successfully!', 'success');
}
errorNotification() {
  Swal.fire('Error', 'Data not deleted successfully!', 'error');
}
alertConfirmation() {
  Swal.fire({
    title: 'Are you sure?',
    text: 'This process is irreversible.',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: 'Yes, go ahead.',
    cancelButtonText: 'No, let me think',
  }).then((result) => {
    if (result.value) {

      Swal.fire('Removed!', 'Data removed successfully.', 'success');
    } else if (result.dismiss === Swal.DismissReason.cancel) {
      Swal.fire('Cancelled', 'Data still in our database.', 'error');
    }
  });
}


addUser(){
  // console.log(this.userForm.value)
  if(this.userForm.valid){
    this.userService.add(this.userForm.value).subscribe({
      next:(response : any[])=>{
        console.log(response)
        this.getUser()
      }, 
      error: (error) => {
        console.error(error);
      }
    })
  }

}

getUser(){
  this.userService.get().subscribe({
    next:(response)=>{
      this.userList = response
      this.dtTrigger.next(null);
      // console.log(response)
    },
    error:(error)=>{
      console.log(error)
    }
  })
}


editUser(id:number, updatedData: any){
  this.userService.update(id, updatedData).subscribe({
    next:(response) => {
      // Handle a successful update here
      this.getUser()
      this.successNotification()
      console.log('User updated:', response);
    },
    error:(error) => {
      // Handle errors here
      console.error('Error updating user:', error);
    }
  }
  );
}
 

deleteUser(email:string){
  Swal.fire({
    title: 'Are you sure?',
    text: 'This process is irreversible.',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: 'Yes, go ahead.',
    cancelButtonText: 'No, let me think',
  }).then((result) => {
    if (result.value) {

      this.userService.delete(email).subscribe(
        {
          next: (response) => {
            this.getUser();
          },
          error: (errorResponse) => {
            this.errorNotification();
            console.error('Error deleting data:', errorResponse.error);
            Swal.fire('Error', 'Something happened.)', 'error');
          }      
      })
      
      Swal.fire('Removed!', 'Data removed successfully.', 'success');
    } else if (result.dismiss === Swal.DismissReason.cancel) {
      Swal.fire('Cancelled', 'Data still in our database.)', 'error');
    }
  });
}

updateLockStatus(id:number, lock:boolean){

  Swal.fire({
    title: 'Are you sure?',
    text: 'This process is irreversible.',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: 'Yes, go ahead.',
    cancelButtonText: 'No, let me think',
  }).then((result) => {
    if (result.value) {

      this.userService.updateLockStatus(id, lock).subscribe(
        {
          next: (response) => {
            this.getUser();
          },
          error: (errorResponse) => {
            this.errorNotification();
            console.error('Error updating lock status:', errorResponse.error);
            Swal.fire('Error', 'Something happened.)', 'error');
          }      
      })
      
      Swal.fire('Removed!', 'Data removed successfully.', 'success');
    } else if (result.dismiss === Swal.DismissReason.cancel) {
      Swal.fire('Cancelled', 'Data still in our database.)', 'error');
    }
  });
  
}
}
