import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProjectService } from '../projects/project/project.service';
import { HttpClient } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-services',
  templateUrl: './services.component.html',
  styleUrls: ['./services.component.scss']
})
export class ServicesComponent implements OnInit {

  projectId!: number; // Define a property to store the project ID
  projId!: number;

  projectData: any = {}; // Change the type to an object

  selectedProject!: number;
  selectedDocumentTitle!: string;
  // selectedDocumentUrl!: string; // To store the URL of the selected document
  selectedDocumentUrl: SafeResourceUrl | undefined;

  institutionName!:string;

  notificationForm!: FormGroup;
  

  constructor(private route: ActivatedRoute,private modalService: NgbModal,
     private projectService: ProjectService, private http: HttpClient, private sanitizer: DomSanitizer) { }

  ngOnInit(): void {

    this.formConfiguration()
    // Read the project ID from the route parameter
    this.route.params.subscribe(params => {
      this.projectId = +params['projectId'];
    });

    // Fetch project data using projectId
    this.fetchProjectData(this.projectId);
  }



  formConfiguration(){
    this.notificationForm = new FormGroup({
      type:new FormControl('',[Validators.required]),
      description:new FormControl('',[Validators.required]),
      institutionName:new FormControl('',[Validators.required]),
    })
  }

  fetchProjectData(projectId: number): void {
    this.projectService.getById(projectId).subscribe({
      next: (data) => {
        data.createdAt = this.formatTimestamp(data.createdAt);
        this.projectData = data; // Assign project data to your component property
        this.institutionName = data.institutionName;
        this.projId = projectId;
      },
      error: (error) => {
        console.error('Error fetching project data:', error);
        // Handle error cases, e.g., display an error message
      }
    }
    );
  }

   // Function to format a timestamp as a date string
   private formatTimestamp(timestamp: number): string {
    const date = new Date(timestamp);
    // You can customize the date format as needed
    return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
  }

  loadDocuments(selectedDocumentTitle: string) {
    // Make an HTTP request to your backend API to get the document URL
    this.projectService.getDocument(this.projectId, selectedDocumentTitle).subscribe({
        next: (data) => {
            if (data && data.filePath) {

                // Define the base URL
                const baseUrl = 'http://localhost:8080/api/v1/documents/';
                const newurl = baseUrl + data.filePath;

                 // Sanitize the document URL before assigning it
                 this.selectedDocumentUrl = this.sanitizer.bypassSecurityTrustResourceUrl(baseUrl + data.filePath);

                  // Open the document in a new tab or window
                window.open(newurl, '_blank');
                // this.selectedDocumentUrl = data.filePath;
                // console.log( this.selectedDocumentUrl)
            }
        },
        error: (error) => {
            console.error('Error fetching document data:', error);
            // Handle error cases, e.g., display an error message
        }
    });
}



addNotification() {
  if (this.notificationForm.valid) {
    // Create the notification object by merging data from both sources
    const notificationData = {
      type: this.notificationForm.value.type,
      description: this.notificationForm.value.description,
      institutionName: this.notificationForm.value.institutionName,
      status:'unread',
      project: {
        id: this.projectData.id,
        projectName: this.projectData.projectName,
        projectManager: this.projectData.projectManager,
        projectVendor: this.projectData.projectVendor,
        projectSponsor: this.projectData.projectSponsor,
        institutionName: this.projectData.institutionName,
        // createdAt: this.projectData.createdAt,
        status: this.projectData.status
      }
    };

    // const notificationData = {
    //   type: "APPROVAL NOTIFICATION",
    //   description: "The project is allowed to start development.",
    //   institutionName: "EGAZ",
    //   project: {
    //     id: 1,
    //     projectName: "DPRMS",
    //     projectManager: "Abdulhamid Aley",
    //     projectVendor: "EGAZ",
    //     projectSponsor: "EGAZ",
    //     institutionName: "EGAZ",
    //     createdAt: 1693636350000,
    //     status: "new"
    //   }
    // };
    

    // Now you can send the notificationData to your API
    this.projectService.addNotification(notificationData).subscribe({
      next: (response: any[]) => {
        console.log(response);
        Swal.fire('Success', 'Notification added successfully!', 'success');
      },
      error: (error) => {
        console.error(error);
      },
    });
  }
}

}


