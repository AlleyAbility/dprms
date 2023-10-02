import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { ServicesComponent } from './components/services/services.component';
import { UserComponent } from './components/users/user/user.component';
import { ProjectsComponent } from './components/projects/projects/projects.component';
import { ProjectViewComponent } from './components/projects/project/project-view/project-view.component';
import { authGuard } from '../../guards/auth.guard'
import { NotificationsComponent } from './components/notifications/notifications.component';
import { RolesComponent } from './components/roles/roles.component';
import { ProfileComponent } from './components/profile/profile.component';

const routes: Routes = [
  {
    path: '', component: AdminDashboardComponent,
    children: [
      { path: 'home', component: HomeComponent ,
      canActivate: [authGuard], // Use the authGuard
      data: { roles: ['ROLE_ADMIN'] }}, // Specify required roles},
      { path: 'home', component: HomeComponent },
      { path: 'users', component: UserComponent },
      { path: 'services/:projectId', component: ServicesComponent },
      { path: 'projects', component: ProjectViewComponent },
      { path: 'project-add', component: ProjectsComponent},
      { path: 'notifications', component: NotificationsComponent},
      { path: 'roles', component: RolesComponent},
      { path: 'profile', component: ProfileComponent},
      { path: '', redirectTo: '/admin/home', pathMatch: 'full' },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AdminRoutingModule {}
