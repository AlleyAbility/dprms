import { Component, OnInit } from '@angular/core';

import { Router } from 'express';
import { UserService } from '../users/user.service';
import { ProjectService } from '../projects/project/project.service';

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.scss']
})
export class NotificationsComponent {

  notificationList: any[] = [];
  
  constructor(private projectService: ProjectService) { }
  
  ngOnInit(): void {
    this.getNotifications()
  }

    getNotifications() {
      this.projectService.getNotifications().subscribe({
        next: (response) => {
          this.notificationList = response
          // console.log(response)
        },
        error: (error) => {
          console.log(error)
        }
      })
    }

   // Function to format a timestamp as a date string
   formatTimestamp(timestamp: number): string {
    const date = new Date(timestamp);
    // You can customize the date format as needed
    return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
  }
}
