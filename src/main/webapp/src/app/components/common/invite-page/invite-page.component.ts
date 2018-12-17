import {Component, OnDestroy, OnInit} from '@angular/core';
import {CourseEditionService} from "../../../services/course-edition.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Event} from "../../../models/event";
import {EventService} from "../../../services/event.service";
import {PrincipalService} from "../../../auth/principal.service";
import {switchMap} from "rxjs/internal/operators";
import {SubscriptionManager} from "../../../utils/SubscriptionManager";

@Component({
  selector: 'cod-invite-page',
  templateUrl: './invite-page.component.html',
  styleUrls: ['./invite-page.component.scss']
})
export class InvitePageComponent implements OnInit, OnDestroy {
  private invitationToken: string;
  private subscriptionManager: SubscriptionManager = new SubscriptionManager();

  constructor(private editionService: CourseEditionService,
              private router: Router,
              private route: ActivatedRoute,
              private eventService: EventService,
              private principalService: PrincipalService) {
  }

  ngOnInit() {
    let sub = this.route.queryParamMap.pipe(switchMap((params) => {
      this.invitationToken = params.get('invitationToken');
      return this.editionService.addToCourseEdition(this.invitationToken);
    }))
    .subscribe(result => {
      this.principalService.redirectToRoleRootRoute();
      this.openModal(result);
    });

    this.subscriptionManager.add(sub);
  }

  ngOnDestroy(): void {
    this.subscriptionManager.unsubscribeAll();
  }

  openModal(result): void {
    this.eventService.emit(new Event('open-after-add-to-course-modal', result));
  }
}
