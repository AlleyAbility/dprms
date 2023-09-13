import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { ForgotPasswordComponent } from './components/forgot-password/forgot-password.component';
import { authGuard } from './guards/auth.guard';
import { RegisterComponent } from './components/register/register.component';
import { VerifyEmailComponent } from './components/verify-email/verify-email.component';

const routes: Routes = [
    {path: 'login', component: LoginComponent},
    {path: 'register', component:RegisterComponent},
    {path: 'users/verifyEmail', component:VerifyEmailComponent},
    {path: 'forgot-password', component:ForgotPasswordComponent},
    {
      path: 'admin',
      canActivate: [authGuard],
      loadChildren: () =>
        import('./modules/admin/admin.module').then((m) => m.AdminModule),
    },
    {path: '', redirectTo: 'login', pathMatch: 'full'},
    {path: '**', component:NotFoundComponent},
    
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
