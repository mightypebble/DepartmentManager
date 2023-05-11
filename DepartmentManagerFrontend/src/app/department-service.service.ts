import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Department } from './department';
import { DirectorateComponent } from './directorate/directorate.component';

@Injectable({
  providedIn: 'root'
})
export class DepartmentServiceService {

  constructor(private http: HttpClient) {
  }

  public findAll(name: string, offset: number): Observable<Department[]> {
    return this.http.get<Department[]>(`http://localhost:8080/directorate/${name}/${offset}`);
  }

  public getDepartmentCount(directorate: string) {
    return this.http.get(`http://localhost:8080/count-departments/${directorate}`);
  }

  public getFilteredDepartmentCount(term: string, directorate: string) {
    return this.http.get(`http://localhost:8080/count-filtered-departments/${term}/${directorate}`);
  }

  public save(department: Department) {
    return this.http.post<Department>('http://localhost:8080/add-department', department);
  }

  public edit(name: string, department: Department) {
    return this.http.post<Department>(`http://localhost:8080/edit-department/${name}`, department);
  }

  public delete(id: number) {
    return this.http.delete(`http://localhost:8080/delete-department/${id}`);
  }

  public search(term: string, offset: number){
    return this.http.get<Department[]>(`http://localhost:8080/search-departments/${term}/${offset}`);
  }

  public validate(name: string) {
    return this.http.get(`http://localhost:8080/validate-department/${name}`);
  }
}
