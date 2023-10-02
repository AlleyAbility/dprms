import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminRoutingModule } from './admin-routing.module';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';
import { HomeComponent } from './components/home/home.component';
import { ServicesComponent } from './components/services/services.component';
import { UserComponent } from './components/users/user/user.component';
import { ProjectsComponent } from './components/projects/projects/projects.component';
import { ProjectViewComponent } from './components/projects/project/project-view/project-view.component';


import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule} from '@angular/material/input';
import { MatDividerModule } from '@angular/material/divider'; 
import { MatStepperModule } from '@angular/material/stepper';
import { NgIf} from '@angular/common';
import { MatCardModule} from '@angular/material/card';
import { MatChipsModule} from '@angular/material/chips';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatList, MatListModule } from '@angular/material/list';
import { MatSelectModule} from '@angular/material/select';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http'
import { DataTablesModule } from 'angular-datatables';
import { DocumentsComponent } from './components/documents/documents.component';
import { NotificationsComponent } from './components/notifications/notifications.component';
import { RolesComponent } from './components/roles/roles.component';
import { ProfileComponent } from './components/profile/profile.component';
import { PaginationModule } from 'ngx-bootstrap/pagination';
import { NgxPaginationModule } from 'ngx-pagination';


@NgModule({
  declarations: [
    AdminDashboardComponent, 
    HomeComponent,  
    ServicesComponent, 
    UserComponent,
    ProjectsComponent,
    ProjectViewComponent,
    DocumentsComponent,
    NotificationsComponent,
    RolesComponent,
    ProfileComponent,],

  imports: [
    CommonModule,
    AdminRoutingModule,
    ReactiveFormsModule,
    DataTablesModule,
    HttpClientModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatDividerModule,
    MatStepperModule,
    NgIf,
    MatCardModule,
    MatChipsModule,
    MatListModule,
    MatToolbarModule,
    MatFormFieldModule,
    MatProgressBarModule,
    MatSelectModule,
    NgbModule,
    FontAwesomeModule, 
    FormsModule,
    PaginationModule,
  NgxPaginationModule ],
})
export class AdminModule {}
