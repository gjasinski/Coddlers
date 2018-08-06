import {Component} from '@angular/core';
import {AuthenticationService} from "../../../auth/authentication.service";
import {EventService} from "../../../services/event.service";
import {Event} from "../../../models/event"

@Component({
  selector: 'cod-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
  public isCollapsed: boolean = true;

  constructor(public authService: AuthenticationService,
              public eventService: EventService) {
  }

  openLoginModal(): void {
    this.eventService.emit(new Event('open-login-modal'));
  }

  openRegisterModal(): void {
    this.eventService.emit(new Event('open-register-modal'));
  }

  logout(): void {
    this.authService.logout();
  }
}
