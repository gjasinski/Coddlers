import { Injectable } from '@angular/core';
import {AccountService} from "../services/account.service";
import {Subject} from "rxjs/internal/Subject";
import {User} from "../models/user";
import {Observable} from "rxjs/internal/Observable";

@Injectable({
  providedIn: 'root'
})
export class PrincipalService {
  private userIdentity: User;
  private authenticated = false;
  private authenticationState = new Subject<any>();

  constructor(
    private accountService: AccountService
  ) { }

  hasAnyAuthority(authorities: string[]): Promise<boolean> {
    return Promise.resolve(this.hasAnyAuthorityDirect(authorities));
  }

  hasAnyAuthorityDirect(authorities: string[]): boolean {
    if (!this.authenticated || !this.userIdentity || !this.userIdentity.userRoles) {
      return false;
    }

    for (let i = 0; i < authorities.length; i++) {
      if (this.userIdentity.userRoles.includes(authorities[i])) {
        return true;
      }
    }

    return false;
  }

  hasAuthority(authority: string): Promise<boolean> {
    if (!this.authenticated) {
      return Promise.resolve(false);
    }

    return this.identity().then((id: User) => {
      return Promise.resolve(id.userRoles && id.userRoles.includes(authority));
    }, () => {
      return Promise.resolve(false);
    });
  }

  identity(force?: boolean): Promise<User> {
    if (force === true) {
      this.userIdentity = undefined;
    }

    // check and see if we have retrieved the userIdentity data from the server.
    // if we have, reuse it by immediately resolving
    if (this.userIdentity) {
      return Promise.resolve(this.userIdentity);
    }

    return this.accountService.getAccount().toPromise().then((user: User) => {
      if (user) {
        this.userIdentity = user;
        this.authenticated = true;
      } else {
        this.userIdentity = null;
        this.authenticated = false;
      }

      this.authenticationState.next(this.userIdentity);
      return this.userIdentity;
    }).catch((err) => {
      this.userIdentity = null;
      this.authenticated = false;
      this.authenticationState.next(this.userIdentity);

      return null;
    });
  }

  getAuthenticationState(): Observable<User> {
    return this.authenticationState.asObservable();
  }

  redirectToRoot
}
