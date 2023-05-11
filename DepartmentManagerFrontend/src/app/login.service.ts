import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from './user';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private http: HttpClient) { }

  public login(user: User) {
    return this.http.post<User>('http://localhost:8080/authenticate', user);
  }

  public setToken(token: any, user: any, route: any, status: any) {
    localStorage.setItem('token', token);
    localStorage.setItem('user', user);
    localStorage.setItem('homeRoute', route);
    localStorage.setItem('status', status);
  }

  public getToken() {
    return localStorage.getItem('token');
  }

  public getHomeRoute() {
    return localStorage.getItem('homeRoute');
  }

  public deleteToken() {
    return localStorage.clear();
  }
}