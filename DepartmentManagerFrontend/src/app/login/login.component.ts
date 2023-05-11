import { Component, ElementRef, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { User } from '../user';
import { LoginService } from '../login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  user: User;
  status = localStorage.getItem("status");
  authError = false;

  constructor(private route: ActivatedRoute, private router: Router, 
    private http: HttpClient, private loginService: LoginService, private _elementRef : ElementRef) {
      this.user = new User;
   }

  ngOnInit() {
      sessionStorage.setItem('token', '');
  }

  login() {
    this.loginService.login(this.user).subscribe((result: any) => {
      this.loginService.setToken(result.access_token, result.user, result.route, result.status);
      this.router.navigate([this.loginService.getHomeRoute()]);
    },error => {
      if (error.status == 403) {
        this.authError = true;
        this.status = null;
        this._elementRef.nativeElement.querySelector('.login-form').reset();
      }
    });
  }
}
