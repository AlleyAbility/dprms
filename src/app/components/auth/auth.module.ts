import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";
import { LoginComponent } from "./login/login.component";
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';

@NgModule({
 declarations:[LoginComponent],
 imports:[
    CommonModule,
    MatButtonModule,
    MatInputModule,
    MatIconModule,
    RouterModule.forChild([{ path: '', component: LoginComponent }]),
],
 exports:[]
})
export class AuthModule{}