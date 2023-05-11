import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from './user';
import { DepartmentComponent } from './department/department.component';
import { Directorate } from './directorate';

@Injectable({
  providedIn: 'root'
})
export class UserServiceService {

  constructor(private http: HttpClient) {
  }

  public findAll(directorate: string, department: string, offset: number): Observable<User[]> {
    return this.http.get<User[]>(`http://localhost:8080/directorate/${directorate}/department/${department}/${offset}`);
  }

  public getUserCount(department: string) {
    return this.http.get(`http://localhost:8080/count-users/${department}`);
  }

  public getFilteredUserCount(term: string, department: string) {
    return this.http.get(`http://localhost:8080/count-filtered-users/${term}/${department}`);
  }

  public save(user: User) {
    return this.http.post<User>('http://localhost:8080/register', user);
  }

  public saveDirector(user: User) {
    return this.http.post<User>('http://localhost:8080/register-director', user);
  }

  public edit(name: string, user: User) {
    return this.http.post<User>(`http://localhost:8080/edit-user/${name}`, user);
  }

  public promote(id: number) {
    return this.http.post(`http://localhost:8080/promote-user/`, id);
  }

  public demote(id: number) {
    return this.http.post(`http://localhost:8080/demote-user/`, id);
  }

  public delete(id: number) {
    return this.http.delete(`http://localhost:8080/delete-user/${id}`);
  }

  public unblock() {
    return this.http.post(`http://localhost:8080/unblock-users`, null);
  }

  public unblockUser(id: number) {
    return this.http.post(`http://localhost:8080/unblock-user/${id}`, null);
  }

  public fireDirector(directorate: string, id: number) {
    return this.http.delete(`http://localhost:8080/fire-director/${directorate}/${id}`);
  }

  public search(term: string, offset: number, department: string){
    return this.http.get<User[]>(`http://localhost:8080/search-users/${term}/${offset}/${department}`);
  }

  public validateUsername(username: string) {
    return this.http.get(`http://localhost:8080/validate-username/${username}`);
  }

  public validateUCN(UCN: string) {
    return this.http.get(`http://localhost:8080/validate-ucn/${UCN}`);
  }
}