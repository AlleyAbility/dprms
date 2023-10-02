import { AfterViewInit, Component, ElementRef, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { NgbModalConfig, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ProjectService } from '../project.service';
import Swal from 'sweetalert2';
import { Subject } from 'rxjs';

declare var $: any; // Import jQuery for modal operations

@Component({
  selector: 'app-project-view',
  templateUrl: './project-view.component.html',
  styleUrls: ['./project-view.component.scss']
})
export class ProjectViewComponent implements OnInit {

  projectForm!: FormGroup
  projectList: any[] = [];
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  loading = false;



  currentPage = 1;
  itemsPerPage = 8; // Number of items to show per page

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
      pagingType: 'simple_numbers',
      pageLength: 7,
      processing: true,
      responsive: true
    };
  }



  formConfiguration() {
    this.projectForm = new FormGroup({
      projectName: new FormControl('', [Validators.required]),
      projectManager: new FormControl('', [Validators.required]),
      projectVendor: new FormControl('', [Validators.required]),
      institutionName: new FormControl('', [Validators.required]),
      projectSponsor: new FormControl('', [Validators.required]),
      createdAt: new FormControl(''),
      status: new FormControl('', [Validators.required])
    })
  }

  addProject() {
    // console.log(this.userForm.value)
    if (this.projectForm.valid) {

      this.projectService.add(this.projectForm.value).subscribe({
        next: (response: any[]) => {
          // console.log(response)
          Swal.fire('Success', 'Data added successfully!', 'success');
          this.getProject()
        },
        error: (error) => {
          console.error(error);
        }
      })
    }

  }

  getProject() {
    this.projectService.get().subscribe({
      next: (response) => {
        // response.createdAt = this.formatTimestamp(response.createdAt);
        this.projectList = response
        this.dtTrigger.next(null);
        // console.log(response)
      },
      error: (error) => {
        console.log(error)
      }
    })
  }


  editProject(id: number, updatedData: any) {
    this.projectService.update(id, updatedData).subscribe({
      next: (response: any) => {
        // Handle a successful update here
        this.getProject()
        this.successNotification()
        // console.log('User updated:', response);
      },
      error: (error: any) => {
        // Handle errors here
        console.error('Error updating user:', error);
      }
    }
    );
  }


  // Function to format a timestamp as a date string
  formatTimestamp(timestamp: number): string {
    const date = new Date(timestamp);
    // You can customize the date format as needed
    return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
  }


















  // Calculate the start and end index for the current page
  get startIndex() {
    return (this.currentPage - 1) * this.itemsPerPage;
  }

  get endIndex() {
    return this.currentPage * this.itemsPerPage;
  }

  // Get the projects for the current page
  get pagedProjects() {
    return this.projectList.slice(this.startIndex, this.endIndex);
  }

 // Function to open the report generation modal
 openReportModal() {
  $('#reportModal').modal('show'); // Open the modal
}

// Function to close the report generation modal
closeReportModal() {
  $('#reportModal').modal('hide'); // Close the modal
}

// Function to get the count of working projects
getWorkingProjectsCount() {
  return this.projectList.filter((project) => project.status === 'In use').length;
}

// Function to get the count of not working projects
getNotWorkingProjectsCount() {
  return this.projectList.filter((project) => project.status === 'Not Working').length;
}

getNewProjectsCount() {
  return this.projectList.filter((project) => project.status === 'New').length;
}

getDevelopingProjectsCount() {
  return this.projectList.filter((project) => project.status === 'Developing').length;
}




  selectedInstitutionName = '';
  showInstitutionReport = false;
  workingProjectsCount: number = 0;
  notWorkingProjectsCount: number = 0;
  newProjectsCount : number = 0;
  developingProjectCounts: number = 0;
  selectedStartDate: string = ''; // Declare selectedStartDate
  selectedEndDate: string = ''; // Declare selectedEndDate
  selectedProjectStatus: string = 'All'; // Declare and initialize selectedProjectStatus


modalPage: number = 1;


calculateProjectCounts() {
  const institutionName = this.selectedInstitutionName;
  // Filter projects for the selected institution
  this.selectedInstitutionProjects = this.projectList.filter((project) => project.institutionName === institutionName);
  
  // Calculate counts
  this.workingProjectsCount = this.selectedInstitutionProjects.filter((project) => project.status === 'In use').length;
  this.notWorkingProjectsCount = this.selectedInstitutionProjects.filter((project) => project.status === 'Not Working').length;
  this.newProjectsCount = this.selectedInstitutionProjects.filter((project) => project.status === 'New').length;
  this.developingProjectCounts = this.selectedInstitutionProjects.filter((project) => project.status === 'Developing').length;
}



/// Function to generate the institution-specific report
generateInstitutionReport() {
  // Show the report section
  this.showInstitutionReport = true;
}

// Function to get the count of projects for a particular institution
getProjectsCountForInstitution(institutionName: string) {
  return this.projectList.filter((project) => project.institutionName === institutionName).length;
}

selectedInstitutionProjects: any[] = [];



// Variables to control the Status Report card
showStatusReportCard = false;
showStatusReport = false;



// Total count of projects with the selected status
totalStatusProjectsCount = 0;

// Function to open the Status Report card
openStatusReportCard() {
  this.showStatusReportCard = true;
  this.showStatusReport = false; // Hide previous report if open
}

// Function to close the Status Report card
closeStatusReportCard() {
  this.showStatusReportCard = false;
}

// Function to generate the Status Report
generateStatusReport() {
  // Filter projects based on the selected project status
  const filteredProjects = this.projectList.filter(
    (project) => this.selectedProjectStatus === 'All' || project.status === this.selectedProjectStatus
  );

  this.totalStatusProjectsCount = filteredProjects.length;
  this.showStatusReport = true;
  this.showStatusReportCard = false;
}






// Add the filteredProjects array
filteredProjects: any[] = [];

// Function to update filteredProjects based on the selected project status
updateFilteredProjects() {
  if (this.selectedProjectStatus === 'All') {
    // Display all projects if "All" is selected
    this.filteredProjects = this.projectList;
  } else {
    // Filter projects based on selected status
    this.filteredProjects = this.projectList.filter((project) => project.projectStatus === this.selectedProjectStatus);
  }

  // Calculate the total count of projects with the selected status
  this.totalStatusProjectsCount = this.filteredProjects.length;
}


}
