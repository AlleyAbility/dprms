import { HttpEventType, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ProjectService } from '../project/project.service';
import Swal from 'sweetalert2';
import { AuthService } from 'src/app/services/auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-projects',
  templateUrl: './projects.component.html',
  styleUrls: ['./projects.component.scss']
})
export class ProjectsComponent implements OnInit{

  projectForm!:FormGroup
  checkListForm!:FormGroup
  // conceptFiles?: FileList;
  // torFiles?: FileList;
  selectedFiles?: FileList;
  currentFile1?: File;
  currentFile2?: File;
  progress1 = 0;
  progress2 = 0;
  message = '';
  documentTitle= '';
  projectId!: number;

  hide = true;
  isLinear = false;
  
  fileName1 = 'Upload Concept Note';
  fileName2 = 'Upload TOR';
  isProjectAdded = false;
  isDocumentUploaded = false;
  isCompleted = false;

  constructor(private _formBuilder: FormBuilder, private projectService: ProjectService ,private authService: AuthService, private router: Router) {}

  userId = this.authService.getUserId();

  ngOnInit(): void {
    this.projectForm = this._formBuilder.group({
      projectName:new FormControl('',[Validators.required]),
      projectManager:new FormControl('',[Validators.required]),
      projectVendor:new FormControl('',[Validators.required]),
      institutionName:new FormControl('',[Validators.required]),
      projectSponsor:new FormControl('',[Validators.required]),
      createdAt:new FormControl(''),
      status:new FormControl('',[Validators.required])
    })
    this.checkListForm = this._formBuilder.group({
      budgetIncluded: new FormControl('', Validators.required),
      sponsorIncluded: new FormControl('', Validators.required),
      outcomes: new FormControl('', Validators.required),
      activities: new FormControl('', Validators.required),
      vision: new FormControl('', Validators.required),
      approved: new FormControl('', Validators.required),
      qualifications: new FormControl('', Validators.required),
      software: new FormControl('', Validators.required),
      role:  new FormControl('', Validators.required),
      contact:  new FormControl('', Validators.required),
      gantt:  new FormControl('', Validators.required),
      policies:  new FormControl('', Validators.required)
    })
  }

  onClick(){

    if(this.projectId != null && this.checkListForm.valid){
      let marks = 0
      if (this.checkListForm.get('budgetIncluded')?.value === 'Yes') {
        marks = marks + 5;
      }else if(this.checkListForm.get('sponsorIncluded')?.value === 'Yes'){
        marks = marks + 5;
      }else if(this.checkListForm.get('outcomes')?.value === 'Yes'){
        marks = marks + 5;
      }else if(this.checkListForm.get('vision')?.value === 'Yes'){
        marks = marks + 5;
      }else if(this.checkListForm.get('activities')?.value === 'Yes'){
        marks = marks + 5;
      }else if(this.checkListForm.get('approved')?.value === 'Yes'){
        marks = marks + 5;
      }else if(this.checkListForm.get('qualifications')?.value === 'Yes'){
        marks = marks + 5;
      }else if(this.checkListForm.get('policies')?.value === 'Yes'){
        marks = marks + 5;
      }else if(this.checkListForm.get('gantt')?.value === 'Yes'){
        marks = marks + 5;
      }else if(this.checkListForm.get('software')?.value === 'Yes'){
        marks = marks + 5;
      }else if(this.checkListForm.get('contact')?.value === 'Yes'){
        marks = marks + 5;
      }else if(this.checkListForm.get('role')?.value === 'Yes'){
        marks = marks + 5;
      }
  
      if(marks >= 40){
        Swal.fire('Success', 'You passed the checklist!', 'success');
        this.isDocumentUploaded = true;
      }else if(marks < 40){
        Swal.fire('Error', 'You did not pass the checklist, Please correct your documents!', 'error');
        this.isDocumentUploaded = false;
      }
    }
   
  }
  
