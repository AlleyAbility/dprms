import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule} from '@angular/material/input';
import { MatFormField } from '@angular/material/form-field';
import { MatDividerModule } from '@angular/material/divider'; 
import { MatStepperModule } from '@angular/material/stepper';
import { NgIf } from '@angular/common';
import { MatCardModule} from '@angular/material/card';
import { MatChipsModule} from '@angular/material/chips';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatList, MatListModule } from '@angular/material/list';
import { MatSelectModule} from '@angular/material/select';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http'
import { DataTablesModule } from 'angular-datatables';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
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
    FontAwesomeModule

  ]
})
export class UserModule { }
