import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { UserComponent } from './components/users/user/user.component';
import { LoginComponent } from './components/login/login.component';
import { ProjectsComponent } from './components/projects/projects/projects.component';
import { LayoutComponent } from './components/layout/layout/layout.component';

const routes: Routes = [
    {path: 'login', component: LoginComponent},
    {path: 'dashboard', component: DashboardComponent},
    {path: 'users', component: UserComponent},
    {path: 'projects', component:ProjectsComponent},
    {path: 'layout', component:LayoutComponent},
    {path: '', redirectTo: 'login', pathMatch: 'full'},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
