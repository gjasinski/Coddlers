import { Injectable } from '@angular/core';
import {User} from "../models/user";
import {Observable, ObservableLike} from "rxjs/index";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {map, tap} from "rxjs/operators";
import {Router} from "@angular/router";
import {Course} from "../models/course";

@Injectable()
export class AccountService {

  constructor(private _router: Router,
              private http: HttpClient) { }

  register(user: User): Observable<any> {
    const body = user.toJSON();
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });
    const options = {
      headers: headers
    };

    return this.http.post(`api/account/register`, body, options)
      .pipe(
        tap(() => {
          this._router.navigate(['/'])
        })
      );
  }

  getAccount(): Observable<User> {
    return this.http.get<User>('api/account')
      .pipe(
        map(obj => User.fromJSON(obj))
      )
  }
}
