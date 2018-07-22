import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from "@angular/router";
import {AuthenticationService} from "./authentication.service";
import {PrincipalService} from "./principal.service";
import {User} from "../models/user";
import {AccountTypesConstants} from "../constants/account-types.constants";

@Injectable({
  providedIn: 'root'
})
export class LoggedGuardService implements CanActivate {

  constructor(private router: Router, private authService: AuthenticationService,
              private principalService: PrincipalService) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    if (this.authService.isSignedIn()) {
      return this.principalService.redirectToRoleRootRoute()
        .then(() => false);

    } else {
      return true;
    }
  }
}
