import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LoginService } from '../login.service';

@Component({
  selector: 'app-error403',
  templateUrl: './error403.component.html',
  styleUrls: ['./error403.component.scss']
})
export class Error401Component {

  constructor(private route: ActivatedRoute, private router: Router, private loginService: LoginService) {
    
  }

  back() {
    this.router.navigate([this.loginService.getHomeRoute()]);
  }

}
