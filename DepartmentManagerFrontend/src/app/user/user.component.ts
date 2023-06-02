import { HttpClient } from '@angular/common/http';
import { Component, ElementRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from '../user';
import { UserServiceService } from '../user-service.service';
import { LoginService } from '../login.service';


@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent {
  name = localStorage.getItem('user');
  backButtonExists: boolean = true;
  users: any;
  user: User;
  department: string;
  directorate!: string;
  filteredUsers: any;
  passwordError = false;
  usernameError = false;
  ucnError = false;
  offset = 0;
  userAreFiltered = false;
  lastTerm = '';
  lastLength = 0;
  hasHead = false;
  userCount: any = 0;
  filteredUserCount: any = 0;

  constructor(private http: HttpClient, private _elementRef: ElementRef,
    private userService: UserServiceService, private route: ActivatedRoute,
    private router: Router, private loginService: LoginService) {
    this.router.routeReuseStrategy.shouldReuseRoute = () => {
      return false;
    };
    this.user = new User;
    this.department = String(this.route.snapshot.paramMap.get('name'));
    this.directorate = String(this.route.snapshot.paramMap.get('dir'));
    if (localStorage.getItem('homeRoute')?.split('/').length == 4) this.backButtonExists = false;
  }

  onSubmit() {
    this.userService.validateUsername(this.user.username).subscribe((data: any) => {
      this.usernameError = data;
      if (!this.usernameError) {
        this.userService.validateUCN(this.user.ucn).subscribe((otherData: any) => {
          this.ucnError = otherData;
          if (!this.ucnError) {
            this.user.department = this.department;
            if (/^(?=(.*[a-z]){1,})(?=(.*[A-Z]){1,})(?=(.*[0-9]){1,})(?=(.*[!@#$%^&*()\-__+.]){1,}).{8,}$/.test(this.user.password)) {
              this.passwordError = false;
              this.userService.save(this.user)
                .subscribe(result => this.router.navigate([`directorate/${this.directorate}/department/${this.department}`]));
            } else this.passwordError = true;
          }
        })
      }
    });
  }

  search(term: string, length: number) {
    if (length >= 1) this.userService.search(term, this.offset, this.department).subscribe(data => {
      this.lastTerm = term;
      this.lastLength = length;
      this.filteredUsers = data;
      this.userService.search(term, this.offset + 10, this.department).subscribe(data => {
        if (!data.length) {
          this._elementRef.nativeElement.querySelector('.arrow-right').style.visibility = 'hidden';
          this._elementRef.nativeElement.querySelector('.arrow-last').style.visibility = 'hidden';
          this._elementRef.nativeElement.querySelector('.last-page-index').style.visibility = 'hidden';
        }
        else {
          this._elementRef.nativeElement.querySelector('.arrow-right').style.visibility = 'visible';
          this._elementRef.nativeElement.querySelector('.arrow-last').style.visibility = 'visible';
          this._elementRef.nativeElement.querySelector('.last-page-index').style.visibility = 'visible';
        }
      })
    })
    else {
      this.filteredUsers = this.users;
      this.ngOnInit();
    }
    if (length >= 1) {
      this.userService.getFilteredUserCount(term, this.department).subscribe((data: any) => {
        this.filteredUserCount = data;
      })
      this.userAreFiltered = true;
    }
    else {
      this.userAreFiltered = false;
      this.filteredUserCount = 0;
    }
    if (this.offset < 10) {
      this._elementRef.nativeElement.querySelector('.arrow-left').style.visibility = 'hidden';
      this._elementRef.nativeElement.querySelector('.arrow-first').style.visibility = 'hidden';
      this._elementRef.nativeElement.querySelector('.first-page-index').style.visibility = 'hidden';
    } else {
      this._elementRef.nativeElement.querySelector('.arrow-left').style.visibility = 'visible';
      this._elementRef.nativeElement.querySelector('.arrow-first').style.visibility = 'visible';
      this._elementRef.nativeElement.querySelector('.first-page-index').style.visibility = 'visible';
    }
  }

  next() {
    this.offset += 10;
    if (!this.userAreFiltered) this.ngOnInit();
    else this.search(this.lastTerm, this.lastLength);
  }

  prev() {
    this.offset -= 10;
    if (!this.userAreFiltered) this.ngOnInit();
    else this.search(this.lastTerm, this.lastLength);
  }

  firstPage() {
    this.offset = 0;
    if (!this.userAreFiltered) this.ngOnInit();
    else this.search(this.lastTerm, this.lastLength);
  }

  lastPage() {
    if (!this.userAreFiltered) {
      if (this.userCount % 10 == 0) this.offset = (Math.floor((this.userCount - 1) / 10) * 10);
      else this.offset = (Math.floor(this.userCount / 10) * 10)
      this.ngOnInit()
    }
    else {
      if (this.filteredUserCount % 10 == 0) this.offset = (Math.floor((this.filteredUserCount - 1) / 10) * 10)
      else this.offset = (Math.floor(this.filteredUserCount / 10) * 10)
      this.search(this.lastTerm, this.lastLength);
    }
  }

  editUser(name: string) { // change user details
    this.userService.edit(name, this.user).subscribe(result => this.router.navigate([`directorate/${this.directorate}/department/${this.department}`]));
  }

  promote(id: number) {
    this.userService.promote(id).subscribe(result => this.router.navigate([`directorate/${this.directorate}/department/${this.department}`]));
  }

  demote(id: number) {
    this.userService.demote(id).subscribe(result => this.router.navigate([`directorate/${this.directorate}/department/${this.department}`]));
  }

  deleteUser(id: number) {
    this.userService.delete(id).subscribe(result => this.router.navigate([`directorate/${this.directorate}/department/${this.department}`]));
  }

  unblockUser(id: number) {
    this.userService.unblockUser(id).subscribe(result => this.router.navigate([`directorate/${this.directorate}/department/${this.department}`]));
  }

  back() {
    this.router.navigate([`directorate/${this.directorate}`]);
  }

  showMenu(user: User) {
    user.shown = !user.shown;
  }

  editForm(user: User) {
    user.editForm = !user.editForm;
  }

  deleteConfirm(user: User) {
    user.deleteConfirm = !user.deleteConfirm;
  }

  openAddForm() {
    this._elementRef.nativeElement.querySelector('.form-button').classList.remove('form-button--inactive');
    this._elementRef.nativeElement.querySelector('.form-button').classList.add('form-button--active');
  }

  ngOnInit() {
    this._elementRef.nativeElement.addEventListener('click', (e: any) => {
      if (!this._elementRef.nativeElement.querySelector('.user-form').contains(e.target) &&
      !this._elementRef.nativeElement.querySelector('.form-button').contains(e.target) &&
      this._elementRef.nativeElement.querySelector('.form-button').classList.contains('form-button--active')) {
        this._elementRef.nativeElement.querySelector('.form-button').classList.remove('form-button--active');
        this._elementRef.nativeElement.querySelector('.form-button').classList.add('form-button--inactive');
      }
    });
    if (!this.userCount) {
      this.userService.getUserCount(this.department).subscribe((data: any) => {
        this.userCount = data;
      })
    }
    this.userService.findAll(this.directorate, this.department, this.offset + 10).subscribe(data => {
      if (!data.length) {
        this._elementRef.nativeElement.querySelector('.arrow-right').style.visibility = 'hidden';
        this._elementRef.nativeElement.querySelector('.arrow-last').style.visibility = 'hidden';
        this._elementRef.nativeElement.querySelector('.last-page-index').style.visibility = 'hidden';
      }
      else {
        this._elementRef.nativeElement.querySelector('.arrow-right').style.visibility = 'visible';
        this._elementRef.nativeElement.querySelector('.arrow-last').style.visibility = 'visible';
        this._elementRef.nativeElement.querySelector('.last-page-index').style.visibility = 'visible';
      }
    })
    this.userService.findAll(this.directorate, this.department, this.offset).subscribe((data) => {
      this.users = data;
      if (this.users.length && this.users[0].position == 'head') this.hasHead = true;
      this.filteredUsers = this.users;
      if (this.offset < 10) {
        this._elementRef.nativeElement.querySelector('.arrow-left').style.visibility = 'hidden';
        this._elementRef.nativeElement.querySelector('.arrow-first').style.visibility = 'hidden';
        this._elementRef.nativeElement.querySelector('.first-page-index').style.visibility = 'hidden';
      }
      else {
        this._elementRef.nativeElement.querySelector('.arrow-left').style.visibility = 'visible';
        this._elementRef.nativeElement.querySelector('.arrow-first').style.visibility = 'visible';
        this._elementRef.nativeElement.querySelector('.first-page-index').style.visibility = 'visible';
      }
    },
      error => {
        if (error.status == 403) this.router.navigate(['403']);
      }
    );
  }
}