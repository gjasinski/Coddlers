import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable, throwError} from "rxjs/index";
import {map, tap} from 'rxjs/operators';

@Injectable()
export class AuthenticationService {

  constructor(private _router: Router,
              private http: HttpClient) {
  }

  logout() {
    localStorage.removeItem("jwt");
    this._router.navigate(['']);
  }

  login(mail: string, password: string): Observable<Response> {
    const body = JSON.stringify({
      userMail: mail,
      password: password
    });

    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });
    const options = {
      headers: headers,
    };

    return this.http.post<Response>(`api/auth/`, body, options)
      .pipe(
        map((response: any) => {
          if (response.status === 403) {
            throwError(response);
          }
          const token = response.token;
          localStorage.setItem("jwt", `Bearer ${token}`);

          this._router.navigate(['/']);

          return response;
        })
      );
  }

  checkCredentials() {
    if (localStorage.getItem("jwt") === null) {
      this._router.navigate(['']);
    }
  }

  isSignedIn(): boolean {
    return localStorage.getItem('jwt') !== null;
  }

}