  submitForm() {
    if (this.checkListForm.valid) {
      // console.log(this.userId);
      const projectData = {
        ...this.checkListForm.value,
        userId: this.userId // Add userId to the project data
      };
  
      this.projectService.add(projectData).subscribe({
        next: (response: any) => {
          // console.log(response);
          Swal.fire('Success', 'Project added successfully!', 'success');
          this.isProjectAdded = true; // Set isProjectAdded to true
          this.projectId = response.id;

          this.projectService.assign(this.userId, this.projectId).subscribe({
            next: (response: any)=> {
              console.log(response)
            },
            error: (error: any) => {
              console.error(error);
              
            }
          })
         
        },
        error: (error) => {
          console.error(error);
          if (error.status === 409) {
            Swal.fire('Error', 'A project with the same name already exists in the institution!', 'error');
          } else {
            Swal.fire('Error', 'Error occurred!', 'error');
          }
        }
      });
    }
  }
  


  selectFile1(event1: any): void {
    if (event1.target.files && event1.target.files[0]) {
      const file1: File = event1.target.files[0];
      this.currentFile1 = file1;
      this.fileName1 = this.currentFile1.name;
      this.documentTitle = "CONCEPT NOTE"
    } else {
      this.fileName1 = 'Select File';
    }
  }


  upload1(): void {
    this.progress1 = 0;
   
    if (this.currentFile1 && this.projectId != null) {

        this.projectService.upload(this.currentFile1, this.projectId, this.documentTitle).subscribe({
          next: (event: any) => {
            this.isCompleted = true;
            if (event.type === HttpEventType.UploadProgress) {
              this.progress1 = Math.round(100 * event.loaded / event.total);
              
            } else if (event instanceof HttpResponse) {
              this.message = event.body.message;
              // this.fileInfos = this.uploadService.getFiles();
              Swal.fire('Success', 'Concept Note uploaded!', 'success');
             
            }
          },
          error: (err: any) => {
            console.log(err);
            this.progress1 = 0;
            Swal.fire('Error', 'Unexpected Error!', 'error');

            // if (err.error && err.error.message) {
            //   this.message = err.error.message;
            // } else {
            //   this.message = 'Could not upload the file!';
            // }

            this.currentFile1 = undefined;
          }
        });
      }
    }

    selectFile2(event2: any): void {
      if (event2.target.files && event2.target.files[0]) {
        const file2: File = event2.target.files[0];
        this.currentFile2 = file2;
        this.fileName2 = this.currentFile2.name;
        this.documentTitle = "TOR"
      } else {
        this.fileName2 = 'Select File';
      }
    }

  upload2(): void {
    this.progress2 = 0;
   
    if (this.currentFile2 && this.projectId != null) {

        this.projectService.upload(this.currentFile2, this.projectId, this.documentTitle).subscribe({
          next: (event2: any) => {
            this.isCompleted = true;
            if (event2.type === HttpEventType.UploadProgress) {
              this.progress2 = Math.round(100 * event2.loaded / event2.total);
            } else if (event2 instanceof HttpResponse) {
              this.message = event2.body.message;
              // this.fileInfos = this.uploadService.getFiles();
              Swal.fire('Success', 'TOR uploaded!', 'success');
            }
          },
          error: (err: any) => {
            console.log(err);
            this.progress2 = 0;
            this.isCompleted = false;
            Swal.fire('Error', 'Unexpected Error!', 'error');

            // if (err.error && err.error.message) {
            //   this.message = err.error.message;
            // } else {
            //   this.message = 'Could not upload the file!';
            // }

            this.currentFile2 = undefined;
          }
        });
      }
    }
 
  onConfirm(){
    if(this.isDocumentUploaded && this.isProjectAdded && this.isCompleted){
        Swal.fire('Success', 'Your project successfully registered, Wait for further notice.', 'success')
        this.router.navigate(['/admin/projects']);
    }else{
      Swal.fire('Error', 'Project is not completed!', 'error')
    }
  }
}
