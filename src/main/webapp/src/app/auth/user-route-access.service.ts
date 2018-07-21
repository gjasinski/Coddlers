import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from "@angular/router";
import {PrincipalService} from "./principal.service";
import {User} from "../models/user";

@Injectable({
  providedIn: 'root'
})
export class UserRouteAccessService implements CanActivate {

  constructor(private router: Router,
              private principalService: PrincipalService) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | Promise<boolean> {

    const authorities = route.data['authorities'];
    // We need to call the checkLogin / and so the principal.identity() function, to ensure,
    // that the client has a principal too, if they already logged in by the server.
    // This could happen on a page refresh.
    return this.checkLogin(authorities);
  }

  checkLogin(authorities: string[]): Promise<boolean> {
    return Promise.resolve(this.principalService.identity().then((user: User) => {

      if (!authorities || authorities.length === 0) {
        return true;
      }

      if (user) {
        return this.principalService.hasAnyAuthority(authorities);
      }

      // navigate to root, then logged guard service handles this
      this.router.navigate(['/']);

      return false;
    }));
  }
}
