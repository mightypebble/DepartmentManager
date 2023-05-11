import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Directorate } from './directorate';

@Injectable({
  providedIn: 'root'
})
export class DirectorateServiceService {

  constructor(private http: HttpClient) {
    const httpOptions = { headers: new HttpHeaders({
       'Content-Type': 'application/json', Authorization: 'Bearer ' + localStorage.getItem("token")
      }) };
  }

  public findAll(offset: number): Observable<Directorate[]> {
    return this.http.get<Directorate[]>(`http://localhost:8080/${offset}`);
  }

  public getDirectorateCount() {
    return this.http.get(`http://localhost:8080/count-directorates`);
  }

  public getFilteredDirectorateCount(term: string) {
    return this.http.get(`http://localhost:8080/count-filtered-directorates/${term}`);
  }

  public save(directorate: Directorate) {
    return this.http.post<Directorate>('http://localhost:8080/add-directorate', directorate);
  }

  public edit(name: string, directorate: Directorate) {
    return this.http.post<Directorate>(`http://localhost:8080/edit-directorate/${name}`, directorate);
  }

  public delete(id: number) {
    return this.http.delete(`http://localhost:8080/delete-directorate/${id}`);
  }

  public search(term: string, offset: number){
    return this.http.get<Directorate[]>(`http://localhost:8080/search-directorates/${term}/${offset}`);
  }

  public validate(name: string) {
    return this.http.get(`http://localhost:8080/validate-directorate/${name}`);
  }
}
