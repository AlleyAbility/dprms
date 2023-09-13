import { Component } from '@angular/core';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent {

  status: boolean = false;
  clickStatusEvent(){
      this.status = !this.status;       
  }

  constructor(private auth: AuthService) {}

}
