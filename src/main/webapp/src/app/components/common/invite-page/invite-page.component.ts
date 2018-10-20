import {Component, OnInit} from '@angular/core';
import {CourseEditionService} from "../../../services/course-edition.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Event} from "../../../models/event";
import {EventService} from "../../../services/event.service";
import {PrincipalService} from "../../../auth/principal.service";

@Component({
  selector: 'cod-invite-page',
  templateUrl: './invite-page.component.html',
  styleUrls: ['./invite-page.component.scss']
})
export class InvitePageComponent implements OnInit {
  private invitationToken: string;

  constructor(private editionService: CourseEditionService,
              private router: Router,
              private route: ActivatedRoute,
              private eventService: EventService,
              private principalService: PrincipalService) {
  }

  ngOnInit() {
    this.route.queryParamMap.subscribe(params => {
      this.invitationToken = params.get('invitationToken');
    });
    this.editionService.addToCourseEdition(this.invitationToken).subscribe(result => {
      this.principalService.redirectToRoleRootRoute();
      if (result)
        this.openModal();
    });
  }

  openModal(): void {
    this.eventService.emit(new Event('open-after-add-to-course-modal'));
  }
}
