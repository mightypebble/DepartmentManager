import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DepartmentComponent } from './department/department.component';
import { DirectorateComponent } from './directorate/directorate.component';
import { UserComponent } from './user/user.component';
import { LoginComponent } from './login/login.component';
import { Error401Component } from './error403/error403.component';

const routes: Routes = [
  { path: '', component: DirectorateComponent },
  { path: 'directorate/:name', component: DepartmentComponent },
  { path: 'directorate/:dir/department/:name', component: UserComponent },
  { path: 'login', component: LoginComponent },
  { path: '403', component: Error401Component},
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { onSameUrlNavigation: 'reload' })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
