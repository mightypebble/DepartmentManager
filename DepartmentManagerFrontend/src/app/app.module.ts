import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { DirectorateComponent } from './directorate/directorate.component';
import { JwtModule } from "@auth0/angular-jwt";
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { DepartmentComponent } from './department/department.component';
import { UserComponent } from './user/user.component';
import { LoginComponent } from './login/login.component';
import { Error401Component } from './error403/error403.component';
import { JwtInterceptor } from './jwt.interceptor';
import { NavbarComponent } from './navbar/navbar.component';

export function tokenGetter() {
  return localStorage.getItem('access_token');
}

@NgModule({
  declarations: [
    AppComponent,
    DirectorateComponent,
    DepartmentComponent,
    UserComponent,
    LoginComponent,
    Error401Component,
    NavbarComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule,
    JwtModule.forRoot({
      config: {
          tokenGetter: tokenGetter,
          allowedDomains: ['localhost:8080/'],
          authScheme: "Bearer "
      }
  })
  ],
  providers: [{
    provide: HTTP_INTERCEPTORS,
    useClass: JwtInterceptor,
    multi: true,
  },],
  bootstrap: [AppComponent]
})
export class AppModule { }
