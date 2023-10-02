import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class UserService {


  private userURL = "http://localhost:8080/api/v1/users";
  private regURL = "https://hrsample.egoz.go.tz/api/";
  private roleURL = "http://localhost:8080/api/v1/roles";

  constructor(private http:HttpClient) {}

  getUserRoles(email:string): Observable<string[]> {
    return this.http.get<string[]>(`${this.userURL}/${email}`);
  }

   add(body:any):Observable<any>{
    return this.http.post<any>(this.userURL,body)
   }

   get():Observable<any[]>{
    return this.http.get<any[]>(this.userURL)
   }

   getUserByEmployeeId(employeeId: string) {
    // Make an HTTP GET request to fetch user data based on employeeId
    return this.http.get(`${this.regURL}/employee/${employeeId}`);
  }

    getUserById(id: number){
    return this.http.get<any>(`${this.userURL}/id/${id}`)
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
    return this.http.put<any>(updateURL, {});
  }
   

  addRole(body:any):Observable<any>{
    return this.http.post<any>(this.roleURL,body)
   }

   getRoles():Observable<any[]>{
    return this.http.get<any[]>(this.roleURL)
   }

   assign(userId: number | null, roleId: number): Observable<any> {
    const assignUrl = `${this.roleURL}/assign-user-to-role?userId=${userId}&roleId=${roleId}`;
    return this.http.post<any>(assignUrl, {});
  }

  remove(userId: number | null, roleId: number): Observable<any> {
    const assignUrl = `${this.roleURL}/remove-user-from-role?userId=${userId}&roleId=${roleId}`;
    return this.http.post<any>(assignUrl, {});
  }

}
