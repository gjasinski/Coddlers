import {Component} from '@angular/core';

@Component({
  selector: 'cod-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
  public isCollapsed: boolean = true;

  constructor() {}

}
