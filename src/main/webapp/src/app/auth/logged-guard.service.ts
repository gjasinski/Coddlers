import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from "@angular/router";
import {AuthenticationService} from "./authentication.service";

@Injectable({
  providedIn: 'root'
})
export class LoggedGuardService implements CanActivate {

  constructor(private router: Router, private authService: AuthenticationService) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    if (this.authService.isSignedIn()) {
      // TODO navigate to proper destination depending on role
      return false;
    } else {
      return true;
    }
  }
}
