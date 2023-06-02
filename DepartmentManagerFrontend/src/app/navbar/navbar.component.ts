import { Component, ElementRef, Optional } from '@angular/core';
import { User } from '../user';
import { LoginService } from '../login.service';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { UserServiceService } from '../user-service.service';
import { DirectorateServiceService } from '../directorate-service.service';
import { DirectorateComponent } from '../directorate/directorate.component';
import { DepartmentComponent } from '../department/department.component';
import { UserComponent } from '../user/user.component';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
  user: any = localStorage.getItem('user');
  page: any;

  constructor(private router: Router, private loginService: LoginService,
     private _elementRef: ElementRef, private userService: UserServiceService,
     private route: ActivatedRoute,
     @Optional() private directorateComponent: DirectorateComponent,
     @Optional() private departmentComponent: DepartmentComponent,
     @Optional() private userComponent: UserComponent) {
      switch (this.router.url.split('/').length) {
        case 2:
          this.page = 'Directorates';
          break;
        case 3:
          this.page = `${this.route.snapshot.paramMap.get('name')}'s departments`;
          break;
        case 5:
          this.page = `${this.route.snapshot.paramMap.get('name')}'s employees`;
          break;
      }
  }

  search(term: string, length: number) { 
    switch (this.router.url.split('/').length) {
      case 2:
        this.directorateComponent.search(term, length);
        break;
      case 3:
        this.departmentComponent.search(term, length);
        break;
      case 5:
        this.userComponent.search(term, length);
        break;
    }
  }

  clearSearch(search: any) {
    search.value = '';
  }

  checkRoute(){
    if (localStorage.getItem('user') === 'admin') return true;
    else return false;
  }

  logout() {
    this.loginService.deleteToken();
    this.router.navigate(['/login']);
  }

  unblock() {
    this.userService.unblock().subscribe(result => this.router.navigate(['/']));
  }

  togglePanel(name: string) {
    let panel: any = this._elementRef.nativeElement.querySelector(`.navbar-panel--${name}`);
    let panels: any[] = this._elementRef.nativeElement.querySelectorAll('.navbar-panel');
    if (!panel.classList.contains(`navbar-${name}--active`)){
      panels.forEach(panel => {
        panel.classList.remove(panel.classList[2]);
      })
      panel.classList.add(`navbar-${name}--active`);
    } else {
      panel.classList.remove(`navbar-${name}--active`);
      panel.classList.add(`navbar-${name}--inactive`);
    }
  }
}
