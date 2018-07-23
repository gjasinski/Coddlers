import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {Edition} from "../models/edition";

@Injectable()
export class EditionService {
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':  'application/json'
    })
  };

  constructor(private http: HttpClient) { }

  getEdition(editionId: number): Observable<Edition> {
    return this.http.get<Edition>(`api/editions/${editionId}`)
      .pipe(
        map(obj => Edition.fromJSON(obj))
      )
  }
}
