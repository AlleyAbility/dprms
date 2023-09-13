import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminRoutingModule } from './admin-routing.module';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { HomeComponent } from './components/home/home.component';
import { ContactComponent } from './components/contact/contact.component';
import { ServicesComponent } from './components/services/services.component';
import { AboutComponent } from './components/about/about.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';



import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import { MatDividerModule } from '@angular/material/divider'; 
import { MatStepperModule } from '@angular/material/stepper';
import {NgIf} from '@angular/common';
import {MatCardModule} from '@angular/material/card';
import {MatChipsModule} from '@angular/material/chips';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatList, MatListModule } from '@angular/material/list';
import {  MatSelectModule} from '@angular/material/select';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http'
import { DataTablesModule } from 'angular-datatables';
import { UserComponent } from './components/users/user/user.component';
import { ProjectsComponent } from './components/projects/projects/projects.component';
import { ProjectViewComponent } from './components/projects/project/project-view/project-view.component';

@NgModule({
  declarations: [AdminDashboardComponent, 
    HeaderComponent, 
    FooterComponent, 
    HomeComponent, 
    ContactComponent, 
    ServicesComponent, 
    AboutComponent, 
    SidebarComponent,
    UserComponent,
    ProjectsComponent,
    ProjectViewComponent],
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
    FontAwesomeModule],
})
export class AdminModule {}
