import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private userURL = "http://localhost:8080/api/v1/users"
  constructor(private http:HttpClient) {

   }

   add(body:any):Observable<any>{
    return this.http.post<any>(this.userURL,body)

   }

   get():Observable<any[]>{
    return this.http.get<any[]>(this.userURL)
   }

   delete(email:string):Observable<any>{
    return this.http.delete<any>(`${this.userURL}/${email}/true`)
   }

   update(id: number, body: any): Observable<any> {
    const updateURL = `${this.userURL}/${id}`; // Assuming your backend API uses the user's ID for updates
    return this.http.put<any>(updateURL, body);
  }

  updateLockStatus(id: number, locked: boolean): Observable<any> {
    const updateURL = `${this.userURL}/${id}/${locked}`;
    // No need to create a body; lock is already in the URL
    return this.http.put<any>(updateURL, {});
  }
   

}
