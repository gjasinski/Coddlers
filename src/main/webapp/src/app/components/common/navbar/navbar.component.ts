import {Component} from '@angular/core';
import {AuthenticationService} from "../../../auth/authentication.service";
import {EventService} from "../../../services/event.service";

@Component({
  selector: 'cod-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
  public isCollapsed: boolean = true;

  constructor(public authService: AuthenticationService,
              public eventService: EventService) {}

  openSignInModal(): void {
    this.eventService.emit('open-sign-in-modal');
  }

  openSignUpModal(): void {
    this.eventService.emit('open-sign-up-modal');
  }

  logout(): void {
    this.authService.logout();
  }
}
