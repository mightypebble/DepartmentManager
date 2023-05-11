import { DOCUMENT } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, ElementRef, Inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Directorate } from '../directorate';
import { DirectorateServiceService } from '../directorate-service.service';
import { User } from '../user';
import { UserServiceService } from '../user-service.service';
import { LoginService } from '../login.service';

@Component({
  selector: 'app-directorate',
  templateUrl: './directorate.component.html',
  styleUrls: ['./directorate.component.scss'],
})
export class DirectorateComponent implements OnInit {

  name = localStorage.getItem('user');
  directorates: any;
  directorate: Directorate;
  filteredDirectorates: any;
  passwordError = false;
  nameError = false;
  director: User;
  offset = 0;
  directoratesAreFiltered = false;
  lastTerm = '';
  lastLength = 0;
  directorateCount: any = 0;
  filteredDirectorateCount: any = 0;

  constructor(private http:HttpClient, private _elementRef : ElementRef,
    private directorateService: DirectorateServiceService, private userService: UserServiceService,
    private route: ActivatedRoute, private router: Router, @Inject(DOCUMENT) private document: Document,
    private loginService: LoginService) {
      this.router.routeReuseStrategy.shouldReuseRoute = () => {
        return false;
      };
      this.directorate = new Directorate;
      this.director = new User;
    }

  onSubmit() { // when you add a directorate from top form
    this.directorateService.validate(this.directorate.name).subscribe((data: any) => {
      this.nameError = data;
      if (!this.nameError) {
        this.directorateService.save(this.directorate).subscribe(result => this.router.navigate(['/']));
      }
    });
  }

  search(term: string, length: number) {
    if (length >= 1) this.directorateService.search(term, this.offset).subscribe(data => {
      this.lastTerm = term;
      this.lastLength = length;
      this.filteredDirectorates = data;
      this.directorateService.search(term, this.offset + 3).subscribe(data => {
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
      this.filteredDirectorates = this.directorates;
      this.ngOnInit();
    }
    if (length >= 1) {
      this.directorateService.getFilteredDirectorateCount(term).subscribe((data: any) => {
        this.filteredDirectorateCount = data;
      })
      this.directoratesAreFiltered = true;
    }
    else {
      this.directoratesAreFiltered = false;
      this.filteredDirectorateCount = 0;
    }
    if (this.offset < 3) {
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
    this.offset += 3;
    if (!this.directoratesAreFiltered) this.ngOnInit();
    else this.search(this.lastTerm, this.lastLength);
  }

  prev() {
    this.offset -= 3;
    if (!this.directoratesAreFiltered) this.ngOnInit();
    else this.search(this.lastTerm, this.lastLength);
  }

  firstPage() {
    this.offset = 0;
    if (!this.directoratesAreFiltered) this.ngOnInit();
    else this.search(this.lastTerm, this.lastLength);
  }

  lastPage() {
    if (!this.directoratesAreFiltered) {
      if (this.directorateCount%3 == 0) this.offset = (Math.floor((this.directorateCount - 1) / 3) * 3);
      else this.offset = (Math.floor(this.directorateCount / 3) * 3)
      this.ngOnInit()
    }
    else {
      if (this.filteredDirectorateCount%3 == 0) this.offset = (Math.floor((this.filteredDirectorateCount - 1) / 3) * 3)
      else this.offset = (Math.floor(this.filteredDirectorateCount / 3) * 3)
      this.search(this.lastTerm, this.lastLength);
    }
  }

  submitDirector() { // add director to chosen directorate
    if (/^(?=(.*[a-z]){1,})(?=(.*[A-Z]){1,})(?=(.*[0-9]){1,})(?=(.*[!@#$%^&*()\-__+.]){1,}).{8,}$/
    .test(this.director.password)) {
      this.passwordError = false;
      this.userService.saveDirector(this.director).subscribe(result => this.router.navigate(['/']));
    } else {
      this.passwordError = true;
    }
  }

  logout() {
    this.loginService.deleteToken();
    this.router.navigate(['/login']);
  }

  editDirectorate(name: string) { // change directorate details
    this.directorateService.edit(name, this.directorate).subscribe(result => this.router.navigate(['/']));
  }

  deleteDirectorate(id: number) {
    this.directorateService.delete(id).subscribe(result => this.router.navigate(['/']));
  }

  setDirector(dir: string) { // sets the directorate to the director before posting him
    this.director.directorate = dir;
  }

  fireDirector(directorate: string ,id: number) {
    this.userService.fireDirector(directorate, id).subscribe(result => this.router.navigate(['/']));
  }

  unblock() {
    this.userService.unblock().subscribe(result => this.router.navigate(['/']));
  }

  unblockDirector(id: number) {
    this.userService.unblockUser(id).subscribe(result => this.router.navigate(['/']));
  }

  showMenu(dir: Directorate) { // reveales menu by activating boolean in dir entity and checking it in html
    dir.shown = !dir.shown;
  }

  dirForm(dir: Directorate) { // same as above but for for the director form
    dir.dirForm = !dir.dirForm;
    this.setDirector(dir.name); // could be causing problems in future!
  }

  editForm(dir: Directorate) {
    dir.editForm = !dir.editForm;
  }
  
  deleteConfirm(user: User) {
    user.deleteConfirm = !user.deleteConfirm;
  }

  toggleUnblockConfirm() {
    this._elementRef.nativeElement.querySelector('.unblock-confirmation').classList.toggle('display');
  }

  openDir(name: string) { // opens directorate
    this.router.navigate([`directorate/${name}`]);
  }

  ngOnInit() {
    if (!this.directorateCount) {
      this.directorateService.getDirectorateCount().subscribe((data: any) => {
        this.directorateCount = data;
      });
    }
    this.directorateService.findAll(this.offset + 3).subscribe(data => {
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
    this.directorateService.findAll(this.offset).subscribe((data) => {
      this.directorates = data;
      this.filteredDirectorates = this.directorates;
      if (this.offset < 3) {
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
