import { HttpClient } from '@angular/common/http';
import { Component, ElementRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LoginService } from './login.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'my_app';
  
  constructor(private http: HttpClient, private _elementRef: ElementRef,
    private route: ActivatedRoute,
    private router: Router, private loginService: LoginService) {
  }
}
