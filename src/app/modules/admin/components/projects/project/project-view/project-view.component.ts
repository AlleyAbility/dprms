import { AfterViewInit, Component, ElementRef, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { NgbModalConfig, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ProjectService } from '../project.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-project-view',
  templateUrl: './project-view.component.html',
  styleUrls: ['./project-view.component.scss']
})
export class ProjectViewComponent implements OnInit, AfterViewInit {
  
  projectForm!:FormGroup
  projectList:any[] = [];
  dtOptions: DataTables.Settings = {};

  constructor(config: NgbModalConfig, private modalService: NgbModal, private projectService: ProjectService, private el: ElementRef) {
		// customize default values of modals used by this component tree
		config.backdrop = 'static';
		config.keyboard = false;

	}

  successNotification() {
    Swal.fire('Success', 'Data updated successfully!', 'success');
  }

	open(content: any) {
		this.modalService.open(content);
	}

  ngOnInit(): void {
    this.formConfiguration()
    this.getProject()
    this.dtOptions = {
      pagingType: 'full_numbers',
      pageLength: 5,
      processing: true
    };
  }

  ngAfterViewInit(): void {
    // Initialize the DataTable
    $(this.el.nativeElement.querySelector('table')).DataTable();
  }


  formConfiguration(){
    this.projectForm = new FormGroup({
      projectName:new FormControl('',[Validators.required]),
      projectManager:new FormControl('',[Validators.required]),
      projectVendor:new FormControl('',[Validators.required]),
      institutionName:new FormControl('',[Validators.required]),
      projectSponsor:new FormControl('',[Validators.required]),
      createdAt:new FormControl(''),
      status:new FormControl('',[Validators.required])
    })
  }

  addProject(){
    // console.log(this.userForm.value)
    if(this.projectForm.valid){
      this.projectService.add(this.projectForm.value).subscribe({
        next:(response : any[])=>{
          console.log(response)
          Swal.fire('Success', 'Data added successfully!', 'success');
          this.getProject()
        }, 
        error: (error) => {
          console.error(error);
        }
      })
    }
  
  }

  getProject(){
    this.projectService.get().subscribe({
      next:(response)=>{
        this.projectList = response
        console.log(response)
      },
      error:(error)=>{
        console.log(error)
      }
    })
  }


  editProject(id:number, updatedData: any){
    this.projectService.update(id, updatedData).subscribe({
      next:(response: any) => {
        // Handle a successful update here
        this.getProject()
        this.successNotification()
        console.log('User updated:', response);
      },
      error:(error: any) => {
        // Handle errors here
        console.error('Error updating user:', error);
      }
    }
    );
  }

}
