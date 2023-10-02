import { HttpClient, HttpEvent, HttpHeaders, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from 'src/app/services/auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
 

  private projectURL = "http://localhost:8080/api/v1/projects"
  private documentURL = "http://localhost:8080/api/v1/documents"
  private notificationURL = "http://localhost:8080/api/v1/notifications"
  constructor(private http:HttpClient, private auth:AuthService) {

   }

   add(body:any):Observable<any>{
    // const headers = new HttpHeaders().set('userId', this.auth.userId.toString());
    return this.http.post<any>(this.projectURL,body)
   }

   get():Observable<any[]>{
    return this.http.get<any[]>(this.projectURL)
   }

   update(id: number, body: any): Observable<any> {
    const updateURL = `${this.projectURL}/${id}`; // Assuming your backend API uses the user's ID for updates
    return this.http.put<any>(updateURL, body);
  }

  getById(projectId: number): Observable<any> {
    const projectByIdURL = `${this.projectURL}/${projectId}`;
    return this.http.get<any>(projectByIdURL);
  }

  getDocument(projectId: number, selectedDocumentTitle: string): Observable<any> {
    const documentByIdURL = `${this.documentURL}/${projectId}/${selectedDocumentTitle}`;
    return this.http.get<any>(documentByIdURL);
  }



addNotification(body:any):Observable<any>{
  const notUrl = `${this.notificationURL}/create`;
  return this.http.post<any>(notUrl, body);
 }

 getNotifications():Observable<any[]>{
  const getnotifUrl = `${this.notificationURL}/all`;
  return this.http.get<any[]>(getnotifUrl)
 }

 upload(file: File, projectId: number, documentTitle: string): Observable<HttpEvent<any>> {
  const formData: FormData = new FormData();

  formData.append('file', file);
  formData.append('projectId', projectId.toString());
  formData.append('documentTitle', documentTitle);

  const req = new HttpRequest('POST', `${this.documentURL}/upload`, formData, {
    reportProgress: true,
    responseType: 'text'
  });

  return this.http.request(req);
}


assign(userId: number | null, projectId: number): Observable<any> {
  const assignUrl = `${this.projectURL}/assign-user-to-project?userId=${userId}&projectId=${projectId}`;
  return this.http.post<any>(assignUrl, {});
}


}
