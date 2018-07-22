import {Injectable} from "@angular/core";
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs/index";

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  constructor() {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    if (!request.url.includes('api/auth') &&
      !request.url.includes('api/account/register')) {
      const token = localStorage.getItem('jwt');

      if (!!token) {
        request = request.clone({
          setHeaders: {
            Authorization: token
          }
        });
      }
    }

    return next.handle(request);
  }
}
