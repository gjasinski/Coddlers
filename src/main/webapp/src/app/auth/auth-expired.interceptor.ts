import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Injector} from "@angular/core";
import {Observable} from "rxjs/internal/Observable";
import {AuthenticationService} from "./authentication.service";
import {tap} from "rxjs/operators";

export class AuthExpiredInterceptor implements HttpInterceptor {

  constructor(
    private injector: Injector
  ) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      tap((event: HttpEvent<any>) => {}, (err: any) => {
        if (err instanceof HttpErrorResponse) {
          if (err.status === 401) {
            const authService: AuthenticationService = this.injector.get(AuthenticationService);
            authService.logout();
          }
        }
      })
    );
  }
}
