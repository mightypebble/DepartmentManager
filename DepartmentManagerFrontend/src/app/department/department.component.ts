import { HttpClient } from '@angular/common/http';
import { Component, ElementRef } from '@angular/core';
import { ActivatedRoute, NavigationError, Router } from '@angular/router';
import { Department } from '../department';
import { DepartmentServiceService } from '../department-service.service';
import { User } from '../user';
import { LoginService } from '../login.service';

@Component({
  selector: 'app-department',
  templateUrl: './department.component.html',
  styleUrls: ['./department.component.scss']
})
export class DepartmentComponent {
  name = localStorage.getItem('user');
  backButtonExists: boolean = true;
  departments: any;
  department: Department;
  directorate: any;
  filteredDepartments: any;
  nameError = false;
  offset = 0;
  departmentsAreFiltered = false;
  lastTerm = '';
  lastLength = 0;
  departmentCount: any = 0;
  filteredDepartmentCount: any = 0;

  constructor(private http:HttpClient, private _elementRef : ElementRef,
    private departmentService: DepartmentServiceService, private route: ActivatedRoute, 
    private router: Router, private loginService: LoginService) {
      this.router.routeReuseStrategy.shouldReuseRoute = () => {
        return false;
      };
      this.department = new Department;
      this.directorate = String(this.route.snapshot.paramMap.get('name'));
      this.department.directorate = this.directorate;
      if (localStorage.getItem('homeRoute')?.split('/').length == 2 && 
      localStorage.getItem('homeRoute')?.split('/')[0]) this.backButtonExists = false;
    }

  onSubmit() {
    this.departmentService.validate(this.department.name).subscribe((data: any) => {
      this.nameError = data;
      if (!this.nameError) {
        this.departmentService.save(this.department)
        .subscribe(result => this.router.navigate([`directorate/${this.directorate}`]));
      }
    });
  }

  search(term: string, length: number) {
    if (length >= 1) this.departmentService.search(term, this.offset).subscribe(data => {
      this.lastTerm = term;
      this.lastLength = length;
      this.filteredDepartments = data;
      this.departmentService.search(term, this.offset + 10).subscribe(data => {
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
        this.filteredDepartments.forEach((dep: Department) => {
          dep.employees.forEach((employee: User) => {
            if (employee.position == 'head') dep.head = employee;
          });
        });
      })
    })
    else {
      this.filteredDepartments = this.departments;
      this.ngOnInit();
    }
    if (length >= 1) {
      this.departmentService.getFilteredDepartmentCount(term, this.directorate).subscribe((data: any) => {
        this.filteredDepartmentCount = data;
      })
      this.departmentsAreFiltered = true;
    }
    else {
      this.departmentsAreFiltered = false;
      this.filteredDepartmentCount = 0;
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
    if (!this.departmentsAreFiltered) this.ngOnInit();
    else this.search(this.lastTerm, this.lastLength);
  }

  prev() {
    this.offset -= 10;
    if (!this.departmentsAreFiltered) this.ngOnInit();
    else this.search(this.lastTerm, this.lastLength);
  }

  firstPage() {
    this.offset = 0;
    if (!this.departmentsAreFiltered) this.ngOnInit();
    else this.search(this.lastTerm, this.lastLength);
  }

  lastPage() {
    if (!this.departmentsAreFiltered) {
      if (this.departmentCount%10 == 0) this.offset = (Math.floor((this.departmentCount - 1) / 10) * 10);
      else this.offset = (Math.floor(this.departmentCount / 10) * 10)
      this.ngOnInit()
    }
    else {
      if (this.filteredDepartmentCount%10 == 0) this.offset = (Math.floor((this.filteredDepartmentCount - 1) / 10) * 10)
      else this.offset = (Math.floor(this.filteredDepartmentCount / 10) * 10)
      this.search(this.lastTerm, this.lastLength);
    }
  }

  editDepartment(name: string) { // change department details
    this.departmentService.edit(name, this.department).subscribe(result => this.router.navigate([`directorate/${this.directorate}`]));
  }

  deleteDepartment(id: number) {
    this.departmentService.delete(id).subscribe(result => this.router.navigate([`directorate/${this.directorate}`]));
  }

  back() {
    this.router.navigate(['/']);
  }

  showMenu(dep: Department) {
    dep.shown = !dep.shown;
  }

  editForm(dep: Department) {
    dep.editForm = !dep.editForm;
  }

  deleteConfirm(user: User) {
    user.deleteConfirm = !user.deleteConfirm;
  }

  openDep(name: string) {
    this.router.navigate([`directorate/${this.directorate}/department/${name}`]);
  }

  openAddForm() {
    this._elementRef.nativeElement.querySelector('.form-button').classList.remove('form-button--inactive');
    this._elementRef.nativeElement.querySelector('.form-button').classList.add('form-button--active');
  }

  ngOnInit() {
    this._elementRef.nativeElement.addEventListener('click', (e: any) => {
      if (!this._elementRef.nativeElement.querySelector('.department-form').contains(e.target) &&
      !this._elementRef.nativeElement.querySelector('.form-button').contains(e.target) &&
      this._elementRef.nativeElement.querySelector('.form-button').classList.contains('form-button--active')) {
        this._elementRef.nativeElement.querySelector('.form-button').classList.remove('form-button--active');
        this._elementRef.nativeElement.querySelector('.form-button').classList.add('form-button--inactive');
      }
    });
    if (!this.departmentCount) {
      this.departmentService.getDepartmentCount(this.directorate).subscribe((data: any) => {
        this.departmentCount = data;
      });
    }
    this.departmentService.findAll(this.directorate, this.offset + 10).subscribe(data => {
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
    this.departmentService.findAll(this.directorate, this.offset).subscribe((data)=>{
      this.departments = data;
      this.filteredDepartments = this.departments
      this.departments.forEach((dep: Department) => {
        dep.employees.forEach((employee: User) => {
          if (employee.position == 'head') dep.head = employee;
        });
      });
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
