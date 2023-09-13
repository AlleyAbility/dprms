import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  private projectURL = "http://localhost:8080/api/v1/projects"
  constructor(private http:HttpClient) {

   }

   add(body:any):Observable<any>{
    return this.http.post<any>(this.projectURL,body)
   }

   get():Observable<any[]>{
    return this.http.get<any[]>(this.projectURL)
   }

   update(id: number, body: any): Observable<any> {
    const updateURL = `${this.projectURL}/${id}`; // Assuming your backend API uses the user's ID for updates
    return this.http.put<any>(updateURL, body);
  }
}
